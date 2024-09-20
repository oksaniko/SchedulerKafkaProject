package ru.rdsystems.demo.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersApi;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersRequest;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "timetableRemoteClient", url = "${remote.timetable.url}")
public interface TimetableClient extends GetTimetableForFiltersApi {

	@Override
	@PostMapping
	ResponseEntity<List<GetTimetableForFilters200ResponseInner>> getTimetableForFilters(
			@Valid GetTimetableForFiltersRequest getTimetableForFiltersRequest);

}
