/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

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

    /** 除权因子 */
    private double            warrantFactor;

    public DailyTradeData() {

    }

    public DailyTradeData(DailyTradeData dtd) {
        BeanUtils.copyProperties(dtd, this);
    }

    public double getHighestPrice(DailyTradeData stdTradeData) {
        if (stdTradeData == null) {
            return closingPrice;
        } else {
            return highestPrice / stdTradeData.getWarrantFactor() * warrantFactor;
        }
    }

    public double getOpeningPrice(DailyTradeData stdTradeData) {
        if (stdTradeData == null) {
            return openingPrice;
        } else {
            return openingPrice / stdTradeData.getWarrantFactor() * warrantFactor;
        }
    }

    public double getClosingPrice(DailyTradeData stdTradeData) {
        if (stdTradeData == null || stdTradeData.getWarrantFactor() == 0) {
            return closingPrice;
        } else {
            double tempClosingPrice = 0;
            if (currentDate.getYear() + 1900 < 2015) {
                tempClosingPrice = closingPrice / warrantFactor;
            } else {
                tempClosingPrice = closingPrice;
            }
            return tempClosingPrice / stdTradeData.getWarrantFactor() * warrantFactor;
        }
    }

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

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getLowestPrice(DailyTradeData stdTradeData) {
        if (stdTradeData == null || stdTradeData.getWarrantFactor() == 0) {
            return lowestPrice;
        } else {
            double tempClosingPrice = 0;
            if (currentDate.getYear() + 1900 < 2015) {
                tempClosingPrice = lowestPrice / warrantFactor;
            } else {
                tempClosingPrice = lowestPrice;
            }
            return tempClosingPrice / stdTradeData.getWarrantFactor() * warrantFactor;
        }
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

    public double getWarrantFactor() {
        return warrantFactor;
    }

    public void setWarrantFactor(double warrantFactor) {
        this.warrantFactor = warrantFactor;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
