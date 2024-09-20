package ru.rdsystems.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.model.ReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {
}
