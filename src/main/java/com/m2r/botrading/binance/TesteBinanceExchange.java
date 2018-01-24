package com.m2r.botrading.binance;

import java.time.LocalDateTime;
import java.util.Map;

import com.m2r.botrading.api.enums.DataChartPeriod;
import com.m2r.botrading.api.model.IApiAccess;
import com.m2r.botrading.api.model.IBalanceList;
import com.m2r.botrading.api.model.ICurrency;
import com.m2r.botrading.api.model.IMarketCoin;
import com.m2r.botrading.api.model.IOrderList;
import com.m2r.botrading.api.model.ITickerList;
import com.m2r.botrading.api.model.MarketCoin;
import com.m2r.botrading.api.service.IExchangeService;
import com.m2r.botrading.api.service.IExchangeSession;

public class TesteBinanceExchange {

	public static void main(String[] args) throws Exception {
		
		String currencyPair = "TRXBTC";
		TesteAPIAccess apiAccess = new TesteAPIAccess();

		IExchangeService exchangeService = new BinanceExchange().init();
		IMarketCoin marketCoin = exchangeService.getMarketCoin(MarketCoin.BTC);

//		Map<String, ICurrency> currencies = marketCoin.getCurrencies();
//		currencies.forEach((k, v) -> {
//			System.out.println(k + " = " + v.getCurrencyPair());
//		});

		IExchangeSession session = exchangeService.getSession(marketCoin, false, true);
		
		session.getChartDatas(currencyPair, DataChartPeriod.FIFTEEN_MINUTES, LocalDateTime.now(), LocalDateTime.now());

		// String orderBuyId = session.buy(apiAccess, "TRXBTC", "0.00000290",
		// "387");
		// session.cancel(apiAccess, orderBuyId, "TRXBTC");

		//IBalanceList banlances = session.getBanlances(apiAccess);

		//IOrderList orders = session.getOrders(apiAccess); // Ver 
		
		//ITickerList tikers = session.getTikers();

	}

	public static class TesteAPIAccess implements IApiAccess {
		
		private String secretKey = "SUA_KEY_MAIS_NAO_PRECISA_PARA_PEGAR_DADOS_DO_CHART";
		private String apiKey = "SUA_KEY_MAIS_NAO_PRECISA_PARA_PEGAR_DADOS_DO_CHART";

		public String getSecretKey() {
			return secretKey;
		}

		@Override
		public String getApiKey() {
			return apiKey;
		}
	}

}
