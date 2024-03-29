package com.m2r.botrading.binance.enums;

/**
 * Time in force to indicate how long an order will remain active before it is executed or expires.
 *
 * GTC (Good-Til-Canceled) orders are effective until they are executed or canceled.
 * IOC (Immediate or Cancel) orders fills all or part of an order immediately and cancels the remaining part of the order.
 *
 * @see <a href="http://www.investopedia.com/terms/t/timeinforce.asp">http://www.investopedia.com/terms/t/timeinforce.asp</a>
 */
public enum TimeInForce {
  GTC,
  IOC
}
