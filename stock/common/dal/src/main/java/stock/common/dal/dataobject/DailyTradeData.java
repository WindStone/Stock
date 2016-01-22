/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author yuanren.syr
 * @version $Id: DailyTradeData.java, v 0.1 2015/11/17 23:50 yuanren.syr Exp $
 */
public class DailyTradeData implements Serializable {

    private static final long serialVersionUID = 7990507695124676240L;

    private String            stockDailyId;

    private Date              currentDate;

    private String            date;

    private String            time;

    private String            stockCode;

    private String            stockName;

    private double            openingPrice;

    private double            closingPrice;

    private double            highestPrice;

    private double            lowestPrice;

    /** 成交量 */
    private double            tradingVolume;

    /** 成交金额 */
    private double            tradingAmount;

    /** 换手率 */
    private double            turnoverRate;

    public String getStockDailyId() {
        return stockDailyId;
    }

    public void setStockDailyId(String stockDailyId) {
        this.stockDailyId = stockDailyId;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
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

    public double getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(double tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public double getTradingAmount() {
        return tradingAmount;
    }

    public void setTradingAmount(double tradingAmount) {
        this.tradingAmount = tradingAmount;
    }

    public double getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(double turnoverRate) {
        this.turnoverRate = turnoverRate;
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

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
