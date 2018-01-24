package com.m2r.botrading.binance.model;

import java.math.BigDecimal;

import com.m2r.botrading.api.model.IExchangeOrder;

public class BinanceExchangeOrder implements IExchangeOrder {

	private String orderId;

	private String side;

	private BigDecimal executedQty;

	private BigDecimal origQty;
	
	private BigDecimal total;
	
	public BinanceExchangeOrder(Order order ) {
		this.orderId = order.getOrderId().toString();
		this.executedQty = new BigDecimal(order.getExecutedQty());
		this.origQty = new BigDecimal(order.getOrigQty());
		this.side = order.getSide().name();
	}

	@Override
	public String getOrderNumber() {
		return orderId;
	}

	@Override
	public String getType() {
		return this.side;
	}

	@Override
	public BigDecimal getRate() {
		return this.executedQty;
	}

	@Override
	public BigDecimal getAmount() {
		return this.origQty;
	}

	@Override
	public BigDecimal getTotal() {
		return this.total;
	}

}
