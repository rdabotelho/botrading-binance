package com.m2r.botrading.binance.model;

import com.m2r.botrading.api.model.CurrencyPairIds;
import com.m2r.botrading.api.model.ICurrencyPairConverter;

public class BinanceCurrencyPairConverter implements ICurrencyPairConverter {

	private static ICurrencyPairConverter instance;
	
	public static ICurrencyPairConverter getInstance() {
		if (instance == null) {
			instance = new BinanceCurrencyPairConverter();
		}
		return instance;
	}

	@Override
	public String currencyPairToString(CurrencyPairIds object) {
		return String.format("%s%s", object.getCurrencyId(), object.getMarketCoinId());
	}

	@Override
	public CurrencyPairIds stringToCurrencyPair(String text) {
		if (text.endsWith("USDT")) {
			return CurrencyPairIds.of(text.substring(text.length() - 4), text.substring(0, text.length() - 4));
		}
		else {
			return CurrencyPairIds.of(text.substring(text.length() - 3), text.substring(0, text.length() - 3));
		}
	}

}
