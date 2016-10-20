/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

/**
 * @author yuanren.syr
 * @version $Id: MinuteTradeData.java, v 0.1 2016/3/7 16:11 yuanren.syr Exp $
 */
public class MinuteTradeData {

    private String stockMinuteId;

    private String stockCode;

    private String stockName;

    private Date   sampleTime;

    private int    sampleInterval;

    private double openingPrice;

    private double closingPrice;

    private double highestPrice;

    private double lowestPrice;

    private double tradingVolume;

    private double tradingAmount;

    private double warrantFactor;

    private Date   gmtCreate;

    private Date   gmtModified;

    public String getStockMinuteId() {
        return stockMinuteId;
    }

    public void setStockMinuteId(String stockMinuteId) {
        this.stockMinuteId = stockMinuteId;
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

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public int getSampleInterval() {
        return sampleInterval;
    }

    public void setSampleInterval(int sampleInterval) {
        this.sampleInterval = sampleInterval;
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

    public double getWarrantFactor() {
        return warrantFactor;
    }

    public void setWarrantFactor(double warrantFactor) {
        this.warrantFactor = warrantFactor;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}
