package com.m2r.botrading.binance;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.m2r.botrading.api.enums.DataChartPeriod;
import com.m2r.botrading.api.exception.ExchangeException;
import com.m2r.botrading.api.model.Currency;
import com.m2r.botrading.api.model.IApiAccess;
import com.m2r.botrading.api.model.IBalance;
import com.m2r.botrading.api.model.IBalanceList;
import com.m2r.botrading.api.model.IChartDataList;
import com.m2r.botrading.api.model.IExchangeOrder;
import com.m2r.botrading.api.model.IMarketCoin;
import com.m2r.botrading.api.model.IOrderList;
import com.m2r.botrading.api.model.ITicker;
import com.m2r.botrading.api.model.ITickerList;
import com.m2r.botrading.api.model.MarketCoin;
import com.m2r.botrading.api.service.ExchangeService;
import com.m2r.botrading.api.service.ExchangeSession;
import com.m2r.botrading.api.service.IExchangeSession;
import com.m2r.botrading.api.util.JsonException;
import com.m2r.botrading.binance.enums.OrderSide;
import com.m2r.botrading.binance.enums.OrderType;
import com.m2r.botrading.binance.enums.TimeInForce;
import com.m2r.botrading.binance.market.BinanceKline;
import com.m2r.botrading.binance.model.Account;
import com.m2r.botrading.binance.model.AssetBalance;
import com.m2r.botrading.binance.model.BalanceBinance;
import com.m2r.botrading.binance.model.BinanceExchangeOrder;
import com.m2r.botrading.binance.model.ExchangeOrderList;
import com.m2r.botrading.binance.model.JsonSuccessBinance;
import com.m2r.botrading.binance.model.Order;
import com.m2r.botrading.binance.model.Ticker;
import com.m2r.botrading.binance.model.TickerList;
import com.m2r.botrading.binance.model.TickerStatistics;
import com.m2r.botrading.binance.model.chart.BalanceList;
import com.m2r.botrading.binance.model.chart.ChartDataList;

public class BinanceExchange extends ExchangeService {
	
	// As assinaturas da API java fornecedidas pela propria Binance.
	// https://github.com/binance-exchange/binance-java-api

	public static final String EXCHANGE_ID = "BINANCE";

	private static final String URL_BASE = "https://api.binance.com/api";
	private static final String V1_API = "/v1";
	private static final String V3_API = "/v3";

	private static final String ORDER = "order";
	private static final String COMMAND_OPEN_ORDERS = "openOrders";
	private static final String COMMAND_COMPLETE_BALANCES = "account";
	private static final String COMMAND_24_HR_PRICE_STATISTICS = "/ticker/24hr";
	private static final String COMMAND_CHART_DATA = "klines";
	private static final ZoneId EXCHANGE_ZONE_ID = ZoneId.of("America/New_York");

	private static final BigDecimal FEE = new BigDecimal("0.15");
	private static final BigDecimal IMMEDIATE_FEE = new BigDecimal("0.25");

	@Override
	public IMarketCoin getDefaultMarketCoin() {
		return getMarketCoin(Currency.BTC);
	}

	@Override
	public BigDecimal getFee() {
		return FEE;
	}

	@Override
	public BigDecimal getImmediateFee() {
		return IMMEDIATE_FEE;
	}

	@Override
	public IExchangeSession getSession(IMarketCoin marketCoin, boolean resetPublic, boolean resetPrivate) {

		IExchangeSession session = ExchangeSession.createSession(this, marketCoin);

		if (resetPublic) {
			session.resetPublicCache();
		}
		if (resetPrivate) {
			session.resetPrivateCache();
		}
		return session;
	}

	public void cancel(IApiAccess apiAccess, String orderNumber, String currencyPair) throws ExchangeException {

		try {
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("orderId", orderNumber);
			parameters.put("symbol", currencyPair);

			this.execTradingDeleteMethod(apiAccess, ORDER, parameters);
		} catch (Exception e) {
			throw new ExchangeException(e);
		}

	}

