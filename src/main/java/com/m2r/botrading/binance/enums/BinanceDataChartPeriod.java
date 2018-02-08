package com.m2r.botrading.binance.enums;

import com.m2r.botrading.api.model.IDataChartPeriod;

public enum BinanceDataChartPeriod implements IDataChartPeriod {

	ONE_MINUTE("1m"),
	THREE_MINUTES("3m"),
	FIVE_MINUTES("5m"),
	FIFTEEN_MINUTES("15m"),
	HALF_HOURLY("30m"),
	HOURLY("1h"),
	TWO_HOURLY("2h"),
	FOUR_HORLY("4h"),
	SIX_HOURLY("6h"),
	EIGHT_HOURLY("8h"),
	TWELVE_HOURLY("12h"),
	DAILY("1d"),
	THREE_DAILY("3d"),
	WEEKLY("1w"),
	MONTHLY("1M");

	private String seconds;
	
	BinanceDataChartPeriod(String period) {
		this.seconds = period;
	}
	
	public String getSeconds() {
		return seconds;
	}	
	
}
