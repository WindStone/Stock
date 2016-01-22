/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.sal.model;

/**
 * @author yuanren.syr
 * @version $Id: CurrentTradeData.java, v 0.1 2015/12/8 0:25 yuanren.syr Exp $
 */
public class CurrentTradeData {
    private String stockName;

    private double openPrice;

    private double lastClosePrice;

    private double currentPrice;

    private double highestPrice;

    private double lowestPrice;

    private long   tradingVolumn;

    private double tradingAmount;

    private String date;

    private String time;

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getLastClosePrice() {
        return lastClosePrice;
    }

    public void setLastClosePrice(double lastClosePrice) {
        this.lastClosePrice = lastClosePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public long getTradingVolumn() {
        return tradingVolumn;
    }

    public void setTradingVolumn(long tradingVolumn) {
        this.tradingVolumn = tradingVolumn;
    }

    public double getTradingAmount() {
        return tradingAmount;
    }

    public void setTradingAmount(double tradingAmount) {
        this.tradingAmount = tradingAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
