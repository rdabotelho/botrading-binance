package com.m2r.botrading.binance.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * An asset balance in an Account.
 *
 * @see Account
 */
public class AssetBalance {

  /**
   * Asset symbol.
   */
  private String asset;

  /**
   * Available balance.
   */
  private String free;

  /**
   * Locked by open orders.
   */
  private String locked;

  public String getAsset() {
    return asset;
  }

  public void setAsset(String asset) {
    this.asset = asset;
  }

  public String getFree() {
    return free;
  }

  public void setFree(String free) {
    this.free = free;
  }

  public String getLocked() {
    return locked;
  }

  public void setLocked(String locked) {
    this.locked = locked;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("asset", asset)
        .append("free", free)
        .append("locked", locked)
        .toString();
  }
}
