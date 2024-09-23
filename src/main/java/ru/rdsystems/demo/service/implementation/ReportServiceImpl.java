package ru.rdsystems.demo.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.CurrencyEntity;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.ReportEntity;
import ru.rdsystems.demo.remote.TimetableClient;
import ru.rdsystems.demo.repository.CurrencyRepository;
import ru.rdsystems.demo.repository.ReportRepository;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.service.EmployeeService;
import ru.rdsystems.demo.service.ReportService;
import ru.rdsystems.demo.service.TimetableService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final ReportRepository repository;
	private final CurrencyRepository currencyRepository;
	private final TimetableClient timetableClient;
	private final TimetableService timetableService;
	private final EmployeeService employeeService;
	private final KafkaProducer kafkaProducer;

	@Value("${kafka.topic.reports}")
	private String topicReports;

	@Override
	public ReportEntity createReport(String repId, EmployeeEntity employee, Float hours, Double salary, LocalDate beginDate) {
		if(beginDate.isAfter(LocalDate.now()))
			beginDate = LocalDate.now();
		ReportEntity entity = new ReportEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				repId, employee, hours, salary, LocalDateTime.now(), beginDate, LocalDate.now()
		);
		repository.save(entity);

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String message = objectMapper.writeValueAsString(employee);
			System.out.println("to kafka:" + message);
			kafkaProducer.sendMessage(topicReports, message);
		} catch (JsonProcessingException e) {
			System.out.println("Ошибка упаковки в json " + e.getMessage());
			throw new RuntimeException(e);
		}

		return entity;
	}

	@Override
	public List<ReportEntity> getByReportId(String id) {
		return repository.findByReportId(id);
	}

	@Override
	public List<ReportEntity> getByIdWithCurrency(String id, String currency) {
		List<ReportEntity> entityList = getByReportId(id);
		if(!currency.isEmpty()){
			List<CurrencyEntity> currList = currencyRepository.findWithCodeAndDate(
					entityList.isEmpty() ? LocalDate.now() : entityList.get(0).getEndDate(), currency);
			if(!currList.isEmpty()){
				for(ReportEntity report : entityList)
					report.setSalary( report.getSalary() * currList.get(0).getCurrValue() );
			}
		}
		return entityList;
	}

	@Override
	public List<ReportEntity> updateReportById(String id) {
		List<ReportEntity> entityList = getByReportId(id);
		ResponseEntity<List<GetTimetableForFilters200ResponseInner>> timetableResponse =
				timetableClient.getTimetableForFilters(timetableService.setParamRequest());
		if(timetableResponse.getStatusCode().is2xxSuccessful()){
			for(ReportEntity report : entityList){
				Float hours = timetableService.calcWorkHours(timetableResponse.getBody(), report.getEmployee());
				report.setHours(hours);
				report.setSalary(employeeService.getSalaryByPosition(report.getEmployee(), hours));
				repository.save(report);
			}
		}
		return entityList;
	}

}
