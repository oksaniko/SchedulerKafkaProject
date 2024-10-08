package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.EmployeeEntity;

public interface EmployeeService {

	EmployeeEntity getById(String id);

	Double getSalaryByPosition(EmployeeEntity employee, Float hours);

}
