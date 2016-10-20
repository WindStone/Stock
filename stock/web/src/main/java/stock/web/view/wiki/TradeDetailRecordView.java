/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view.wiki;

import java.util.Date;

/**
 * @author yuanren.syr
 * @version $Id: TradeDetailRecordView.java, v 0.1 2016/2/25 20:12 yuanren.syr Exp $
 */
public class TradeDetailRecordView {

    private String tradeFlow;

    private String tradeDate;

    private String tradeTime;

    private String price;

    private String conclusion;

    private Date   gmtTrade;

    public String getTradeFlow() {
        return tradeFlow;
    }

    public void setTradeFlow(String tradeFlow) {
        this.tradeFlow = tradeFlow;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Date getGmtTrade() {
        return gmtTrade;
    }

    public void setGmtTrade(Date gmtTrade) {
        this.gmtTrade = gmtTrade;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
