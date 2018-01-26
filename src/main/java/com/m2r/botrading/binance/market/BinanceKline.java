package com.m2r.botrading.binance.market;

import java.math.BigDecimal;

import com.m2r.botrading.api.model.IChartData;

public final class BinanceKline implements IChartData {

	public final String openTime;
	public final BigDecimal open;
	public final BigDecimal high;
	public final BigDecimal low;
	public final BigDecimal close;
	public final BigDecimal volume;
	public final String closeTime;
	public final BigDecimal quoteAssetVolume;
	public final String numberOfTrades;
	public final BigDecimal takerBuyBaseAssetVolume;
	public final BigDecimal takerBuyQuoteAssetVolume;

	public BinanceKline(Object[] obj) {
		openTime = obj[0].toString();
		open = new BigDecimal(obj[1].toString());
		high = new BigDecimal(obj[2].toString());
		low = new BigDecimal(obj[3].toString());
		close = new BigDecimal(obj[4].toString());
		volume = new BigDecimal(obj[5].toString());
		closeTime = new BigDecimal(obj[6].toString()).toString();
		quoteAssetVolume = new BigDecimal(obj[7].toString());
		numberOfTrades = obj[8].toString();
		takerBuyBaseAssetVolume = new BigDecimal(obj[9].toString());
		takerBuyQuoteAssetVolume = new BigDecimal(obj[10].toString());
	}

	@Override
	public String getDate() {
		return this.openTime;
	}

	@Override
	public String getHigh() {
		return this.high.toString();
	}

	@Override
	public String getLow() {
		return this.low.toString();
	}

	@Override
	public String getOpen() {
		return this.open.toString();
	}

	@Override
	public String getClose() {
		return this.close.toString();
	}

	@Override
	public String getVolume() {
		return volume.toString();
	}

	@Override
	public String getQuoteVolume() {
		return this.quoteAssetVolume.toString();
	}

	@Override
	public String getWeightedAverage() {
		// TODO Auto-generated method stub
		return null;
	}
}
