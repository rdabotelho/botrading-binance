package com.m2r.botrading.binance.model;

import com.m2r.botrading.api.model.CurrencyFactory;
import com.m2r.botrading.api.model.ICurrencyPairConverter;

public class BinanceCurrencyFactory extends CurrencyFactory {

	private static CurrencyFactory instance;
	
	public static CurrencyFactory getInstance() {
		if (instance == null) {
			instance = new BinanceCurrencyFactory();
		}
		return instance;
	}
	
	@Override
	public ICurrencyPairConverter getCurrencyPairConverter() {
		return BinanceCurrencyPairConverter.getInstance();
	}

}
