package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

	private final EmployeeRepository employeeRepository;

	@KafkaListener(id = "${kafka.consumer}", topics = { "${kafka.topic.employeeData}" })
	public void listenEmployeeMessages(String message){
		try{
			EmployeeEntity employee = EmployeeEntity.fromString(message);
			System.out.println("employee = " + employee);
			employeeRepository.save(employee);
		}catch (Exception ex) {
			System.out.println("Message " + message + " can't to be parse: " + ex.getMessage());
		}
	}

}
