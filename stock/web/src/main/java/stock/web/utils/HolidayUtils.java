/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;

import stock.common.dal.datainterface.WorkHolidayDAO;
import stock.common.dal.dataobject.WorkHolidayConfig;
import stock.common.util.DateUtil;

import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: HolidayUtils.java, v 0.1 2016/1/11 1:24 yuanren.syr Exp $
 */
public class HolidayUtils implements InitializingBean {

    private static Map<String, Boolean> workHolidayConfigs;

    private static WorkHolidayDAO       workHolidayDAO;

    public void afterPropertiesSet() throws Exception {
        loadWorkHolidayConfigs();
    }

    public static boolean isAvailable(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Boolean isHoliday = workHolidayConfigs.get(DateUtil.simpleFormat(date));
        if (isHoliday == null) {
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                return false;
            } else {
                return true;
            }
        } else {
            return !isHoliday;
        }
    }

    public static Date getPrevWorkDate(Date date) {
        Date tmpDate = DateUtils.addDays(date, -1);
        while (!isAvailable(tmpDate) && tmpDate.getYear() + 1900 >= 2015) {
            tmpDate = DateUtils.addDays(tmpDate, -1);
        }
        return tmpDate;
    }

    public static Date getNextWorkDate(Date date) {
        Date tmpDate = DateUtils.addDays(date, 1);
        Date now = new Date();
        while (!isAvailable(tmpDate) && DateUtil.getDiffInDays(now, tmpDate) > 0) {
            tmpDate = DateUtils.addDays(tmpDate, 1);
        }
        return tmpDate;
    }

    public static void loadWorkHolidayConfigs() {
        workHolidayConfigs = Maps.newHashMap();
        for (WorkHolidayConfig workHolidayConfig : workHolidayDAO.queryWorkHolidays()) {
            workHolidayConfigs.put(DateUtil.simpleFormat(workHolidayConfig.getDay()),
                workHolidayConfig.isHoliday());
        }
    }

    public static Date getLastAvailableDate() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 15 || !isAvailable(calendar.getTime())) {
            return getPrevWorkDate(calendar.getTime());
        } else {
            return calendar.getTime();
        }
    }

    public void setWorkHolidayDAO(WorkHolidayDAO workHolidayDAO) {
        this.workHolidayDAO = workHolidayDAO;
    }
}
