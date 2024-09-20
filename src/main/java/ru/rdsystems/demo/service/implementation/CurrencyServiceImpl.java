package ru.rdsystems.demo.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.CurrencyEntity;
import ru.rdsystems.demo.repository.CurrencyRepository;
import ru.rdsystems.demo.service.CurrencyService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

	private final CurrencyRepository repository;

	@Override
	public CurrencyEntity createCurrency(String code, Double val, LocalDate valueDate) {
		CurrencyEntity entity = new CurrencyEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				valueDate, code, val, LocalDateTime.now()
		);
		repository.save(entity);
		return entity;
	}

}
