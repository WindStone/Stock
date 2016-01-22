/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
 * @version $Id: NearestPeakProcessor.java, v 0.1 2016/1/7 11:59 yuanren.syr Exp $
 */
public class NearestPeakProcessor extends AbstractDataProcessor {

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
                for (DailyTradeData dailyTradeData : dailyTradeDatas) {
                    if (dailyTradeData.getHighestPrice() > highest) {
                        highest = dailyTradeData.getHighestPrice();
                        highestDate = dailyTradeData.getCurrentDate();
                    }
                }

                List<DailyTradeData> currentDailyTrades = dailyTradeDAO.queryByPrevKTradingData(
                    stockCode, DateUtil.simpleFormat(currentDate), 2);

                if (CollectionUtils.isEmpty(currentDailyTrades)) {
                    continue;
                }
                DailyTradeData dtd = currentDailyTrades.get(0);
                DailyTradeData prevDtd = currentDailyTrades.size() > 1 ? currentDailyTrades.get(1)
                    : null;
                if (StringUtils.contains(dtd.getStockName(), "ST")) {
                    continue;
                }

                if (!CollectionUtils.isEmpty(dailyTradeDatas)
                    && dtd.getClosingPrice() >= 0.9 * highest) {
                    StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(stockCode,
                        dtd.getStockName(), highest, highestDate, dtd.getClosingPrice());
                    group.setCurDailyTradeDate(dtd);
                    group.setPrevDailyTradeData(prevDtd);

                    stockCodes.add(group);
                }
            }
        }

        List<StockCodeNearestPeakGroup> groups = sortByLimitUp(stockCodes, currentDate);
        for (StockCodeNearestPeakGroup group : groups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                group.getStockCode(), "2015-07-07", DateUtil.simpleFormat(currentDate));
            double highestPrice0707 = 0;
            Date highestDate0707 = null;
            for (DailyTradeData dailyTradeData : dailyTradeDatas) {
                if (highestPrice0707 < dailyTradeData.getHighestPrice()) {
                    highestPrice0707 = dailyTradeData.getHighestPrice();
                    highestDate0707 = dailyTradeData.getCurrentDate();
                }
            }
            List<DailyTradeData> dailyTradeDatas2 = dailyTradeDAO.queryByIntervalTradingData(
                group.getStockCode(), "2015-06-01", "2015-06-30");
            double highestPrice06 = 0;
            Date highestDate06 = null;
            for (DailyTradeData dailyTradeData : dailyTradeDatas2) {
                if (highestPrice06 < dailyTradeData.getHighestPrice()) {
                    highestPrice06 = dailyTradeData.getHighestPrice();
                    highestDate06 = dailyTradeData.getCurrentDate();
                }
            }
            double rasingRate06 = 0;
            if (highestDate06 != null) {
                rasingRate06 = (group.getCurrentPrice() - highestPrice06) / highestPrice06;
            }
            String highestDate06Str = highestDate06 == null ? "停牌" : DateUtil
                .simpleFormat(highestDate06);
            List<String> row = Lists.newArrayList();
            row.add(group.getStockCode());
            row.add(group.getStockName());
            row.add(highestDate06Str);

            row.add(DecimalUtil.formatDecimal(highestPrice06));
            row.add(DecimalUtil.formatPercent(rasingRate06));
            row.add(DateUtil.simpleFormat(highestDate0707));
            row.add(DecimalUtil.formatDecimal(highestPrice0707));
            row.add(DateUtil.simpleFormat(group.getHighestDate()));
            row.add(DecimalUtil.formatDecimal(group.getHighestPrice()));
            row.add(DecimalUtil.formatDecimal(group.getCurrentPrice()));
            row.add(StringUtils.defaultString(
                RasingRateUtils.getRasingRateStr(group.getPrevDailyTradeData(),
                    group.getCurDailyTradeDate()), ""));

            row.add(new BigDecimal(group.getCurDailyTradeDate().getTradingVolume()).toString());
            row.add(DecimalUtil.formatDecimal(group.getCurDailyTradeDate().getTradingAmount()));

            if (group.getDates() == null || group.getDates().size() <= 1) {
                row.add("无涨停");
            } else {
                row.add(DateUtil.simpleFormat(group.getDates().get(group.getDates().size() - 2)));
            }
            row.add(CollectionUtils.isEmpty(group.getDates()) ? "无涨停" : DateUtil.simpleFormat(group
                .getDates().get(group.getDates().size() - 1)));

            results.add(row);
        }
        return results;
    }

    public List<List<String>> evaluate(Date prevDate) {
        List<List<String>> results = Lists.newArrayList();
        List<StockCodeNearestPeakGroup> stockCodes = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(prevDate);
        Date endDate;
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            endDate = DateUtils.addDays(prevDate, 3);
        } else {
            endDate = DateUtils.addDays(prevDate, 1);
        }
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")) {
                List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                    stockCode, "2015-10-01", DateUtil.simpleFormat(prevDate));
                double highest = 0;
                Date highestDate = null;
                Iterator<DailyTradeData> iter = dailyTradeDatas.iterator();
                DailyTradeData curDailyTradeData = null;

                while (iter.hasNext()) {
                    DailyTradeData dailyTradeData = iter.next();

                    if (dailyTradeData.getHighestPrice() > highest) {
                        highest = dailyTradeData.getHighestPrice();
                        highestDate = dailyTradeData.getCurrentDate();
                    }
                    if (DateUtil.getDiffInDays(dailyTradeData.getCurrentDate(), prevDate) > 0) {
                        break;
                    } else if (DateUtil.getDiffInDays(dailyTradeData.getCurrentDate(), prevDate) == 0) {
                        curDailyTradeData = dailyTradeData;
                        break;
                    }
                }

                if (curDailyTradeData == null
                    || StringUtils.contains(curDailyTradeData.getStockName(), "ST")) {
                    continue;
                }

                if (highestDate == null) {
                    continue;
                }

                if (!CollectionUtils.isEmpty(dailyTradeDatas)
                    && curDailyTradeData.getClosingPrice() >= 0.9 * highest) {
                    StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(stockCode,
                        curDailyTradeData.getStockName(), highest, highestDate,
                        curDailyTradeData.getClosingPrice());

                    List<DailyTradeData> currentDailyTrades = dailyTradeDAO
                        .queryByPrevKTradingData(stockCode, DateUtil.simpleFormat(prevDate), 2);
                    DailyTradeData prevDtd = (currentDailyTrades == null || currentDailyTrades
                        .size() > 1) ? currentDailyTrades.get(1) : null;
                    group.setCurDailyTradeDate(curDailyTradeData);
                    group.setPrevDailyTradeData(prevDtd);
                    stockCodes.add(group);
                }
            }
        }

        List<StockCodeNearestPeakGroup> groups = sortByLimitUp(stockCodes, prevDate);
        for (StockCodeNearestPeakGroup group : groups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                group.getStockCode(), "2015-07-07", DateUtil.simpleFormat(prevDate));
            double highestPrice0707 = 0;
            Date highestDate0707 = null;
            for (DailyTradeData dailyTradeData : dailyTradeDatas) {
                if (highestPrice0707 < dailyTradeData.getHighestPrice()) {
                    highestPrice0707 = dailyTradeData.getHighestPrice();
                    highestDate0707 = dailyTradeData.getCurrentDate();
                }
            }
            List<DailyTradeData> dailyTradeDatas2 = dailyTradeDAO.queryByIntervalTradingData(
                group.getStockCode(), "2015-06-01", "2015-06-30");
            double highestPrice06 = 0;
            Date highestDate06 = null;
            for (DailyTradeData dailyTradeData : dailyTradeDatas2) {
                if (highestPrice06 < dailyTradeData.getHighestPrice()) {
                    highestPrice06 = dailyTradeData.getHighestPrice();
                    highestDate06 = dailyTradeData.getCurrentDate();
                }
            }
            double rasingRate06 = 0;
            if (highestDate06 != null) {
                rasingRate06 = (group.getCurrentPrice() - highestPrice06) / highestPrice06;
            }
            String highestDate06Str = highestDate06 == null ? "停牌" : DateUtil
                .simpleFormat(highestDate06);
            String highestDate = group.getHighestDate() == null ? "一直停牌" : DateUtil
                .simpleFormat(group.getHighestDate());

            List<String> row = Lists.newArrayList();
            row.add(group.getStockCode());
            row.add(group.getStockName());
            row.add(highestDate06Str);

            row.add(DecimalUtil.formatDecimal(highestPrice06));
            row.add(DecimalUtil.formatPercent(rasingRate06));
            row.add(DateUtil.simpleFormat(highestDate0707));
            row.add(DecimalUtil.formatDecimal(highestPrice0707));
            row.add(highestDate);
            row.add(DecimalUtil.formatDecimal(group.getHighestPrice()));
            row.add(DecimalUtil.formatDecimal(group.getCurrentPrice()));
            row.add(StringUtils.defaultString(
                RasingRateUtils.getRasingRateStr(group.getPrevDailyTradeData(),
                    group.getCurDailyTradeDate()), ""));

            row.add(new BigDecimal(group.getCurDailyTradeDate().getTradingVolume()).toString());
            row.add(DecimalUtil.formatDecimal(group.getCurDailyTradeDate().getTradingAmount()));

            if (group.getDates() == null || group.getDates().size() <= 1) {
                row.add("无涨停");
            } else {
                row.add(DateUtil.simpleFormat(group.getDates().get(group.getDates().size() - 2)));
            }

            row.add(CollectionUtils.isEmpty(group.getDates()) ? "无涨停" : DateUtil.simpleFormat(group
                .getDates().get(group.getDates().size() - 1)));

            List<DailyTradeData> nextDailyTradeDatas = dailyTradeDAO.queryByPrevKTradingData(
                group.getStockCode(), DateUtil.simpleFormat(endDate), 2);
            double rasingRate = (nextDailyTradeDatas.get(0).getClosingPrice() - nextDailyTradeDatas
                .get(1).getClosingPrice()) / nextDailyTradeDatas.get(1).getClosingPrice();
            row.add(DecimalUtil.formatPercent(rasingRate));
            if (rasingRate > 0.098) {
                row.add("*");
            }

            results.add(row);
        }
        return results;
    }

    public List<TitleLabel> getTitleLables() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "股票代码"));
        titleRow.add(new TitleLabel(1, "股票名称"));
        titleRow.add(new TitleLabel(2, "6月份最高点"));
        titleRow.add(new TitleLabel(1, "当前股价到最高点值"));
        titleRow.add(new TitleLabel(2, "7月7日后最高点"));
        titleRow.add(new TitleLabel(2, "10月01日之后最高点"));
        titleRow.add(new TitleLabel(1, "当前价格"));
        titleRow.add(new TitleLabel(1, "当天涨幅"));
        titleRow.add(new TitleLabel(1, "成交量(股)"));
        titleRow.add(new TitleLabel(1, "成交金额(元)"));
        titleRow.add(new TitleLabel(1, "前一次涨停"));
        titleRow.add(new TitleLabel(1, "最近一次涨停"));
        return titleRow;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        List<TitleLabel> forcastLabelRow = Lists.newArrayList();
        forcastLabelRow.addAll(getTitleLables());
        forcastLabelRow.add(new TitleLabel(1, "涨幅"));
        forcastLabelRow.add(new TitleLabel(1, "是否涨停"));
        return forcastLabelRow;
    }

    private List<StockCodeNearestPeakGroup> sortByLimitUp(List<StockCodeNearestPeakGroup> stockGroups,
                                                          Date prevDate) {
        List<StockCodeNearestPeakGroup> results = Lists.newArrayList();
        for (StockCodeNearestPeakGroup stockGroup : stockGroups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                stockGroup.getStockCode(), "2015-01-01");
            List<Date> result = Lists.newArrayList();

            DailyTradeData dtd = null;
            if (!CollectionUtils.isEmpty(dailyTradeDatas)) {
                for (DailyTradeData dailyTradeData : dailyTradeDatas) {
                    if (DateUtils.isSameDay(dailyTradeData.getCurrentDate(), prevDate)) {
                        dtd = dailyTradeData;
                    }
                }
            }

            if (dtd == null) {
                continue;
            }

            for (int i = 0; i < dailyTradeDatas.size() - 1; ++i) {
                if (prevDate != null
                    && DateUtil
                        .getDiffInDays(dailyTradeDatas.get(i + 1).getCurrentDate(), prevDate) >= 0) {
                    break;
                }
                DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                DailyTradeData nextTradeData = dailyTradeDatas.get(i + 1);
                double closingPrice = dailyTradeData.getClosingPrice();
                double nextClosingPrice = nextTradeData.getClosingPrice();
                if (((nextClosingPrice - closingPrice) / closingPrice) > 0.098) {
                    result.add(nextTradeData.getCurrentDate());
                }
            }
            StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(
                stockGroup.getStockCode(), dtd.getStockName(), result,
                stockGroup.getHighestPrice(), stockGroup.getHighestDate(),
                stockGroup.getCurrentPrice());
            group.setCurDailyTradeDate(stockGroup.getCurDailyTradeDate());
            group.setPrevDailyTradeData(stockGroup.getPrevDailyTradeData());
            results.add(group);
        }

        Collections.sort(results, new Comparator<StockCodeNearestPeakGroup>() {
            public int compare(StockCodeNearestPeakGroup o1, StockCodeNearestPeakGroup o2) {
                Date o1Date = CollectionUtils.isEmpty(o1.getDates()) ? DateUtil
                    .parseSimpleDate("2014-12-31") : o1.getDates().get(o1.getDates().size() - 1);
                Date o2Date = CollectionUtils.isEmpty(o2.getDates()) ? DateUtil
                    .parseSimpleDate("2014-12-31") : o2.getDates().get(o2.getDates().size() - 1);
                return DateUtil.getDiffInDays(o1Date, o2Date);
            }
        });

        return results;
    }

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {
        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }

        // 1. 查询从2015-10-01到currentDate之间的数据
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(stockCode,
            "2015-10-01", DateUtil.simpleFormat(currentDate));
        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return null;
        }
        double highest = 0;
        DailyTradeData currentDailyTradeData = null;

        // 2. 取最高值并且取当前交易日的数据
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            if (dailyTradeData.getHighestPrice() > highest) {
                highest = dailyTradeData.getHighestPrice();
            }
            if (DateUtils.isSameDay(dailyTradeData.getCurrentDate(), currentDate)) {
                currentDailyTradeData = dailyTradeData;
            }
        }

        // 3.1 当前交易日无数据返回false
        if (currentDailyTradeData == null) {
            return null;
        }
        // 3.2 stockName为st开头的返回false
        if (StringUtils.contains(currentDailyTradeData.getStockName(), "ST")) {
            return null;
        }

        // 4. 接近最高点90%时返回true
        if (currentDailyTradeData.getClosingPrice() >= 0.9 * highest) {
            return currentDailyTradeData;
        }

        return null;
    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        final Map<String, DailyTradeData> map = Maps.newHashMap();
        for (DailyTradeData originTradeData : originTradeDatas) {
            String stockCode = originTradeData.getStockCode();
            List<DailyTradeData> dtds = RasingRateUtils.getLimitUpTradeDatas(stockCode);
            map.put(stockCode, CollectionUtil.fetchLastElement(dtds));
        }
        Collections.sort(originTradeDatas, new Comparator<DailyTradeData>() {
            public int compare(DailyTradeData o1, DailyTradeData o2) {
                return DateUtil.getDiffInDays(map.get(o1.getStockCode()).getCurrentDate(),
                    map.get(o2.getStockCode()).getCurrentDate());
            }
        });
        return originTradeDatas;
    }
}
