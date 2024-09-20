package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.ReportEntity;

import java.time.LocalDate;

public interface ReportService {

	ReportEntity createReport(String repId, EmployeeEntity employee, Float hours, Double salary, LocalDate beginDate);

}