	@Override
	public String sell(IApiAccess apiAccess, String currencyPair, String price, String amount)
			throws ExchangeException {
		try {

			String data = commandOrder(apiAccess, currencyPair, price, amount, OrderSide.SELL);
			JsonSuccessBinance result = parseReturn(data, new TypeToken<JsonSuccessBinance>() {}.getType());

			return result.getOrderId();
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	@Override
	public String buy(IApiAccess apiAccess, String currencyPair, String price, String amount) throws ExchangeException {

		try {
			String data = commandOrder(apiAccess, currencyPair, price, amount, OrderSide.BUY);
			JsonSuccessBinance result = parseReturn(data, new TypeToken<JsonSuccessBinance>() {}.getType());

			return result.getOrderId();
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	private String commandOrder(IApiAccess apiAccess, String currencyPair, String price, String amount,
			OrderSide orderSide) throws Exception {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("symbol", currencyPair);
		parameters.put("side", orderSide);
		parameters.put("type", OrderType.LIMIT);
		parameters.put("timeInForce", TimeInForce.GTC);
		parameters.put("quantity", amount);
		parameters.put("price", price);

		return execTradingPostMethod(apiAccess, ORDER, parameters);
	}

	@Override
	protected IBalanceList getBanlances(IApiAccess apiAccess, IExchangeSession session) throws ExchangeException {
		try {
			Account account = commandCompleteBalances(apiAccess);
			
			List<AssetBalance> balances = account.getBalances().stream().filter(b -> !b.getFree().equals("0.00000000")).collect(Collectors.toList());
			Map<String, IBalance> map = new HashMap<>();

			for (AssetBalance balance : balances) {
				map.put(balance.getAsset(), new BalanceBinance(balance));
			}

			return BalanceList.of(map);

		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	public Account commandCompleteBalances(IApiAccess apiAccess) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		String data = this.execTradingGetMethod(apiAccess, V3_API, COMMAND_COMPLETE_BALANCES, parameters);
		return parseReturn(data, new TypeToken<Account>() {}.getType());
	}

	@Override
	protected IChartDataList getChartDatas(String currencyPair, DataChartPeriod period, LocalDateTime start,
			LocalDateTime end, IExchangeSession session) throws ExchangeException {
		try {
			ZonedDateTime endDate = ZonedDateTime.of(end, EXCHANGE_ZONE_ID);
			ZonedDateTime startDate = ZonedDateTime.of(start, EXCHANGE_ZONE_ID);

			return ChartDataList.of(this.commandChartDatas(currencyPair, period, startDate.toInstant().getEpochSecond(),
					endDate.toInstant().getEpochSecond()));
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	public List<BinanceKline> commandChartDatas(String currencyPair, DataChartPeriod period, Long dateStart, Long dateEnd) throws Exception {

		Map<String, String> params = new HashMap<>();
		params.put("symbol", currencyPair);
		params.put("interval", period.getSeconds().toString()); // FIXO até alterar o enum com os Parametros da Binance
		params.put("startTime", dateStart.toString());
		params.put("endTime", dateEnd.toString());

		try {
			
			String data = this.execPublicAPI(COMMAND_CHART_DATA, V1_API, params);
			List<Object[]> klines = parseReturn(data, new TypeToken<List<Object[]>>() {}.getType());
			List<BinanceKline> resutl = klines.stream().map(obj -> new BinanceKline(obj)).collect(Collectors.toList());
			
			return resutl;
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	@Override
	protected IOrderList getOrders(IApiAccess apiAccess, IExchangeSession session) throws ExchangeException {

		try {
			List<Order> opensOrders = commandOpenOrders(apiAccess);
			List<String> coins = opensOrders.stream().map(o -> o.getSymbol()).distinct().collect(Collectors.toList());

			Map<String, List<IExchangeOrder>> data = new HashMap<>();
			for (String coin : coins) {

				List<Order> ordersToCoin = opensOrders.stream().filter(o -> o.getSymbol().equals(coin)).collect(Collectors.toList());
				List<IExchangeOrder> exchangeOrders = new ArrayList<>();
				
				for (Order order : ordersToCoin) {
					exchangeOrders.add(new BinanceExchangeOrder(order));
				}
				data.put(coin, exchangeOrders);
			}
			return ExchangeOrderList.of(data);
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	public List<Order> commandOpenOrders(IApiAccess apiAccess) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		String data = execTradingGetMethod(apiAccess, V3_API, COMMAND_OPEN_ORDERS, parameters);

		return parseReturn(data, new TypeToken<List<Order>>() {}.getType());
	}

	@Override
	protected ITickerList getTikers(IExchangeSession session) throws ExchangeException {
		try {
			Map<String, ITicker> tikersMap = commandTicker();
			return TickerList.of(tikersMap);
		} catch (Exception e) {
			throw new ExchangeException(e);
		}
	}

	@Override
	protected Map<String, IMarketCoin> loadMarketCoins() {
		Map<String, IMarketCoin> map = new HashMap<>();
		try {
			Map<String, ITicker> tikersMap = commandTicker();
			tikersMap.forEach((k, v) -> {
				String marketCoinId = currencyPairToMarketCoinId(k);
				IMarketCoin marketCoin = map.get(marketCoinId);
				if (marketCoin == null) {
					marketCoin = MarketCoin.of(marketCoinId);
					map.put(marketCoinId, marketCoin);
				}
				String currencyId = currencyPairToCurrencyId(k);
				marketCoin.createAndAddCurrency(currencyId, currencyId);
			});
		} catch (Exception e) {
		}
		return map;
	}

	public static String currencyPairToMarketCoinId(String currencyPair) {

		if (currencyPair.contains(Currency.USDT)) {
			return Currency.USDT;
		}

		return StringUtils.right(currencyPair, 3);
	}

	public static String currencyPairToCurrencyId(String currencyPair) {

		if (currencyPair.contains(Currency.USDT)) {
			return StringUtils.remove(currencyPair, StringUtils.right(currencyPair, 4));
		}

		return StringUtils.remove(currencyPair, StringUtils.right(currencyPair, 3));
	}

	public Map<String, ITicker> commandTicker() throws Exception {

		List<TickerStatistics> tickerStatistics = commandTickerStatistics();
		Map<String, ITicker> data = new HashMap<>();

		for (TickerStatistics tickers : tickerStatistics) {
			data.put(tickers.getSymbol(), new Ticker(tickers));
		}

		return data;
	}

	private List<TickerStatistics> commandTickerStatistics() throws Exception {
		Map<String, String> params = new HashMap<>();
		String data = execPublicAPI(COMMAND_24_HR_PRICE_STATISTICS, V1_API, params);
		return parseReturn(data, new TypeToken<List<TickerStatistics>>() {}.getType());
	}

	private static <T> T parseReturn(String data, Type typeOf) throws JsonException {
		Gson gson = new Gson();
		if (data.startsWith("{\"error\"")) {
			Map<String, Object> result = gson.fromJson(data, new TypeToken<Map<String, Object>>() {
			}.getType());
			throw new JsonException(result.get("error").toString());
		}
		return (T) gson.fromJson(data, typeOf);
	}

	private String execPublicAPI(String command, String version, Map<String, String> parameters) throws Exception {

		StringBuilder builder = new StringBuilder(URL_BASE).append(version).append("/").append(command);

		List<String> keys = new ArrayList<String>(parameters.keySet());

		for (int i = 0; i < keys.size(); i++) {
			if (i == 0) {
				builder.append("?").append(keys.get(i)).append("=").append(parameters.get(keys.get(i)));
			} else {
				builder.append("&").append(keys.get(i)).append("=").append(parameters.get(keys.get(i)));
			}
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet(builder.toString());
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		HttpEntity responseEntity = httpResponse.getEntity();

		return EntityUtils.toString(responseEntity);

	}

	private String execTradingGetMethod(IApiAccess apiAccess, String version, String command,
			Map<String, Object> parameters) throws Exception {

		StringBuilder queryArgs = aplicarParametrosUrl(parameters);
		String signature = encode(apiAccess.getSecretKey(), queryArgs.toString());

		StringBuilder requestUrl = new StringBuilder(URL_BASE).append(version);
		requestUrl.append("/").append(command);
		requestUrl.append("?" + queryArgs).append("&signature=" + signature);

		HttpGet httpGet = new HttpGet(requestUrl.toString());
		httpGet.addHeader("X-MBX-APIKEY", apiAccess.getApiKey());

		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();

		return EntityUtils.toString(responseEntity);
	}

	private String execTradingPostMethod(IApiAccess apiAccess, String command, Map<String, Object> parameters)
			throws Exception {

		StringBuilder queryArgs = aplicarParametrosUrl(parameters);
		String signature = encode(apiAccess.getSecretKey(), queryArgs.toString());

		StringBuilder requestUrl = new StringBuilder(this.getUrlTradingAPI());
		requestUrl.append("/").append(command);
		requestUrl.append("?" + queryArgs).append("&signature=" + signature);

		HttpPost post = new HttpPost(requestUrl.toString());
		post.addHeader("X-MBX-APIKEY", apiAccess.getApiKey());

		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = httpClient.execute(post);
		HttpEntity responseEntity = response.getEntity();

		return EntityUtils.toString(responseEntity);
	}

	private String execTradingDeleteMethod(IApiAccess apiAccess, String command, Map<String, Object> parameters)
			throws Exception {

		StringBuilder queryArgs = aplicarParametrosUrl(parameters);
		String signature = encode(apiAccess.getSecretKey(), queryArgs.toString());

		StringBuilder requestUrl = new StringBuilder(this.getUrlTradingAPI());
		requestUrl.append("/").append(command);
		requestUrl.append("?" + queryArgs).append("&signature=" + signature);

		HttpDelete delete = new HttpDelete(requestUrl.toString());
		delete.addHeader("X-MBX-APIKEY", apiAccess.getApiKey());

		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = httpClient.execute(delete);
		HttpEntity responseEntity = response.getEntity();

		return EntityUtils.toString(responseEntity);
	}

	private StringBuilder aplicarParametrosUrl(Map<String, Object> parameters) {

		parameters.put("recvWindow", 10000000);
		parameters.put("timestamp", String.valueOf(new Date().getTime()));

		StringBuilder queryArgs = new StringBuilder();
		boolean firstElement = true;

		for (String param : parameters.keySet()) {
			if (firstElement) {
				queryArgs.append(param).append("=").append(parameters.get(param));
				firstElement = false;
			} else {
				queryArgs.append("&").append(param).append("=").append(parameters.get(param));
			}
		}
		return queryArgs;
	}

	private String encode(String key, String data) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
	}
	
	public String getUrlTradingAPI() {
		return URL_BASE + V3_API;
	}

}
