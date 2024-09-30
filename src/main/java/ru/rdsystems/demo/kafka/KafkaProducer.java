package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.KafkaErrorMessage;
import ru.rdsystems.demo.service.KafkaErrorMessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	private final KafkaErrorMessageService service;

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
			KafkaErrorMessage errorMessage = service.createIfNotExists(message, topic, e.getMessage());
			log.info("Error message sended to error list, id = {}", errorMessage.getId());
		}
		return result;
	}

}
