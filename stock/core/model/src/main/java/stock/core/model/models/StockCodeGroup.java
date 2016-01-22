/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import java.util.Date;
import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: StockCodeGroup.java, v 0.1 2015/12/10 23:19 yuanren.syr Exp $
 */
public class StockCodeGroup {
    protected String     stockCode;

    protected String     stockName;

    protected List<Date> dates;

    public StockCodeGroup(String stockCode, String stockName, List<Date> dates) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.dates = dates;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }
}
