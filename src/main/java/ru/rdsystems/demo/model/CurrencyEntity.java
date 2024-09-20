package ru.rdsystems.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency")
public class CurrencyEntity {

	@Id
	@Column(length = 32, nullable = false)
	private String id;

	@Column(name = "report_date", nullable = false)
	private LocalDate reportDate;

	@Column(length = 3, nullable = false)
	private String currCode;

	@Column(nullable = false)
	private Double currValue;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime creationDate;

}
