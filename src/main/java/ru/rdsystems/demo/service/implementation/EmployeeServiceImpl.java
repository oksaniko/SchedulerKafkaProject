package ru.rdsystems.demo.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.repository.EmployeeRepository;
import ru.rdsystems.demo.service.EmployeeService;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;

	@Override
	public EmployeeEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
	}

	private Double getHourSalaryByPosition(EmployeeEntity.EmployeePosition position) {
		Double min = 0d, max = 0d;
		switch(position) {
			case MANAGER:
				min =100000d; max = 150000d; break;
			case EMPLOYEE:
				min = 50000d; max = 70000d; break;
			case TECH:
				min = 30000d; max = 50000d; break;
		}
		return new Random().doubles(min, max).limit(1).findFirst().getAsDouble();
	}

	@Override
	public Double getSalaryByPosition(EmployeeEntity employee, Float hours) {
		return getHourSalaryByPosition(employee.getPosition()) * hours / 8;
	}

}
