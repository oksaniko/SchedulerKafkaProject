package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.repository.EmployeeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

	private final EmployeeRepository employeeRepository;

	@KafkaListener(id = "${kafka.consumer}", topics = { "${kafka.topic.employeeData}" })
	public void listenEmployeeMessages(String message){
		try{
			EmployeeEntity employee = EmployeeEntity.fromString(message);
			log.info("Get employee {}", employee);
			employeeRepository.save(employee);
		}catch (Exception ex) {
			log.error("Message {} can't to be parse: {}", message, ex.getMessage());
		}
	}

}
