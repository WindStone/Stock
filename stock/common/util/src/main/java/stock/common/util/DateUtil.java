/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author yuanren.syr
 * @version $Id: DateUtil.java, v 0.1 2015/11/18 0:37 yuanren.syr Exp $
 */
public class DateUtil {

    public static String    SLASH_DATE_FORMAT   = "yyyy/MM/dd";

    public static String    DATE_FORMAT         = "yyyy/MM/dd hh:mm";

    public static String    SIMPLE_FORMAT       = "yyyy-MM-dd";

    public static final int HOUR_PER_DAY        = 24;

    public static final int MINUTE_PER_HOUR     = 60;

    public static final int SECOND_PER_MINUTE   = 60;

    public static final int MILLISEC_PER_SECOND = 1000;

    public static String slashDateFormat(Date date) {
        return new SimpleDateFormat(SLASH_DATE_FORMAT).format(date);
    }

    public static String simpleFormat(Date date) {
        return new SimpleDateFormat(SIMPLE_FORMAT).format(date);
    }

    public static Date parseSlashDate(String date) {
        try {
            return new SimpleDateFormat(SLASH_DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseSimpleDate(String date) {
        try {
            return new SimpleDateFormat(SIMPLE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getDiffInDays(Date a, Date b) {
        return (int) ((a.getTime() - b.getTime()) / HOUR_PER_DAY / MINUTE_PER_HOUR
                      / SECOND_PER_MINUTE / MILLISEC_PER_SECOND);
    }
}
