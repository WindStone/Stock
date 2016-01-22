/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.service.algorithm.impl;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.core.model.models.MACDValueTuple;
import stock.core.service.algorithm.MACDService;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: MACDServiceImpl.java, v 0.1 2016/1/4 23:18 yuanren.syr Exp $
 */
public class MACDServiceImpl extends BaseServiceImpl implements MACDService {

    private int ema1   = 9;

    private int ema2   = 21;

    private int dea    = 9;

    private int factor = 2;

    public MACDValueTuple getMACDValueTuple(String stockCode, Date date) {
        List<DailyTradeData> curDailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(stockCode,
            DateUtil.simpleFormat(date));
        if (CollectionUtils.isEmpty(curDailyTradeDatas)) {
            return null;
        }
        double price = curDailyTradeDatas.get(0).getClosingPrice();
        double ema1Rate = (ema1 - 1) / (ema1 + 1);

        int ema1EvaLen = (int) Math.floor(Math.log(0.01 / (price * (1 - ema1Rate)))
                                          / Math.log(ema1Rate));
        double ema2Rate = (ema2 - 1) / (ema2 + 1);
        int ema2EvaLen = (int) Math.floor(Math.log(0.01 / (price * (1 - ema2Rate)))
                                          / Math.log(ema2Rate));

        MACDValueTuple macdValueTuple = new MACDValueTuple();

        double deaRate = (dea - 1) / (dea + 1);
        double deaLen = (int) Math.floor(Math.log(0.01 / (price * (1 - deaRate)))
                                         / Math.log(deaRate));

        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), ema2EvaLen);

        List<Double> ema1List = Lists.newArrayList(0.0);
        List<Double> ema2List = Lists.newArrayList(0.0);
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            double ema1Value = ema1List.get(ema1List.size() - 1) * ema1Rate
                               + dailyTradeData.getClosingPrice() * (1 - ema1Rate);
            double ema2Value = ema2List.get(ema1List.size() - 1) * ema2Rate
                               + dailyTradeData.getClosingPrice() * (1 - ema2Rate);
            ema1List.add(ema1Value);
            ema2List.add(ema2Value);
        }

        List<Double> deaList = Lists.newArrayList(0.0);
        for (int i = 1; i < ema1List.size(); ++i) {
            double deaValue = deaList.get(deaList.size() - 1) * deaRate
                              + (ema1List.get(i) - ema2List.get(i)) * (1 - deaRate);
            deaList.add(deaValue);
        }

        macdValueTuple.setEMA1(ema1List.get(ema1List.size() - 1));
        macdValueTuple.setEMA2(ema2List.get(ema2List.size() - 2));
        macdValueTuple
            .setDIF(ema1List.get(ema1List.size() - 1) - ema2List.get(ema2List.size() - 1));
        macdValueTuple.setDEA(deaList.get(deaList.size() - 1));
        macdValueTuple.setMACD((macdValueTuple.getDIF() - macdValueTuple.getDEA()) * factor);

        return macdValueTuple;
    }
}
