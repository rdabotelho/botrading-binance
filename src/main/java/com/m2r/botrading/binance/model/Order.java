package com.m2r.botrading.binance.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.m2r.botrading.binance.enums.OrderSide;
import com.m2r.botrading.binance.enums.OrderStatus;
import com.m2r.botrading.binance.enums.TimeInForce;

/**
 * Trade order information.
 */
public class Order {

	/**
	 * Symbol that the order was put on.
	 */
	private String symbol;

	/**
	 * Order id.
	 */
	private Long orderId;

	/**
	 * Client order id.
	 */
	private String clientOrderId;

	/**
	 * Price.
	 */
	private String price;

	/**
	 * Original quantity.
	 */
	private String origQty;

	/**
	 * Original quantity.
	 */
	private String executedQty;

	/**
	 * Order status.
	 */
	private OrderStatus status;

	/**
	 * Time in force to indicate how long will the order remain active.
	 */
	private TimeInForce timeInForce;

	/**
	 * Type of order.
	 */
	private String type;

	/**
	 * Buy/Sell order side.
	 */
	private OrderSide side;

	/**
	 * Used with stop orders.
	 */
	private String stopPrice;

	/**
	 * Used with iceberg orders.
	 */
	private String icebergQty;

	/**
	 * Order timestamp.
	 */
	private long time;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getClientOrderId() {
		return clientOrderId;
	}

	public void setClientOrderId(String clientOrderId) {
		this.clientOrderId = clientOrderId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getOrigQty() {
		return origQty;
	}

	public void setOrigQty(String origQty) {
		this.origQty = origQty;
	}

	public String getExecutedQty() {
		return executedQty;
	}

	public void setExecutedQty(String executedQty) {
		this.executedQty = executedQty;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public TimeInForce getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(TimeInForce timeInForce) {
		this.timeInForce = timeInForce;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public String getStopPrice() {
		return stopPrice;
	}

	public void setStopPrice(String stopPrice) {
		this.stopPrice = stopPrice;
	}

	public String getIcebergQty() {
		return icebergQty;
	}

	public void setIcebergQty(String icebergQty) {
		this.icebergQty = icebergQty;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("symbol", symbol)
				.append("orderId", orderId).append("clientOrderId", clientOrderId).append("price", price)
				.append("origQty", origQty).append("executedQty", executedQty).append("status", status)
				.append("timeInForce", timeInForce).append("type", type).append("side", side)
				.append("stopPrice", stopPrice).append("icebergQty", icebergQty).append("time", time).toString();
	}

	public String getOrderNumber() {
		return this.orderId.toString();
	}

	public BigDecimal getRate() {
		return null;
	}

	public BigDecimal getAmount() {
		return new BigDecimal(origQty);
	}

	public BigDecimal getTotal() {
		return new BigDecimal(executedQty);
	}
}
