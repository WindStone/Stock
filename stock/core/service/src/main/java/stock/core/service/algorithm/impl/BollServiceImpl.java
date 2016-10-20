/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.service.algorithm.impl;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.core.model.models.BollValueTuple;
import stock.core.service.algorithm.BollService;

/**
 * @author yuanren.syr
 * @version $Id: BollServiceImpl.java, v 0.1 2016/1/4 22:42 yuanren.syr Exp $
 */
public class BollServiceImpl extends BaseServiceImpl implements BollService {

    private int n = 15;

    private int k = 3;

    public BollValueTuple getBollValueTuple(String stockCode, Date date) {
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), n);
        BollValueTuple bollValueTuple = new BollValueTuple();
        double sum = 0;
        double avg = 0;
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            sum += dailyTradeData.getClosingPrice(null);
        }
        avg = sum / n;

        sum = 0;
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            double diff = dailyTradeData.getClosingPrice(null) - avg;
            sum += diff * diff;
        }
        bollValueTuple.setAvgPrice(avg);
        bollValueTuple.setStandardDeviation(Math.sqrt(sum / n));
        bollValueTuple.setUpPrice(avg + k * bollValueTuple.getStandardDeviation());
        bollValueTuple.setDownPrice(avg - k * bollValueTuple.getStandardDeviation());
        bollValueTuple.setStandardDeviationRate(k * bollValueTuple.getStandardDeviation()
                                                / bollValueTuple.getAvgPrice());
        return bollValueTuple;
    }

    public void setParam(int n, int k) {
        this.n = n;
        this.k = k;
    }
}
