/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * @author yuanren.syr
 * @version $Id: HolidayUtils.java, v 0.1 2016/1/11 1:24 yuanren.syr Exp $
 */
public class HolidayUtils {

    public static boolean isAvailable(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return false;
        }
        return true;
    }

    public static Date getPrevWorkDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY) {
            return DateUtils.addDays(date, -3);
        } else {
            return DateUtils.addDays(date, -1);
        }
    }

    public static Date getNextWorkDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.FRIDAY) {
            return DateUtils.addDays(date, 3);
        } else {
            return DateUtils.addDays(date, 1);
        }
    }
}
