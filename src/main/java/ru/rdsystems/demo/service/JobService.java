package ru.rdsystems.demo.service;

import java.io.IOException;

public interface JobService {

	void createReport();
	void getCurrencyRate() throws IOException;
}
