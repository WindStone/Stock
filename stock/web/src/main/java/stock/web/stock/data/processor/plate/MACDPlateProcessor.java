/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.plate;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.dataobject.DailyPlateData;
import stock.common.util.DecimalUtil;
import stock.core.model.models.MACDValueTuple;
import stock.web.service.MACDService;

import com.google.common.collect.Lists;
import stock.web.stock.data.processor.plate.AbstractPlateProcessor;

/**
 * @author yuanren.syr
 * @version $Id: DMAPlateProcessor.java, v 0.1 2016/2/2 0:01 yuanren.syr Exp $
 */
public class MACDPlateProcessor extends AbstractPlateProcessor {

    private MACDService macdService;

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
            MACDValueTuple macdValueTuple = macdService
                .getMACDValueTuple(stockCode, calcDate, null);
            if (macdValueTuple != null) {
                totalStockCode++;
                if (macdValueTuple.getDIF() > macdValueTuple.getDEA()
                    && macdValueTuple.getDIF() > 0 && macdValueTuple.getDEA() > 0) {
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
        dailyPlateData.setParam(macdService.getParamConfig());
        dailyPlateData.setPlateName(plateName);
        dailyPlateData.setTradingDate(calcDate);
        printResultList(calcDate, aboveStockCodeList);
        return dailyPlateData;
    }

    protected String getParam() {
        return macdService.getParamConfig();
    }

    public void setMacdService(MACDService macdService) {
        this.macdService = macdService;
    }
}
