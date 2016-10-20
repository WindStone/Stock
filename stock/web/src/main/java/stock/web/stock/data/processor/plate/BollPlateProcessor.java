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
 * @author yuanren.syr
 * @version $Id: DMAPlateProcessor.java, v 0.1 2016/2/2 0:01 yuanren.syr Exp $
 */
public class BollPlateProcessor extends AbstractPlateProcessor {

    private BollService bollService;

    public DailyPlateData processPlate(String plateName, List<String> stockCodes, Date calcDate) {
        DailyPlateData dailyPlateData = new DailyPlateData();

        int totalStockCode = 0, aboveStockCode = 0;
        List<String> aboveStockCodeList = Lists.newArrayList();
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
                if (bollValueTuple.getClosingPrice() > bollValueTuple.getAvgPrice()) {
                    aboveStockCode++;
                    aboveStockCodeList.add(stockCode);
                }
            }
        }
        if (totalStockCode == 0) {
            return null;
        }
        dailyPlateData.setValue(DecimalUtil.formatDecimal((double) aboveStockCode
                                                          / (double) totalStockCode));
        dailyPlateData.setParam(bollService.getParamConfig());
        dailyPlateData.setPlateName(plateName);
        dailyPlateData.setTradingDate(calcDate);
        printResultList(calcDate, aboveStockCodeList);
        return dailyPlateData;
    }

    protected String getParam() {
        return bollService.getParamConfig();
    }

    public void setBollService(BollService bollService) {
        this.bollService = bollService;
    }

}
