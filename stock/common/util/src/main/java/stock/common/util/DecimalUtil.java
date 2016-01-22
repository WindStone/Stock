/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.util;

import java.text.DecimalFormat;

/**
 * @author yuanren.syr
 * @version $Id: DecimalUtil.java, v 0.1 2015/12/18 0:09 yuanren.syr Exp $
 */
public class DecimalUtil {

    private static final String PERCENT_FORMAT = "#0.00%";

    private static final String DECIMAL_FORMAT = "#0.00";

    public static String formatPercent(double value) {
        return new DecimalFormat(PERCENT_FORMAT).format(value);
    }

    public static String formatDecimal(double value) {
        return new DecimalFormat(DECIMAL_FORMAT).format(value);
    }
}
