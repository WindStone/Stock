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

import com.google.common.collect.Lists;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.config.DailyDataWorkerConfig;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.stock.data.exporter.Exporter;
import stock.web.stock.data.processor.followed.FollowedDataProcessor;
import stock.web.stock.data.processor.stock.DataProcessor;
import stock.web.utils.RasingRateUtils;

/**
 * @author yuanren.syr
 * @version $Id: DailyDataProcessWorker.java, v 0.1 2016/1/18 23:22 yuanren.syr Exp $
 */
public class DailyDataProcessWorker extends AbstractProcessWorker {

    private static final double         FILTER_PERCENT        = 0.4;
    private static final double         SORT_PERCENT          = 0.2;
    private static final double         FOLLOWED_PERCENT      = 0.4;

    private String                      fileName;

    private Date                        currentDate;

    private DataProcessor               mainProcessor;

    private List<FollowedDataProcessor> followedDataProcessors;

    /** excel输出器 */
    private static Exporter             excelExporter;

    private Map<String, Object>         param;

    private int                         mainProcessedSize     = -1;

    private int                         followedProcessedSize = -1;

    private int                         totalSize             = -1;

    private int                         followedTotalSize     = -1;

    private DailyDataWorkerConfig       dailyDataWorkerConfig;

    public DailyDataProcessWorker() {

    }

    public DailyDataProcessWorker(Date currentDate, DailyDataWorkerConfig dailyDataWorkerConfig,
                                  Map<String, Object> param) {
        this.currentDate = currentDate;
        this.fileName = dailyDataWorkerConfig.getFileName();
        this.param = param;
        this.mainProcessor = dailyDataWorkerConfig.getMainProcessor();
        this.followedDataProcessors = dailyDataWorkerConfig.getFollowedDataProcessors();
    }

    public void execute() {
        List<String> stockCodes = getStockCodes();
        totalSize = stockCodes.size();
        List<DailyTradeData> dtds = Lists.newArrayList();
        for (mainProcessedSize = 0; mainProcessedSize < totalSize; ++mainProcessedSize) {
            String stockCode = stockCodes.get(mainProcessedSize);
            DailyTradeData dailyTradeData = mainProcessor.processEach(stockCode, currentDate, null);
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
        List<TitleLabel> mainTitleLabels = mainProcessor.getSpecialTitleLabels();
        if (mainTitleLabels != null) {
            titleRow.addAll(mainTitleLabels);
        }
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
            List<ContentLabel> mainContents = mainProcessor.getSpecialContents(dtd);
            if (mainContents != null) {
                row.addAll(mainContents);
            }
            if (!CollectionUtils.isEmpty(followedDataProcessors)) {
                for (FollowedDataProcessor followedDataProcessor : followedDataProcessors) {
                    row.addAll(followedDataProcessor.getFollowedDatas(dtd, param));
                }
            }
            labels.add(row);
        }
        if (!StringUtils.isEmpty(fileName)) {
            excelExporter.writeContentResult(titleRow, labels,
                fileName + "_" + DateUtil.simpleFormat(currentDate) + ".xls", currentDate);
        }
    }

    public List<ContentLabel> processMainFollowed(DailyTradeData dtd) {
        List<ContentLabel> results = Lists.newArrayList();
        results.add(new ContentLabel(dtd.getStockCode()));
        results.add(new ContentLabel(dtd.getStockName()));
        results.add(new ContentLabel(DecimalUtil.formatDecimal(dtd.getClosingPrice(null))));
        results.add(new ContentLabel(DecimalUtil.formatPercent(RasingRateUtils.getRasingRate(
            dtd.getStockCode(), dtd.getCurrentDate()))));
        results.add(new ContentLabel(new BigDecimal(dtd.getTradingVolume()).toString()));
        results.add(new ContentLabel(DecimalUtil.formatDecimal(dtd.getTradingAmount()).toString()));
        return results;
    }

    public double getProcess() {
        double filterPercent = 0, sortPercent = 0, followedPercent = 0;
        if (mainProcessedSize >= 0) {
            filterPercent = (double) mainProcessedSize / (double) totalSize * FILTER_PERCENT;
        }
        if (followedProcessedSize >= 0) {
            sortPercent = SORT_PERCENT;
            followedPercent = (double) followedProcessedSize / (double) followedTotalSize
                              * FOLLOWED_PERCENT;
        }
        return filterPercent + sortPercent + followedPercent;
    }

    public void setExcelExportor(Exporter excelExporter) {
        DailyDataProcessWorker.excelExporter = excelExporter;
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

    public int getMainProcessedSize() {
        return mainProcessedSize;
    }

    public int getFollowedProcessedSize() {
        return followedProcessedSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getFollowedTotalSize() {
        return followedTotalSize;
    }
}
