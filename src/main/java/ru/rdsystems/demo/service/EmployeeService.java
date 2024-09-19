package ru.rdsystems.demo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository repository;

	public EmployeeEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
	}

}
