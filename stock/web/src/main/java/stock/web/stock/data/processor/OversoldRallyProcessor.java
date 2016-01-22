/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.core.model.models.StockCodeNearestPeakGroup;
import stock.web.context.StockContext;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.CollectionUtil;
import stock.web.utils.RasingRateUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: OversoldRallyProcessor.java, v 0.1 2016/1/10 23:11 yuanren.syr Exp $
 */
public class OversoldRallyProcessor extends AbstractDataProcessor {

    public List<TitleLabel> getTitleLables() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "股票代码"));
        titleRow.add(new TitleLabel(1, "股票名称"));

        titleRow.add(new TitleLabel(1, "10.01最高点时间"));
        titleRow.add(new TitleLabel(1, "10.01最高点价格"));
        titleRow.add(new TitleLabel(1, "10.01最高点百分比"));
        titleRow.add(new TitleLabel(1, "当前价格"));
        titleRow.add(new TitleLabel(1, "当日涨幅"));
        titleRow.add(new TitleLabel(1, "前一次涨停"));
        titleRow.add(new TitleLabel(1, "最近一次涨停"));
        return titleRow;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        return null;
    }

    public List<List<String>> forcast(Date currentDate) {
        List<List<String>> results = Lists.newArrayList();
        List<StockCodeNearestPeakGroup> stockCodes = Lists.newArrayList();
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")) {
                List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                    stockCode, "2015-10-01", DateUtil.simpleFormat(currentDate));
                double highest = 0;
                Date highestDate = null;
                for (int i = 0; i < dailyTradeDatas.size(); ++i) {
                    DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                    if (dailyTradeData.getHighestPrice() > highest) {
                        highest = dailyTradeData.getHighestPrice();
                        highestDate = dailyTradeData.getCurrentDate();
                    }
                }

                if (CollectionUtils.isEmpty(dailyTradeDatas)) {
                    continue;
                }
                String stockName = dailyTradeDatas.get(0).getStockName();
                if (StringUtils.contains(stockName, "ST")) {
                    continue;
                }
                DailyTradeData dtd = dailyTradeDatas.get(dailyTradeDatas.size() - 1);
                if (!DateUtils.isSameDay(dtd.getCurrentDate(), currentDate)) {
                    continue;
                }

                if (!CollectionUtils.isEmpty(dailyTradeDatas)
                    && dtd.getClosingPrice() <= 0.75 * highest
                    && dtd.getClosingPrice() >= 0.5 * highest) {

                    StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(stockCode,
                        dtd.getStockName(), highest, highestDate, dtd.getClosingPrice());

                    List<DailyTradeData> currentDailyTrades = dailyTradeDAO
                        .queryByPrevKTradingData(stockCode, DateUtil.simpleFormat(currentDate), 2);
                    DailyTradeData prevDtd = (currentDailyTrades == null || currentDailyTrades
                        .size() > 1) ? currentDailyTrades.get(1) : null;
                    group.setCurDailyTradeDate(dtd);
                    group.setPrevDailyTradeData(prevDtd);
                    stockCodes.add(group);
                }
            }
        }

        List<StockCodeNearestPeakGroup> groups = sortByLimitUp(stockCodes);
        for (StockCodeNearestPeakGroup group : groups) {
            double rate = group.getCurrentPrice() / group.getHighestPrice();

            List<String> row = Lists.newArrayList();
            row.add(group.getStockCode());
            row.add(group.getStockName());
            row.add(DateUtil.simpleFormat(group.getHighestDate()));
            row.add(DecimalUtil.formatDecimal(group.getHighestPrice()));
            row.add(DecimalUtil.formatPercent(1 - rate));
            row.add(DecimalUtil.formatDecimal(group.getCurrentPrice()));
            row.add(StringUtils.defaultString(
                RasingRateUtils.getRasingRateStr(group.getPrevDailyTradeData(),
                    group.getCurDailyTradeDate()), ""));
            if (group.getDates() == null || group.getDates().size() <= 1) {
                row.add("无涨停");
            } else {
                row.add(DateUtil.simpleFormat(group.getDates().get(group.getDates().size() - 2)));
            }
            if (CollectionUtils.isEmpty(group.getDates())) {
                row.add("无涨停");
            } else {
                row.add(DateUtil.simpleFormat(group.getDates().get(group.getDates().size() - 1)));
            }
            results.add(row);
        }
        return results;
    }

    public List<List<String>> evaluate(Date prevDate) {
        return null;
    }

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {
        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }

        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(stockCode,
            "2015-10-01", DateUtil.simpleFormat(currentDate));
        double highest = 0;
        for (int i = 0; i < dailyTradeDatas.size(); ++i) {
            DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
            if (dailyTradeData.getHighestPrice() > highest) {
                highest = dailyTradeData.getHighestPrice();
            }
        }

        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return null;
        }
        String stockName = dailyTradeDatas.get(0).getStockName();
        if (StringUtils.contains(stockName, "ST")) {
            return null;
        }
        DailyTradeData dtd = CollectionUtil.fetchLastElement(dailyTradeDatas);
        if (!DateUtils.isSameDay(dtd.getCurrentDate(), currentDate)) {
            return null;
        }

        if (!CollectionUtils.isEmpty(dailyTradeDatas) && dtd.getClosingPrice() <= 0.75 * highest
            && dtd.getClosingPrice() >= 0.5 * highest) {
            return dtd;
        }
        return null;
    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        if (CollectionUtils.isEmpty(originTradeDatas)) {
            return null;
        }
        final Map<String, Double> map = Maps.newHashMap();
        for (DailyTradeData dtd : originTradeDatas) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                dtd.getStockCode(), "2015-10-01", DateUtil.simpleFormat(dtd.getCurrentDate()));
            double highest = 0;
            for (int i = 0; i < dailyTradeDatas.size(); ++i) {
                DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                if (dailyTradeData.getHighestPrice() > highest) {
                    highest = dailyTradeData.getHighestPrice();
                }
            }
            map.put(dtd.getStockCode(), dtd.getClosingPrice() / highest);
        }
        Collections.sort(originTradeDatas, new Comparator<DailyTradeData>() {
            public int compare(DailyTradeData o1, DailyTradeData o2) {
                return (int) (map.get(o1.getStockCode()) * 1000000 - map.get(o2.getStockCode()) * 1000000);
            }
        });

        return originTradeDatas;
    }

    private List<StockCodeNearestPeakGroup> sortByLimitUp(List<StockCodeNearestPeakGroup> stockGroups) {
        List<StockCodeNearestPeakGroup> results = Lists.newArrayList();
        for (StockCodeNearestPeakGroup stockGroup : stockGroups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                stockGroup.getStockCode(), "2015-01-01");
            List<Date> result = Lists.newArrayList();

            if (CollectionUtils.isEmpty(dailyTradeDatas)) {
                continue;
            }
            String stockName = dailyTradeDatas.get(0).getStockName();
            if (StringUtils.contains(stockName, "ST")) {
                continue;
            }

            for (int i = 0; i < dailyTradeDatas.size() - 1; ++i) {
                DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                DailyTradeData nextTradeData = dailyTradeDatas.get(i + 1);
                double closingPrice = dailyTradeData.getClosingPrice();
                double nextClosingPrice = nextTradeData.getClosingPrice();
                if (((nextClosingPrice - closingPrice) / closingPrice) > 0.098) {
                    result.add(nextTradeData.getCurrentDate());
                }
            }
            StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(
                stockGroup.getStockCode(), stockName, result, stockGroup.getHighestPrice(),
                stockGroup.getHighestDate(), stockGroup.getCurrentPrice());
            group.setCurDailyTradeDate(stockGroup.getCurDailyTradeDate());
            group.setPrevDailyTradeData(stockGroup.getPrevDailyTradeData());
            results.add(group);
        }

        Collections.sort(results, new Comparator<StockCodeNearestPeakGroup>() {
            public int compare(StockCodeNearestPeakGroup o1, StockCodeNearestPeakGroup o2) {
                double rate1 = o1.getCurrentPrice() / o1.getHighestPrice();
                double rate2 = o2.getCurrentPrice() / o2.getHighestPrice();
                return (int) (rate1 * 10000 - rate2 * 10000);
            }
        });

        return results;
    }
}
