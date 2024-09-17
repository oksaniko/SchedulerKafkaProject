package ru.rdsystems.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

	@KafkaListener(id = "${kafka.consumer}", topics = { "${kafka.topic.employeeData}" })
	public void listenMessages(String message){
		System.out.println("silkachevaon message = " + message);
	}

}
