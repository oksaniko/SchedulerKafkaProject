package ru.rdsystems.demo.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.ReportEntity;
import ru.rdsystems.demo.repository.ReportRepository;
import ru.rdsystems.demo.service.ReportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final ReportRepository repository;

	@Override
	public ReportEntity createReport(String repId, EmployeeEntity employee, Float hours, Double salary, LocalDate beginDate) {
		if(beginDate.isAfter(LocalDate.now()))
			beginDate = LocalDate.now();
		ReportEntity entity = new ReportEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				repId, employee, hours, salary, LocalDateTime.now(), beginDate, LocalDate.now()
		);
		repository.save(entity);
		return entity;
	}

}
