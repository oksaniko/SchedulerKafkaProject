package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public boolean sendMessage(String topic, String message){
		boolean result = false;
		try {
			log.info("Sending message='{}' to topic='{}'", message, topic);
			kafkaTemplate.send(topic, message);
			result = true;
		} catch (Exception e){
			log.error(e.getMessage());
		}
		return result;
	}

}
