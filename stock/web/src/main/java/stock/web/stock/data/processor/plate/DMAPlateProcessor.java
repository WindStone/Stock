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
import stock.core.model.models.DMAValueTuple;
import stock.web.service.DMAService;
import stock.web.stock.data.processor.plate.AbstractPlateProcessor;
import stock.web.utils.HolidayUtils;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: DMAPlateProcessor.java, v 0.1 2016/2/2 0:01 yuanren.syr Exp $
 */
public class DMAPlateProcessor extends AbstractPlateProcessor {

    private DMAService dmaService;

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
            DMAValueTuple prevDmaValueTuple = dmaService.getDMAValueTuple(stockCode,
                HolidayUtils.getPrevWorkDate(calcDate), null);
            DMAValueTuple dmaValueTuple = dmaService.getDMAValueTuple(stockCode, calcDate, null);

            if (prevDmaValueTuple != null && dmaValueTuple != null) {
                totalStockCode++;
                if (prevDmaValueTuple.getAMA() < dmaValueTuple.getAMA()
                    && dmaValueTuple.getDMA() > dmaValueTuple.getAMA()) {
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
        dailyPlateData.setParam(dmaService.getParamConfig());
        dailyPlateData.setPlateName(plateName);
        dailyPlateData.setTradingDate(calcDate);
        printResultList(calcDate, aboveStockCodeList);
        return dailyPlateData;
    }

    protected String getParam() {
        return dmaService.getParamConfig();
    }

    public void setDmaService(DMAService dmaService) {
        this.dmaService = dmaService;
    }
}
