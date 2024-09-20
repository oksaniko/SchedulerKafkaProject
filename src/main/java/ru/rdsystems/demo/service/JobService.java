package ru.rdsystems.demo.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.remote.CurrencyClient;
import ru.rdsystems.demo.remote.TimetableClient;
import ru.rdsystems.demo.repository.CurrencyRepository;
import ru.rdsystems.demo.scheduler.schedulerApi.FilterTimetable;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersRequest;
import ru.rdsystems.demo.scheduler.schedulerApi.SortTimetable;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobService {

	private final TimetableClient timetableClient;
	private final CurrencyClient currencyClient;
	private final CurrencyService currencyService;
	private final CurrencyRepository currencyRepository;

	@Value("${remote.currency.url}")
	private String remoteCurrencyUrl;

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

	@Scheduled(cron = "${cron.currency}")
	@ConditionalOnProperty(name = "currency.scheduler.enabled", havingValue = "true", matchIfMissing = true)
	public void getCurrencyRate() throws IOException {
		/*ResponseEntity<Object> currencyResponse = currencyClient.getCurrencyCbr();
		Map<String, Object> json;
		if (currencyResponse.getStatusCode().is2xxSuccessful()){
			json = Map.of("id", currencyResponse.getBody());
		}else {
			json = Map.of("error", currencyResponse.getStatusCode());
		}*/

		JSONObject json = new JSONObject(
				new String(
						new URL(remoteCurrencyUrl).openStream().readAllBytes()
				));
		LocalDate valueDate = LocalDate.parse(json.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		if(currencyRepository.findByReportDate(valueDate).isEmpty()){
			JSONObject currenciesData = json.getJSONObject("rates");
			for(String currency: currenciesData.keySet()) {
				currencyService.createCurrency(
					currency,
					Double.valueOf(currenciesData.get(currency).toString()),
					valueDate);
			}
			System.out.println("currency rates are loaded");
		}else
			System.out.println("currencyRepository is not empty");
	}
}
