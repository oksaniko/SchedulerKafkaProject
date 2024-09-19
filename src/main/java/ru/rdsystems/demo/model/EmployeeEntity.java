package ru.rdsystems.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "employees")
public class EmployeeEntity {

	@Id
	@Column(length = 32, nullable = false)
	private String id;

	@Column(name = "employee_name", nullable = false)
	private String name;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private EmployeePosition position;

	@Column(length = 20, nullable = false)
	private String status;

	public enum EmployeePosition{
		MANAGER, EMPLOYEE, UNDEFINED, TECH
	}

	public static EmployeeEntity fromString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, EmployeeEntity.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
