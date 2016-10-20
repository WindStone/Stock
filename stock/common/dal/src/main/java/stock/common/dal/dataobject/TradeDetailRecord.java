/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

/**
 * @author yuanren.syr
 * @version $Id: TradeDetailRecord.java, v 0.1 2016/2/22 0:00 yuanren.syr Exp $
 */
public class TradeDetailRecord {

    private String tradeDetailRecordId;

    private String tradeRecordId;

    private String tradeFlow;

    private Date   tradeTime;

    private double price;

    private String conclusion;

    private Date   gmtCreate;

    private Date   gmtModified;

    public String getTradeDetailRecordId() {
        return tradeDetailRecordId;
    }

    public void setTradeDetailRecordId(String tradeDetailRecordId) {
        this.tradeDetailRecordId = tradeDetailRecordId;
    }

    public String getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(String tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
    }

    public String getTradeFlow() {
        return tradeFlow;
    }

    public void setTradeFlow(String tradeFlow) {
        this.tradeFlow = tradeFlow;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
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
