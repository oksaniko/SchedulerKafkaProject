package ru.rdsystems.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rdsystems.demo.model.KafkaErrorMessage;

import java.util.Optional;

public interface KafkaErrorMessageRepository extends JpaRepository<KafkaErrorMessage, String> {

	Optional<KafkaErrorMessage> findByMessageAndTopicName(String message, String topic);

}
