/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.utils;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: RasingRateUtils.java, v 0.1 2016/1/12 22:50 yuanren.syr Exp $
 */
public class RasingRateUtils {

    /** DailyTradeDAO */
    private static DailyTradeDAO dailyTradeDAO;

    /** 涨停阈值 */
    private static double        RASING_RATE_THRESHOLD = 0.098;

    public static Double getRasingRate(DailyTradeData prevDailyTradeData,
                                       DailyTradeData curDailyTradeData) {
        if (prevDailyTradeData == null || curDailyTradeData == null) {
            return null;
        }
        return (curDailyTradeData.getClosingPrice() - prevDailyTradeData.getClosingPrice())
               / prevDailyTradeData.getClosingPrice();
    }

    public static String getRasingRateStr(DailyTradeData prevDailyTradeData,
                                          DailyTradeData curDailyTradeData) {
        Double rate = getRasingRate(prevDailyTradeData, curDailyTradeData);
        if (rate == null) {
            return null;
        }
        return DecimalUtil.formatPercent(rate);
    }

    public static double getRasingRate(String stockCode, Date date) {
        List<DailyTradeData> dtds = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), 2);
        if (CollectionUtils.isEmpty(dtds) || dtds.size() != 2) {
            return 0;
        }
        return (dtds.get(0).getClosingPrice() - dtds.get(1).getClosingPrice())
               / dtds.get(1).getClosingPrice();
    }

    public static List<DailyTradeData> getLimitUpTradeDatas(String stockCode) {
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(stockCode,
            "2015-01-01");
        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return null;
        }
        List<DailyTradeData> dtds = Lists.newArrayList();
        for (int i = 0; i < dailyTradeDatas.size() - 1; ++i) {
            double rasingRate = (dailyTradeDatas.get(i + 1).getClosingPrice() - dailyTradeDatas
                .get(i).getClosingPrice()) / dailyTradeDatas.get(i).getClosingPrice();
            if (rasingRate > RASING_RATE_THRESHOLD) {
                dtds.add(dailyTradeDatas.get(i + 1));
            }
        }
        return dtds;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        RasingRateUtils.dailyTradeDAO = dailyTradeDAO;
    }

    public static boolean isRaisingUpLimit(double rasingRate) {
        return rasingRate > RASING_RATE_THRESHOLD;
    }
}
