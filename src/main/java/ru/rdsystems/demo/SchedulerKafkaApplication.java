package ru.rdsystems.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(Configurator.class)
public class SchedulerKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerKafkaApplication.class, args);
	}

}
