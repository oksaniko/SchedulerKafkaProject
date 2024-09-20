package ru.rdsystems.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "reports")
public class ReportEntity {

	@Id
	@Column(length = 32, nullable = false)
	private String id;

	@Column(name = "report_id", length = 32, nullable = false)
	private String reportId;

	@ManyToOne
	@ToString.Exclude
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private EmployeeEntity employee;

	@Column(nullable = false)
	private Float hours;

	@Column(nullable = false)
	private Double salary;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime creationDate;

	@Column(nullable = false)
	private LocalDate beginDate;

	@Column(nullable = false)
	private LocalDate endDate;

}
