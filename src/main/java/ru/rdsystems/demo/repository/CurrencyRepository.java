package ru.rdsystems.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.model.CurrencyEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

	List<CurrencyEntity> findByReportDate(LocalDate repDate);

	@Query("select ce from CurrencyEntity ce where ce.currCode = :code and ce.reportDate <= :reportDate order by ce.reportDate desc")
	List<CurrencyEntity> findWithCodeAndDate(LocalDate reportDate, String code);

}
