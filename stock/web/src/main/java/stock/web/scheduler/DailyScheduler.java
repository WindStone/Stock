/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import stock.web.stock.data.collector.DailyTradeDataCollector;
import stock.web.stock.data.exporter.Exporter;
import stock.web.stock.data.processor.NearestPeakProcessor;
import stock.web.stock.data.processor.OversoldRallyProcessor;
import stock.web.utils.HolidayUtils;

/**
 * @author yuanren.syr
 * @version $Id: DailyScheduler.java, v 0.1 2016/1/7 1:16 yuanren.syr Exp $
 */
public class DailyScheduler {

    private DailyTradeDataCollector dailyTradeDataCollector;

    private NearestPeakProcessor    nearestPeakProcessor;

    private OversoldRallyProcessor  oversoldRallyProcessor;

    private Exporter                excelExporter;

    private HolidayUtils            holidayUtils;

    protected void execute() {
    }

    public void setDailyTradeDataCollector(DailyTradeDataCollector dailyTradeDataCollector) {
        this.dailyTradeDataCollector = dailyTradeDataCollector;
    }

    public void setNearestPeakProcessor(NearestPeakProcessor nearestPeakProcessor) {
        this.nearestPeakProcessor = nearestPeakProcessor;
    }

    public void setOversoldRallyProcessor(OversoldRallyProcessor oversoldRallyProcessor) {
        this.oversoldRallyProcessor = oversoldRallyProcessor;
    }

    public void setExcelExporter(Exporter excelExporter) {
        this.excelExporter = excelExporter;
    }

    public void setHolidayUtils(HolidayUtils holidayUtils) {
        this.holidayUtils = holidayUtils;
    }
}
