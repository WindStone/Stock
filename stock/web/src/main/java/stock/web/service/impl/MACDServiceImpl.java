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
import stock.core.model.models.MACDValueTuple;
import stock.web.config.MACDConfig;
import stock.web.service.MACDService;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: MACDServiceImpl.java, v 0.1 2016/1/4 23:18 yuanren.syr Exp $
 */
public class MACDServiceImpl extends AlgorithmServiceImpl<MACDConfig> implements MACDService {

    public MACDValueTuple getMACDValueTuple(String stockCode, Date date, DailyTradeData stdDtd) {
        int ema1 = algorithmConfig.getEma1();
        int ema2 = algorithmConfig.getEma2();
        int dea = algorithmConfig.getDea();
        int factor = algorithmConfig.getFactor();
        List<DailyTradeData> curDailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), 1);
        if (CollectionUtils.isEmpty(curDailyTradeDatas)) {
            return null;
        }
        DailyTradeData currentDailyTradeData = curDailyTradeDatas.get(0);
        if (!DateUtils.isSameDay(currentDailyTradeData.getCurrentDate(), date)) {
            return null;
        }
        double price = currentDailyTradeData.getClosingPrice(stdDtd);
        double ema1Rate = (double) (ema1 - 1) / (double) (ema1 + 1);
        int ema1EvaLen = (int) Math.floor(Math.log(0.001 / price) / Math.log(ema1Rate));
        double ema2Rate = (double) (ema2 - 1) / (double) (ema2 + 1);
        int ema2EvaLen = (int) Math.floor(Math.log(0.001 / price) / Math.log(ema2Rate));

        MACDValueTuple macdValueTuple = new MACDValueTuple();

        double deaRate = (double) (dea - 1) / (double) (dea + 1);
        double deaLen = (int) Math.floor(Math.log(0.01 / price) / Math.log(deaRate));

        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(stockCode,
            DateUtil.simpleFormat(date), 200);

        List<Double> ema1List = Lists.newArrayList(0.0);
        List<Double> ema2List = Lists.newArrayList(0.0);
        double ema1Value = 0.0;
        double ema2Value = 0.0;
        for (int i = dailyTradeDatas.size() - 1; i >= 0; --i) {
            DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
            ema1Value = ema1Value * ema1Rate + (dailyTradeData.getClosingPrice(stdDtd))
                        * (1 - ema1Rate);
            ema2Value = ema2Value * ema2Rate + (dailyTradeData.getClosingPrice(stdDtd))
                        * (1 - ema2Rate);
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
        macdValueTuple.setEMA2(ema2List.get(ema2List.size() - 1));
        macdValueTuple
            .setDIF(ema1List.get(ema1List.size() - 1) - ema2List.get(ema2List.size() - 1));
        macdValueTuple.setDEA(deaList.get(deaList.size() - 1));
        macdValueTuple.setMACD((macdValueTuple.getDIF() - macdValueTuple.getDEA()) * factor);

        return macdValueTuple;
    }

}
