package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFilters200ResponseInner;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersRequest;

import java.util.List;

public interface TimetableService {

	GetTimetableForFiltersRequest setParamRequest();
	Float calcWorkHours(List<GetTimetableForFilters200ResponseInner> timetable, EmployeeEntity employee);

}
