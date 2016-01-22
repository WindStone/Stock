/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.builder;

import java.util.List;

import stock.core.model.models.IntervalResult;

import com.google.common.collect.Lists;

/**
 *
 * @author yuanren.syr
 * @version $Id: IntervalResultBuilder.java, v 0.1 2015/11/24 0:42 yuanren.syr Exp $
 */
public class IntervalResultBuilder {

    private List<Integer> intervals = Lists.newArrayList();

    private List<Integer> periods   = Lists.newArrayList();

    public IntervalResultBuilder values(Integer interval, Integer period) {
        intervals.add(interval);
        periods.add(period);
        return this;
    }

    public IntervalResult build() {
        IntervalResult intervalResult = new IntervalResult();
        intervalResult.setIntervals(intervals);
        intervalResult.setPeriods(periods);
        double sum = 0;
        for (int interval : intervals) {
            sum += interval;
        }
        double avg = sum / intervals.size();
        intervalResult.setAvg(avg);
        sum = 0;
        for (int interval : intervals) {
            sum += (interval - avg) * (interval - avg);
        }
        intervalResult.setVariance(sum / intervals.size());
        return intervalResult;
    }
}
