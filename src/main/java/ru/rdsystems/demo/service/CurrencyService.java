package ru.rdsystems.demo.service;

import ru.rdsystems.demo.model.CurrencyEntity;

import java.time.LocalDate;

public interface CurrencyService {

	CurrencyEntity createCurrency(String code, Double val, LocalDate valueDate);

}
