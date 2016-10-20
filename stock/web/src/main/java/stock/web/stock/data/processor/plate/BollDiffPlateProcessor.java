/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.plate;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.dataobject.DailyPlateData;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DecimalUtil;
import stock.core.model.models.BollValueTuple;
import stock.web.service.BollService;

import com.google.common.collect.Lists;
import stock.web.stock.data.processor.plate.AbstractPlateProcessor;

/**
 * 用于计算  BOLL当前收盘价距上轴3%以内-距下轴3%以内的股票百分比
 * @author yuanren.syr
 * @version $Id: BollDiffPlateProcessor.java, v 0.1 2016/2/29 23:12 yuanren.syr Exp $
 */
public class BollDiffPlateProcessor extends AbstractPlateProcessor {

    private BollService bollService;

    private double      rate = 0.03;

    @Override
    protected DailyPlateData processPlate(String plateName, List<String> stockCodes, Date calcDate) {

        int totalStockCode = 0, aboveStockCode = 0, belowStockCode = 0;
        List<String> aboveStockCodeList = Lists.newArrayList();
        List<String> belowStockCodeList = Lists.newArrayList();
        for (String stockCode : stockCodes) {
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }
            DailyTradeData dtd = dailyTradeDAO.queryByStockCodeAndDate(stockCode, calcDate);
            if (dtd == null) {
                continue;
            }
            BollValueTuple bollValueTuple = bollService.getBollValueTuple(stockCode, calcDate, dtd);
            if (bollValueTuple != null) {
                totalStockCode++;
                if (dtd.getClosingPrice(dtd) > bollValueTuple.getUpPrice() * (1 - rate)) {
                    aboveStockCode++;
                    aboveStockCodeList.add(stockCode);
                }
                if (dtd.getClosingPrice(dtd) < bollValueTuple.getDownPrice() * (1 + rate)) {
                    belowStockCode++;
                    belowStockCodeList.add(stockCode);
                }
            }
        }

        if (totalStockCode == 0) {
            return null;
        }

        DailyPlateData dailyPlateData = new DailyPlateData();
        dailyPlateData.setValue(DecimalUtil
            .formatDecimal(((double) aboveStockCode - (double) belowStockCode)
                           / (double) totalStockCode));
        dailyPlateData.setParam(bollService.getParamConfig());
        dailyPlateData.setPlateName(plateName);
        dailyPlateData.setTradingDate(calcDate);
        printResultList(calcDate, aboveStockCodeList);
        printResultList(calcDate, belowStockCodeList);
        return dailyPlateData;
    }

    @Override
    protected String getParam() {
        return bollService.getParamConfig();
    }

    public void setBollService(BollService bollService) {
        this.bollService = bollService;
    }

}
