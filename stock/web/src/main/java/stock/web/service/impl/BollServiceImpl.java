/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.core.model.models.BollValueTuple;
import stock.web.config.BollConfig;
import stock.web.service.BollService;

/**
 * @author yuanren.syr
 * @version $Id: BollServiceImpl.java, v 0.1 2016/1/4 22:42 yuanren.syr Exp $
 */
public class BollServiceImpl extends AlgorithmServiceImpl<BollConfig> implements BollService {

    public BollValueTuple getBollValueTuple(String stockCode, Date date, DailyTradeData stdDtd) {
        int n = algorithmConfig.getN();
        int k = algorithmConfig.getK();
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), n);

        DailyTradeData currentDailyTradeData = dailyTradeDatas.get(0);
        if (!DateUtils.isSameDay(currentDailyTradeData.getCurrentDate(), date)) {
            return null;
        }

        BollValueTuple bollValueTuple = new BollValueTuple();
        double sum = 0, avg = 0;
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            sum += dailyTradeData.getClosingPrice(stdDtd);
        }
        avg = sum / n;

        sum = 0;
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            double diff = dailyTradeData.getClosingPrice(stdDtd) - avg;
            sum += diff * diff;
        }
        bollValueTuple.setAvgPrice(avg);
        bollValueTuple.setStandardDeviation(Math.sqrt(sum / n));
        bollValueTuple.setUpPrice(avg + k * bollValueTuple.getStandardDeviation());
        bollValueTuple.setDownPrice(avg - k * bollValueTuple.getStandardDeviation());
        bollValueTuple.setStandardDeviationRate(k * bollValueTuple.getStandardDeviation()
                                                / bollValueTuple.getAvgPrice());
        bollValueTuple.setOpeningPrice(currentDailyTradeData.getOpeningPrice(stdDtd));
        bollValueTuple.setClosingPrice(currentDailyTradeData.getClosingPrice(stdDtd));

        return bollValueTuple;
    }

}
