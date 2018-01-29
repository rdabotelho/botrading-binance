package com.m2r.botrading.binance;

import com.m2r.botrading.api.model.Currency;
import com.m2r.botrading.api.model.IApiAccess;
import com.m2r.botrading.api.model.MarketCoin;
import com.m2r.botrading.api.service.IExchangeService;
import com.m2r.botrading.api.service.IExchangeSession;

public class TesteBinanceExchange {

	public static void main(String[] args) throws Exception {

		String currencyPair = "TRXBTC";
		TesteAPIAccess apiAccess = new TesteAPIAccess();

		IExchangeService exchangeService = new BinanceExchange().init();
		MarketCoin marketCoin = exchangeService.getMarketCoin(Currency.BTC);

		// Map<String, Currency> currencies = marketCoin.getCurrencies();
		// currencies.forEach((k, v) -> {
		// System.out.println(k + " = " + v.getCurrencyPair());
		// });

		IExchangeSession session = exchangeService.getSession(marketCoin, false, true);

		// IChartDataList chartDatas = session.getChartDatas(currencyPair,
		// DataChartPeriod.FIFTEEN_MINUTES, LocalDateTime.now(),
		// LocalDateTime.now());

		// System.out.println(chartDatas);

		// String orderBuyId = session.buy(apiAccess, currencyPair,
		// "0.00000290", "387");
		// session.cancel(apiAccess, orderBuyId, currencyPair);

		//IBalanceList banlances = session.getBanlances(apiAccess);

		// IOrderList orders = session.getOrders(apiAccess);

		// ITickerList tikers = session.getTikers();
		
		/*
		 * API Key:
2vnlV0ADtayHMk5CH7YW7ucZhJzfQzrBapwg62K3EtsayA2zuLQ7kQq2QK5ryKsR
Secret:
ibaQvDyjfK5br0f3CGZJ6lOvuWrXuQ3A9KnTslCGn2IW7xJSctQIsQStSuf8tlNZ
		 */

	}

	public static class TesteAPIAccess implements IApiAccess {

		private String secretKey = "K2bNh0BpLlliPLvD6Uk1fRqVaw8YrRw8tTT7KzM0Tmtbqh5bTOvlDAtBvoyKjXjb";
		private String apiKey = "Uii9uCoSrQb7hcfqN1EGJituiAbXX9nQYrg8hmxHaOloWXTjqtzx5Nap6hakfVqO";

		public String getSecretKey() {
			return secretKey;
		}

		@Override
		public String getApiKey() {
			return apiKey;
		}
	}

}
