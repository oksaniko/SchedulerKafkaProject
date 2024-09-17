package ru.rdsystems.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebController {

	@PostMapping("/kafka/test/osilkacheva")
	public ResponseEntity putToKafka(String message){
		return ResponseEntity.ok("success");
	}

}
