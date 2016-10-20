/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

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

    public static double parsePercent(String value) {
        try {
            return (Double) new DecimalFormat(PERCENT_FORMAT).parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDecimal(double value) {
        return new DecimalFormat(DECIMAL_FORMAT).format(value);
    }

    public static double average(List<Double> values) {
        double result = 0;
        int len = 0;
        for (Double value : values) {
            if (value != null) {
                result += value;
                len++;
            }
        }
        result /= values.size();
        return result;
    }
}
