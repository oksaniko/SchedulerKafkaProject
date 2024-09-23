package ru.rdsystems.demo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.model.ReportEntity;
import ru.rdsystems.demo.repository.EmployeeRepository;
import ru.rdsystems.demo.repository.ReportRepository;
import ru.rdsystems.demo.service.EmployeeService;
import ru.rdsystems.demo.service.ReportService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WebController {

	private final KafkaProducer producer;
	private final EmployeeRepository employeeRepository;
	private final EmployeeService employeeService;
	private final ReportRepository reportRepository;
	private final ReportService reportService;

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

	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeEntity>> getEmployees(){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(employeeRepository.findAll());
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Map<String, String>> getEmployeeInfo(@PathVariable String id){
		HttpStatus responseStatus;
		Map<String, String> json = new HashMap<>();
		try{
			EmployeeEntity employee = employeeService.getById(id);
			responseStatus = HttpStatus.OK;
			json = Map.of("employee", employee.toString());
		} catch (EntityNotFoundException e){
			responseStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(responseStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(json);
	}

	@GetMapping("/reports")
	public ResponseEntity<List<Map<String, Object>>> getReports(){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(reportRepository.findDistinct());
	}

	@GetMapping("/reports/{id}")
	public ResponseEntity<List<ReportEntity>> getReportById(
			@PathVariable String id,
			@RequestParam(required = false) String currency
	){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(reportService.getByIdWithCurrency(id, currency == null ? "" : currency));
	}

	@PatchMapping("/reports/{id}/refresh")
	public ResponseEntity<String> updateReportById(@PathVariable String id){
		ResponseEntity<String> response;
		if(!reportService.updateReportById(id).isEmpty())
			response = ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body("Success");
		else
			response = ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body("Report is not found");
		return response;
	}

}
