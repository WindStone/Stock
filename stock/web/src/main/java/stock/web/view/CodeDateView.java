/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author yuanren.syr
 * @version $Id: CodeDateView.java, v 0.1 2016/2/22 21:35 yuanren.syr Exp $
 */
public class CodeDateView {

    private String stockCode;

    private Date   tradeDate;

    public CodeDateView(String stockCode, Date tradeDate) {
        this.stockCode = stockCode;
        this.tradeDate = tradeDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CodeDateView) {
            return StringUtils.equals(stockCode, ((CodeDateView) obj).stockCode)
                   && DateUtils.isSameDay(tradeDate, ((CodeDateView) obj).tradeDate);
        }
        return false;
    }

    public int hashCode() {
        return stockCode.hashCode() + tradeDate.hashCode();
    }
}
