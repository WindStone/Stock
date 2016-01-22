/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.util.DateUtil;
import stock.core.model.builder.IntervalResultBuilder;
import stock.core.model.enums.ShenWanPlateNameEnum;
import stock.core.model.models.ExtremumDailyTradeData;
import stock.core.model.models.IntervalResult;
import stock.core.model.models.PeriodResult;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 *
 * @author yuanren.syr
 * @version $Id: ShenWanPlatePeriodProcessor.java, v 0.1 2015/11/18 1:00 yuanren.syr Exp $
 */
public class ShenWanPlatePeriodProcessor {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    private static final Date          startDate           = DateUtil.parseSlashDate("2015/01/01");

    private static final Date          endDate             = DateUtil.parseSlashDate("2015/11/01");

    private static final int           SMOOTH_WINDOW_SIZE  = 5;

    private static final int           TREND_WINDOW_SIZE   = 3;

    public static void main(String[] args) {
        List<DailyTradeData> shDtds = Lists.newArrayList();
        List<DailyTradeData> szDtds = Lists.newArrayList();
        List<DailyTradeData> blackMetalDtds = Lists.newArrayList();
        for (Date date = startDate; !DateUtils.isSameDay(date, endDate); date = DateUtils.addDays(
            date, 1)) {
            DailyTradeData shDtd = historyDataAccessor.getDailySHExp(date);
            if (shDtd != null) {
                shDtds.add(shDtd);
            }
            DailyTradeData szDtd = historyDataAccessor.getDailySZExp(date);
            if (szDtd != null) {
                szDtds.add(szDtd);
            }
            DailyTradeData blackMetalDtd = historyDataAccessor.getDailyPlateHistoryByName(
                ShenWanPlateNameEnum.食品饮料.name(), date);
            if (blackMetalDtd != null) {
                blackMetalDtds.add(blackMetalDtd);
            }
        }
        ExtremumDailyTradeData shExt = calcExtremum(shDtds);
        ExtremumDailyTradeData szExt = calcExtremum(szDtds);
        ExtremumDailyTradeData blackMetalExt = calcExtremum(blackMetalDtds);

        PeriodResult periodResultSH = calcPeriod(blackMetalExt, shExt);
        PeriodResult periodResultSZ = calcPeriod(blackMetalExt, szExt);
        List<PeriodResult> results = Lists.newArrayList(periodResultSH, periodResultSZ);

        for (PeriodResult pr : results) {
            sysPrint("Max2Max: ", pr.getMax2MaxInterval());
            sysPrint("Max2Min: ", pr.getMax2MinInterval());
            sysPrint("Min2Max: ", pr.getMin2MaxInterval());
            sysPrint("Min2Min: ", pr.getMin2MinInterval());
        }

    }

    private static void sysPrint(String name, IntervalResult intervalResult) {
        System.out.println("\n" + name + ": " + intervalResult.getAvg() + " "
                           + intervalResult.getVariance());
        for (int i = 0; i < intervalResult.getIntervals().size(); ++i) {
            System.out.print(intervalResult.getIntervals().get(i) + " ");
        }
    }

    private static ExtremumDailyTradeData calcExtremum(List<DailyTradeData> dtds) {
        ExtremumDailyTradeData ret = new ExtremumDailyTradeData();
        List<Double> smoothClosing = Lists.newArrayList();
        for (int i = SMOOTH_WINDOW_SIZE - 1; i < dtds.size(); ++i) {
            double sum = 0;
            for (int j = 0; j < SMOOTH_WINDOW_SIZE; ++j) {
                sum += dtds.get(i - j).getClosingPrice();
            }
            smoothClosing.add(sum / SMOOTH_WINDOW_SIZE);
        }
        for (int i = TREND_WINDOW_SIZE; i < smoothClosing.size() - TREND_WINDOW_SIZE; ++i) {
            boolean minimum = true;
            boolean maximum = true;
            double value = smoothClosing.get(i);
            for (int j = 1; (maximum || minimum) && j <= TREND_WINDOW_SIZE; ++j) {
                if (smoothClosing.get(i - j) > value) {
                    maximum = false;
                } else if (smoothClosing.get(i - j) < value) {
                    minimum = false;
                }
                if (smoothClosing.get(i + j) > value) {
                    maximum = false;
                } else if (smoothClosing.get(i + j) < value) {
                    minimum = false;
                }
            }
            if (minimum) {
                ret.addMaximum(dtds.get(i));
            }
            if (maximum) {
                ret.addMinimum(dtds.get(i));
            }
        }
        return ret;
    }

    private static PeriodResult calcPeriod(ExtremumDailyTradeData feature,
                                           ExtremumDailyTradeData base) {
        PeriodResult ret = new PeriodResult();
        List<Date> maxDates = Lists.transform(feature.getMaximum(),
            new Function<DailyTradeData, Date>() {
                public Date apply(DailyTradeData dailyTradeData) {
                    return dailyTradeData.getCurrentDate();
                }
            });
        List<Date> minDates = Lists.transform(feature.getMinimum(),
            new Function<DailyTradeData, Date>() {
                public Date apply(DailyTradeData dailyTradeData) {
                    return dailyTradeData.getCurrentDate();
                }
            });
        IntervalResultBuilder max2MaxBuilder = new IntervalResultBuilder();
        IntervalResultBuilder max2MinBuilder = new IntervalResultBuilder();
        IntervalResultBuilder min2MaxBuilder = new IntervalResultBuilder();
        IntervalResultBuilder min2MinBuilder = new IntervalResultBuilder();
        for (int i = 0; i < base.getMaximum().size(); ++i) {
            Date baseDate = base.getMaximum().get(i).getCurrentDate();
            int period = (i + 1 == base.getMaximum().size()) ? -1 : DateUtil.getDiffInDays(base
                .getMaximum().get(i + 1).getCurrentDate(), baseDate);
            int max2MaxInterval = getInterval(baseDate, maxDates);
            max2MaxBuilder.values(max2MaxInterval, period);
            int max2MinInterval = getInterval(baseDate, minDates);
            max2MinBuilder.values(max2MinInterval, period);
        }
        for (int i = 0; i < base.getMinimum().size(); ++i) {
            Date baseDate = base.getMinimum().get(i).getCurrentDate();
            int period = (i + 1 == base.getMinimum().size()) ? -1 : DateUtil.getDiffInDays(base
                .getMaximum().get(i + 1).getCurrentDate(), baseDate);
            int min2MaxInterval = getInterval(baseDate, maxDates);
            min2MaxBuilder.values(min2MaxInterval, period);
            int min2MinInterval = getInterval(baseDate, minDates);
            min2MinBuilder.values(min2MinInterval, period);
        }
        ret.setMax2MaxInterval(max2MaxBuilder.build());
        ret.setMax2MinInterval(max2MinBuilder.build());
        ret.setMin2MaxInterval(min2MaxBuilder.build());
        ret.setMin2MinInterval(min2MinBuilder.build());
        return ret;
    }

    private static int getInterval(Date date, List<Date> featureDates) {
        int ret = Integer.MAX_VALUE;
        for (int i = 0; i < featureDates.size(); ++i) {
            Date featureDate = featureDates.get(i);
            int diff = DateUtil.getDiffInDays(date, featureDate);
            if (Math.abs(ret) > Math.abs(diff)) {
                ret = diff;
            }
        }
        return ret;
    }
}
