package ru.rdsystems.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.remote.CurrencyClient;
import ru.rdsystems.demo.remote.TimetableClient;
import ru.rdsystems.demo.scheduler.schedulerApi.FilterTimetable;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersRequest;
import ru.rdsystems.demo.scheduler.schedulerApi.SortTimetable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobService {

	private final TimetableClient timetableClient;

	@Scheduled(cron = "${cron.report}")
	@ConditionalOnProperty(name = "report.scheduler.enabled", havingValue = "true", matchIfMissing = true)
	@Async
	public void createReport(){
		// установка параметров запроса
		FilterTimetable filter = new FilterTimetable();
		filter.setScheduleId("c536aa0c9a5847ca87b6709db7a33a06"); // расписание "Полный рабочий день"
		SortTimetable sort = new SortTimetable();
		sort.setField("slotType");
		sort.setDirection("ASC");
		GetTimetableForFiltersRequest params = new GetTimetableForFiltersRequest();
		params.setFilter(filter);
		params.setSort(sort);

		ResponseEntity<List<GetTimetableForFilters200ResponseInner>> timetableResponse = timetableClient.getTimetableForFilters(params);
		Map<String, Object> json;
		if(timetableResponse.getStatusCode().is2xxSuccessful()){
			//ReportEntity entity = reportService.createReport(responseEntity.getBody());
			//json = Map.of("id", entity.getId());
			json = Map.of("id", timetableResponse.getBody());
		} else {
			json = Map.of("error", timetableResponse.getStatusCode());
		}
		StringBuilder stringBuilder = new StringBuilder(LocalDateTime.now().toString());
		stringBuilder.append(" ");
		stringBuilder.append(json);
		System.out.println(stringBuilder);
	}
}
