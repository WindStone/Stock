/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author yuanren.syr
 * @version $Id: DateUtil.java, v 0.1 2015/11/18 0:37 yuanren.syr Exp $
 */
public class DateUtil {

    public static String        SLASH_DATE_FORMAT   = "yyyy/MM/dd";

    public static String        DATE_FORMAT         = "yyyy/MM/dd hh:mm";

    public static String        SIMPLE_FORMAT       = "yyyy-MM-dd";

    public static String        SIMPLE_TIME_FORMAT  = "hh:mm:ss";

    public static String        HOUR_MINUTE_FORMAT  = "hh:mm";

    public static String        JSON_FORMAT         = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final int     HOUR_PER_DAY        = 24;

    public static final int     MINUTE_PER_HOUR     = 60;

    public static final int     SECOND_PER_MINUTE   = 60;

    public static final int     MILLISEC_PER_SECOND = 1000;

    public static final String  SYMBOL_REGEX        = "[+-]";
    public static final String  TIME_REGEX          = "([1-9][0-9]*[yMdhms])+\\s*";
    public static final String  REGEX               = "^now\\s*" + SYMBOL_REGEX + "\\s*"
                                                      + TIME_REGEX;

    public static final Pattern SYMBOL_PATTERN      = Pattern.compile(SYMBOL_REGEX);
    public static final Pattern TIME_PATTERN        = Pattern.compile(TIME_REGEX);
    public static final Pattern REGEX_PATTERN       = Pattern.compile(REGEX);

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

    public static String simpleTimeFormat(Date date) {
        return new SimpleDateFormat(SIMPLE_TIME_FORMAT).format(date);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseNow(String date) {
        date = date.trim();
        if (StringUtils.equals(date, "now")) {
            return new Date();
        }

        if (REGEX_PATTERN.matcher(date).matches()) {
            String[] parts = SYMBOL_PATTERN.split(date);
            String time = parts[1].trim();
            char unit = time.charAt(time.length() - 1);
            int value = Integer.parseInt(time.substring(0, time.length() - 1));
            Date ret = new Date();
            if (unit == 'M') {
                if (StringUtils.contains(date, '-')) {
                    value = -value;
                }
                ret = DateUtils.addMonths(ret, value);
            }
            return ret;
        }
        return null;
    }

    public static Date parseSimpleDate(String date) {
        Date d = parseNow(date);
        if (d != null) {
            return d;
        }
        try {
            return new SimpleDateFormat(SIMPLE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseJsonDate(String date) {
        try {
            return new SimpleDateFormat(JSON_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseHourMinute(String date) {
        try {
            return new SimpleDateFormat(HOUR_MINUTE_FORMAT).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateFormat(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static int getDiffInDays(Date a, Date b) {
        return (int) ((a.getTime() - b.getTime()) / HOUR_PER_DAY / MINUTE_PER_HOUR
                      / SECOND_PER_MINUTE / MILLISEC_PER_SECOND);
    }
}
