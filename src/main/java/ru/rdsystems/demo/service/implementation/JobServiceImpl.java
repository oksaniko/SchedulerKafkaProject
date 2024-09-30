package ru.rdsystems.demo.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.KafkaErrorMessage;
import ru.rdsystems.demo.model.ReportEntity;
import ru.rdsystems.demo.remote.CurrencyClient;
import ru.rdsystems.demo.remote.TimetableClient;
import ru.rdsystems.demo.repository.CurrencyRepository;
import ru.rdsystems.demo.repository.EmployeeRepository;
import ru.rdsystems.demo.repository.KafkaErrorMessageRepository;
import ru.rdsystems.demo.repository.ReportRepository;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.service.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

	private final TimetableClient timetableClient;
	private final TimetableService timetableService;
	private final CurrencyClient currencyClient;
	private final CurrencyService currencyService;
	private final CurrencyRepository currencyRepository;
	private final ReportService reportService;
	private final ReportRepository reportRepository;
	private final EmployeeService employeeService;
	private final EmployeeRepository employeeRepository;
	private final KafkaErrorMessageRepository kafkaErrorRepository;
	private final KafkaErrorMessageService kafkaErrorService;
	private final KafkaProducer producer;

	@Value("${remote.currency.url}")
	private String remoteCurrencyUrl;

	@Override
	@Scheduled(cron = "${cron.report}")
	@ConditionalOnProperty(name = "report.scheduler.enabled", havingValue = "true", matchIfMissing = true)
	@Async
	public void createReport(){
		List<ReportEntity> reportList = reportRepository.findAll(Sort.by(Sort.Direction.DESC, "creationDate"));
		LocalDate beginDate = reportList.isEmpty() ? LocalDate.now() : reportList.get(0).getEndDate().plusDays(1);
		String reportId = UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT);
		ResponseEntity<List<GetTimetableForFilters200ResponseInner>> timetableResponse =
				timetableClient.getTimetableForFilters(timetableService.setParamRequest());
		if(timetableResponse.getStatusCode().is2xxSuccessful()){
			for(EmployeeEntity employee : employeeRepository.findAll()){
				Float hours = timetableService.calcWorkHours(timetableResponse.getBody(), employee);
				reportService.createReport(reportId, employee, hours, employeeService.getSalaryByPosition(employee, hours), beginDate);
			}
		}
	}

	@Override
	@Scheduled(cron = "${cron.currency}")
	@ConditionalOnProperty(name = "currency.scheduler.enabled", havingValue = "true", matchIfMissing = true)
	public void getCurrencyRate() throws IOException {
		//ResponseEntity<Object> currencyResponse = currencyClient.getCurrencyCbr();
		JSONObject json = new JSONObject(
				new String(
						new URL(remoteCurrencyUrl).openStream().readAllBytes()
				));
		LocalDate valueDate = LocalDate.parse(json.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if(currencyRepository.findByReportDate(valueDate).isEmpty()){
			JSONObject currenciesData = json.getJSONObject("rates");
			for(String currency: currenciesData.keySet()) {
				currencyService.createCurrency(
					currency,
					Double.valueOf(currenciesData.get(currency).toString()),
					valueDate);
			}
			log.info("Currency rates are loaded");
		}else
			log.info("CurrencyRepository is not empty");
	}

	@Override
	@Scheduled(cron = "${cron.kafka.error}")
	public void repeatSendToKafka() {
		List<KafkaErrorMessage> errorList = kafkaErrorRepository.findAll();
		if(errorList.isEmpty())
			log.info("No message for repeat sending");
		else
			for(KafkaErrorMessage message : errorList) {
				if(producer.sendMessage(message.getTopicName(), message.getMessage())){
					kafkaErrorService.deleteById(message.getId());
					log.info("Message '{}' send", message);
				}
			}
	}
}
