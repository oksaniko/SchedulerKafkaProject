package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.KafkaErrorMessage;

public interface KafkaErrorMessageService {

	KafkaErrorMessage createIfNotExists(String message, String topic, String errorTxt);

	void deleteById(String id);

}
