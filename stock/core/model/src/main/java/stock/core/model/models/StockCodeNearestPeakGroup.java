/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.sal.model.CurrentTradeData;

/**
 * @author yuanren.syr
 * @version $Id: StockCodeNearestPeakGroup.java, v 0.1 2015/12/10 23:24 yuanren.syr Exp $
 */
public class StockCodeNearestPeakGroup extends StockCodeGroup {

    private double           highestPrice;

    private double           currentPrice;

    private double           nextPrice;

    private CurrentTradeData curTradeDate;

    private DailyTradeData   curDailyTradeDate;

    private DailyTradeData   prevDailyTradeData;

    private Date             highestDate;

    public StockCodeNearestPeakGroup(String stockCode, String stockName, List<Date> dates) {
        super(stockCode, stockName, dates);
    }

    public StockCodeNearestPeakGroup(String stockCode, String stockName, double highestPrice,
                                     Date highestDate, double currentPrice) {
        super(stockCode, stockName, null);
        this.highestPrice = highestPrice;
        this.currentPrice = currentPrice;
        this.highestDate = highestDate;
    }

    public StockCodeNearestPeakGroup(String stockCode, String stockName, List<Date> dates,
                                     double highestPrice, Date highestDate, double currentPrice) {
        this(stockCode, stockName, highestPrice, highestDate, currentPrice);
        this.dates = dates;
    }

    public StockCodeNearestPeakGroup(String stockCode, String stockName, List<Date> dates,
                                     double highestPrice, Date highestDate, double currentPrice,
                                     double nextPrice) {
        this(stockCode, stockName, highestPrice, highestDate, currentPrice);
        this.dates = dates;
        this.nextPrice = nextPrice;
    }

    public StockCodeNearestPeakGroup(String stockCode, String stockName, double highestPrice,
                                     Date highestDate, double currentPrice, double nextPrice) {
        this(stockCode, stockName, highestPrice, highestDate, currentPrice);
        this.nextPrice = nextPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getHighestDate() {
        return highestDate;
    }

    public void setHighestDate(Date highestDate) {
        this.highestDate = highestDate;
    }

    public double getNextPrice() {
        return nextPrice;
    }

    public void setNextPrice(double nextPrice) {
        this.nextPrice = nextPrice;
    }

    public CurrentTradeData getCurTradeDate() {
        return curTradeDate;
    }

    public void setCurTradeDate(CurrentTradeData curTradeDate) {
        this.curTradeDate = curTradeDate;
    }

    public DailyTradeData getCurDailyTradeDate() {
        return curDailyTradeDate;
    }

    public void setCurDailyTradeDate(DailyTradeData curDailyTradeDate) {
        this.curDailyTradeDate = curDailyTradeDate;
    }

    public DailyTradeData getPrevDailyTradeData() {
        return prevDailyTradeData;
    }

    public void setPrevDailyTradeData(DailyTradeData prevDailyTradeData) {
        this.prevDailyTradeData = prevDailyTradeData;
    }
}
