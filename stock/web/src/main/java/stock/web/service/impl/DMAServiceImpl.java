/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.core.model.models.DMAValueTuple;
import stock.web.config.DMAConfig;
import stock.web.service.DMAService;
import stock.web.utils.CollectionUtil;
import stock.web.utils.HolidayUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: DMAServiceImpl.java, v 0.1 2016/1/31 23:57 yuanren.syr Exp $
 */
public class DMAServiceImpl extends AlgorithmServiceImpl<DMAConfig> implements DMAService {

    public DMAValueTuple getDMAValueTuple(String stockCode, Date date, final DailyTradeData stdDtd) {
        int shortAvg = algorithmConfig.getShortAvg();
        int longAvg = algorithmConfig.getLongAvg();
        int M = algorithmConfig.getM();
        List<DailyTradeData> dailyTradeDatas = AlgorithmServiceImpl.dailyTradeDAO
            .queryByPrevKTradingData(stockCode, DateUtil.simpleFormat(date), longAvg + M);
        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return null;
        }
        if (dailyTradeDatas.size() < longAvg + M) {
            return null;
        }
        DailyTradeData currentDailyTradeData = dailyTradeDatas.get(0);
        if (!DateUtils.isSameDay(currentDailyTradeData.getCurrentDate(), date)) {
            return null;
        }

        int len = dailyTradeDatas.size();
        Double[] shortSums = new Double[len];
        Double[] longSums = new Double[len];
        List<Double> closingPrices = Lists.transform(dailyTradeDatas,
            new Function<DailyTradeData, Double>() {
                public Double apply(DailyTradeData dailyTradeData) {
                    return dailyTradeData.getClosingPrice(stdDtd);
                }
            });
        for (int i = len - 1; i >= 0; --i) {
            shortSums[i] = 0.0;
            longSums[i] = 0.0;

            shortSums[i] = CollectionUtil.fetchDefault(shortSums, i + 1, 0.0)
                           + closingPrices.get(i)
                           - CollectionUtil.fetchDefault(closingPrices, i + shortAvg, 0.0);
            longSums[i] = CollectionUtil.fetchDefault(longSums, i + 1, 0.0) + closingPrices.get(i)
                          - CollectionUtil.fetchDefault(closingPrices, i + longAvg, 0.0);
        }
        double amaSum = 0;
        for (int i = 0; i < Math.min(M, len); ++i) {
            amaSum += shortSums[i] / shortAvg - longSums[i] / longAvg;
        }
        DMAValueTuple dmaValueTuple = new DMAValueTuple();
        dmaValueTuple.setDMA(shortSums[0] / shortAvg - longSums[0] / longAvg);
        dmaValueTuple.setAMA(amaSum / Math.min(M, len));

        return dmaValueTuple;
    }
}
