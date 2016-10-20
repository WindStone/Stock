/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view.wiki;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: TradeRecordView.java, v 0.1 2016/2/25 20:09 yuanren.syr Exp $
 */
public class TradeRecordView {

    private String                      operator;

    private String                      stockCode;

    private String                      stockName;

    private List<TradeDetailRecordView> tradeDetailRecordViews;

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

    public List<TradeDetailRecordView> getTradeDetailRecordViews() {
        return tradeDetailRecordViews;
    }

    public void setTradeDetailRecordViews(List<TradeDetailRecordView> tradeDetailRecordViews) {
        this.tradeDetailRecordViews = tradeDetailRecordViews;
    }

    public void sortDetailView() {
        Collections.sort(tradeDetailRecordViews, new Comparator<TradeDetailRecordView>() {
            public int compare(TradeDetailRecordView o1, TradeDetailRecordView o2) {
                return (int) (o1.getGmtTrade().getTime() - o2.getGmtTrade().getTime());
            }
        });
    }

}
