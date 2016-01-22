/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;

import com.google.common.collect.Lists;

/**
 *
 * @author yuanren.syr
 * @version $Id: FundPowerEvaluateProcessor.java, v 0.1 2015/11/30 23:25 yuanren.syr Exp $
 */
public class FundPowerEvaluateProcessor {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    private static Date                startDate           = DateUtil.parseSlashDate("2015/10/1");

    private static Date                endDate             = DateUtil.parseSlashDate("2015/11/1");

    private static Calendar            calendar            = Calendar.getInstance();

    public static void main(String[] args) {
        File file = new File(PathUtil.getDataPath() + "/5min/" + "/201510");
        File[] files = file.listFiles();
        for (File f : files) {
            List<DailyTradeData> dtds = historyDataAccessor.get5MinTrade(f);
            List<Date> dates = Lists.newArrayList();
            double closePrice = 0;
            for (DailyTradeData dtd : dtds) {
                if (StringUtils.equals(dtd.getTime(), "15:00")) {
                    double curClosePrice = dtd.getClosingPrice();
                    if (closePrice != 0 && ((curClosePrice - closePrice) / closePrice) > 0.099) {
                        dates.add(dtd.getCurrentDate());
                    }
                    closePrice = curClosePrice;
                }
            }
            for (int i = 0; i < dates.size() - 1; ++i) {
                Date date = dates.get(i);
                calendar.setTime(dates.get(i));
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    date = DateUtils.addDays(date, 3);
                } else {
                    date = DateUtils.addDays(date, 1);
                }
                if (DateUtils.isSameDay(date, dates.get(i + 1))) {
                    System.out.println(f.getName() + " " + date);
                }
            }
        }
    }
}
