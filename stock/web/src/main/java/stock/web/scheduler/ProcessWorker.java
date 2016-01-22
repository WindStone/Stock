/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.stock.data.exporter.Exporter;
import stock.web.stock.data.processor.DataProcessor;
import stock.web.stock.data.processor.FollowedDataProcessor;
import stock.web.utils.RasingRateUtils;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: ProcessWorker.java, v 0.1 2016/1/18 23:22 yuanren.syr Exp $
 */
public class ProcessWorker {

    private String                      fileName;

    private Date                        currentDate;

    private DataProcessor               mainProcessor;

    private List<FollowedDataProcessor> followedDataProcessors;

    private static DailyTradeDAO        dailyTradeDAO;

    /** excel输出器 */
    private static Exporter             excelExporter;

    private Map<String, Object>         param;

    private boolean                     finished              = false;

    private int                         mainProcessedSize     = -1;

    private int                         followedProcessedSize = -1;

    private int                         totalSize             = -1;

    private int                         followedTotalSize     = -1;

    public ProcessWorker() {

    }

    public ProcessWorker(Date currentDate, WorkerConfig workerConfig, Map<String, Object> param) {
        this.currentDate = currentDate;
        this.fileName = workerConfig.getFileName();
        this.param = param;
        this.mainProcessor = workerConfig.getMainProcessor();
        this.followedDataProcessors = workerConfig.getFollowedDataProcessors();
    }

    public void work() {

        try {
            List<String> stockCodes = dailyTradeDAO.queryForStockCodes();
            totalSize = stockCodes.size();
            List<DailyTradeData> dtds = Lists.newArrayList();
            for (mainProcessedSize = 0; mainProcessedSize < totalSize; ++mainProcessedSize) {
                String stockCode = stockCodes.get(mainProcessedSize);
                DailyTradeData dailyTradeData = mainProcessor.processEach(stockCode, currentDate,
                    null);
                if (dailyTradeData != null) {
                    dtds.add(dailyTradeData);
                }
            }

            List<TitleLabel> titleRow = Lists.newArrayList();
            titleRow.add(new TitleLabel(1, "股票代码"));
            titleRow.add(new TitleLabel(1, "股票名称"));
            titleRow.add(new TitleLabel(1, "当前价格"));
            titleRow.add(new TitleLabel(1, "当天涨幅"));
            titleRow.add(new TitleLabel(1, "成交量(股)"));
            titleRow.add(new TitleLabel(1, "成交金额(元)"));
            if (!CollectionUtils.isEmpty(followedDataProcessors)) {
                for (FollowedDataProcessor followedDataProcessor : followedDataProcessors) {
                    titleRow.addAll(followedDataProcessor.getTitle(null));
                }
            }
            dtds = mainProcessor.sortDatas(dtds);
            List<List<ContentLabel>> labels = Lists.newArrayList();
            followedTotalSize = dtds == null ? 0 : dtds.size();
            for (followedProcessedSize = 0; followedProcessedSize < followedTotalSize; ++followedProcessedSize) {
                DailyTradeData dtd = dtds.get(followedProcessedSize);
                List<ContentLabel> row = Lists.newArrayList();
                row.addAll(processMainFollowed(dtd));
                if (!CollectionUtils.isEmpty(followedDataProcessors)) {
                    for (FollowedDataProcessor followedDataProcessor : followedDataProcessors) {
                        row.addAll(followedDataProcessor.getFollowedDatas(dtd, param));
                    }
                }
                labels.add(row);
            }
            if (!StringUtils.isEmpty(fileName)) {
                excelExporter.writeContentResult(titleRow, labels,
                    fileName + DateUtil.simpleFormat(currentDate) + ".xls", currentDate);
            }
        } finally {
            this.finished = true;
            ProcessQueue.pollToWork();
        }
    }

    public List<ContentLabel> processMainFollowed(DailyTradeData dtd) {
        List<ContentLabel> results = Lists.newArrayList();
        results.add(new ContentLabel(dtd.getStockCode()));
        results.add(new ContentLabel(dtd.getStockName()));
        results.add(new ContentLabel(DecimalUtil.formatDecimal(dtd.getClosingPrice())));
        results.add(new ContentLabel(DecimalUtil.formatPercent(RasingRateUtils.getRasingRate(
            dtd.getStockCode(), dtd.getCurrentDate()))));
        results.add(new ContentLabel(new BigDecimal(dtd.getTradingVolume()).toString()));
        results.add(new ContentLabel(DecimalUtil.formatDecimal(dtd.getTradingAmount()).toString()));
        return results;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setExcelExportor(Exporter excelExporter) {
        ProcessWorker.excelExporter = excelExporter;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        ProcessWorker.dailyTradeDAO = dailyTradeDAO;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExcelExporter(Exporter excelExporter) {
        this.excelExporter = excelExporter;
    }

    public void setMainProcessor(DataProcessor mainProcessor) {
        this.mainProcessor = mainProcessor;
    }

    public void setFollowedDataProcessors(List<FollowedDataProcessor> followedDataProcessors) {
        this.followedDataProcessors = followedDataProcessors;
    }

}
