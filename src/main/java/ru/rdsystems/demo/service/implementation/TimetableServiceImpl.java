package ru.rdsystems.demo.service.implementation;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.*;
import ru.rdsystems.demo.service.TimetableService;

import java.time.Duration;
import java.util.List;

@Service
@NoArgsConstructor
public class TimetableServiceImpl implements TimetableService {

	private FilterTimetable setFilterTimetable(){
		FilterTimetable filter = new FilterTimetable();
		filter.setScheduleId("c536aa0c9a5847ca87b6709db7a33a06"); // расписание "Полный рабочий день"
		filter.setSlotType("FROM_HOME");
		return filter;
	}

	private SortTimetable setSortTimetable(){
		SortTimetable sort = new SortTimetable();
		sort.setField("slotType");
		sort.setDirection("ASC");
		return sort;
	}

	@Override
	public GetTimetableForFiltersRequest setParamRequest(){
		GetTimetableForFiltersRequest params = new GetTimetableForFiltersRequest();
		params.setFilter(setFilterTimetable());
		params.setSort(setSortTimetable());
		return params;
	}

	@Override
	public Float calcWorkHours(List<GetTimetableForFilters200ResponseInner> timetableList, EmployeeEntity employee){
		Float hours = 0f;
		for(GetTimetableForFilters200ResponseInner timetable : timetableList) {
			if (timetable.getExecutor().getId().equals(employee.getId())) {
				Slot slot = timetable.getSlot();
				hours += Float.valueOf(Duration.between(slot.getBeginTime(), slot.getEndTime()).toMinutes() / 60);
			}
		}
		return hours;
	}

}
