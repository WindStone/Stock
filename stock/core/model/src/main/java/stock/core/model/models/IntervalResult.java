/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import java.util.List;

import com.google.common.collect.Lists;

/**
 *
 * @author yuanren.syr
 * @version $Id: IntervalResult.java, v 0.1 2015/11/24 0:36 yuanren.syr Exp $
 */
public class IntervalResult {

    private List<Integer> intervals = Lists.newArrayList();

    private List<Integer> periods   = Lists.newArrayList();

    private double        avg;

    private double        variance;

    public List<Integer> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<Integer> intervals) {
        this.intervals = intervals;
    }

    public List<Integer> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Integer> periods) {
        this.periods = periods;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }
}
