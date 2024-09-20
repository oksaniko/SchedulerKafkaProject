package ru.rdsystems.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.model.CurrencyEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

	List<CurrencyEntity> findByReportDate(LocalDate repDate);

}
