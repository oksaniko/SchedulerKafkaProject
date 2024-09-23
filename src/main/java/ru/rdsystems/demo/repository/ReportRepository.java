package ru.rdsystems.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.model.ReportEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {

	@Query(value = "select distinct report_id, begin_date, end_date from reports", nativeQuery = true)
	List<Map<String, Object>> findDistinct();

	List<ReportEntity> findByReportId(String reportId);
}
