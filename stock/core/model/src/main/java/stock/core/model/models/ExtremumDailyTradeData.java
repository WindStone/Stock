/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;

import com.google.common.collect.Lists;

/**
 *
 * @author yuanren.syr
 * @version $Id: ExtremumDailyTradeData.java, v 0.1 2015/11/22 23:56 yuanren.syr Exp $
 */
public class ExtremumDailyTradeData {
    private List<DailyTradeData> maximum = Lists.newArrayList();

    private List<DailyTradeData> minimum = Lists.newArrayList();

    public List<DailyTradeData> getMaximum() {
        return maximum;
    }

    public void setMaximum(List<DailyTradeData> maximum) {
        this.maximum = maximum;
    }

    public void addMaximum(DailyTradeData dtd) {
        maximum.add(dtd);
    }

    public List<DailyTradeData> getMinimum() {
        return minimum;
    }

    public void setMinimum(List<DailyTradeData> minimum) {
        this.minimum = minimum;
    }

    public void addMinimum(DailyTradeData dtd) {
        minimum.add(dtd);
    }
}
