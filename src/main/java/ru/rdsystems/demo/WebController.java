package ru.rdsystems.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rdsystems.demo.kafka.KafkaConsumer;
import ru.rdsystems.demo.kafka.KafkaProducer;

@RestController
@RequiredArgsConstructor
public class WebController {

	private final KafkaConsumer consumer;
	private final KafkaProducer producer;

	@Value("${kafka.topic.reports}")
	private String topicProducer;

	@PostMapping("/kafka/test/{topic_id}")
	public ResponseEntity<String> putToKafka(@PathVariable String topic_id, @RequestBody String message){
		HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		String responseMessage;
		if (!topicProducer.equals(topic_id)){
			responseMessage = "Топик " + topic_id + " неизвестен";
		} else{
			if (producer.sendMessage(topic_id, message)){
				responseStatus = HttpStatus.OK;
				responseMessage = "Сообщение успешно отправлено";
			}
			else
				responseMessage = "Сообщение не отправлено";
		}
		return ResponseEntity.status(responseStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(responseMessage);
	}

}
