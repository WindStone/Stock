/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

/**
 *
 * @author yuanren.syr
 * @version $Id: ${FILE}.java, v 0.1 2015/11/23 1:26 yuanren.syr Exp $
 */
public class PeriodResult {
    private IntervalResult max2MaxInterval;

    private IntervalResult max2MinInterval;

    private IntervalResult min2MaxInterval;

    private IntervalResult min2MinInterval;

    public IntervalResult getMax2MaxInterval() {
        return max2MaxInterval;
    }

    public void setMax2MaxInterval(IntervalResult max2MaxInterval) {
        this.max2MaxInterval = max2MaxInterval;
    }

    public IntervalResult getMax2MinInterval() {
        return max2MinInterval;
    }

    public void setMax2MinInterval(IntervalResult max2MinInterval) {
        this.max2MinInterval = max2MinInterval;
    }

    public IntervalResult getMin2MaxInterval() {
        return min2MaxInterval;
    }

    public void setMin2MaxInterval(IntervalResult min2MaxInterval) {
        this.min2MaxInterval = min2MaxInterval;
    }

    public IntervalResult getMin2MinInterval() {
        return min2MinInterval;
    }

    public void setMin2MinInterval(IntervalResult min2MinInterval) {
        this.min2MinInterval = min2MinInterval;
    }
}
