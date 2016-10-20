/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

/**
 * @author yuanren.syr
 * @version $Id: TradeRecord.java, v 0.1 2016/2/25 15:03 yuanren.syr Exp $
 */
public class TradeRecord {

    private String tradeRecordId;

    private String operator;

    private String stockCode;

    private String stockName;

    private Date gmtCreate;

    private Date gmtModified;

    public String getTradeRecordId() {
        return tradeRecordId;
    }

    public void setTradeRecordId(String tradeRecordId) {
        this.tradeRecordId = tradeRecordId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
