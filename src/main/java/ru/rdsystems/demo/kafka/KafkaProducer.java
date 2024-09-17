package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public boolean sendMessage(String topic, String message){
		boolean result = false;
		try {
			kafkaTemplate.send(topic, message);
			result = true;
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return result;
	}

}
