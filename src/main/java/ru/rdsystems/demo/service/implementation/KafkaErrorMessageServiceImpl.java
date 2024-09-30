package ru.rdsystems.demo.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.KafkaErrorMessage;
import ru.rdsystems.demo.repository.KafkaErrorMessageRepository;
import ru.rdsystems.demo.service.KafkaErrorMessageService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaErrorMessageServiceImpl implements KafkaErrorMessageService {

	private final KafkaErrorMessageRepository repository;

	private KafkaErrorMessage create(String message, String topic, String errorTxt){
		KafkaErrorMessage errorMessage = new KafkaErrorMessage(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				LocalDateTime.now(), topic, message, errorTxt
		);
		repository.save(errorMessage);
		return errorMessage;
	}

	@Override
	public KafkaErrorMessage createIfNotExists(String message, String topic, String errorTxt){
		return repository.findByMessageAndTopicName(message, topic)
				.orElseGet(() -> create(message, topic, errorTxt));
	}

	@Override
	public void deleteById(String id){
		repository.deleteById(id);
	}

}
