package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.ReportEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

	ReportEntity createReport(String repId, EmployeeEntity employee, Float hours, Double salary, LocalDate beginDate);

	List<ReportEntity> getByReportId(String id);

	List<ReportEntity> getByIdWithCurrency(String id, String currency);

	List<ReportEntity> updateReportById(String id);

}
