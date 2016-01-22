/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.sal.model.CurrentTradeData;
import stock.common.sal.sina.SinaStockClient;
import stock.common.sal.sina.SinaStockClientImpl;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.PathUtil;
import stock.core.model.models.BollValueTuple;
import stock.core.model.models.StockCodeNearestPeakGroup;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: NearestPeakForecastProcessor.java, v 0.1 2015/12/7 23:57 yuanren.syr Exp $
 */
public class NearestPeakForecastProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    private static DailyTradeDAO   dailyTradeDAO;

    private static final int       N               = 15;
    private static final int       K               = 3;

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        List<StockCodeNearestPeakGroup> stockCodes = Lists.newArrayList();
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")) {
                List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                    stockCode, "2015-10-01");
                double highest = 0;
                Date highestDate = null;
                for (DailyTradeData dailyTradeData : dailyTradeDatas) {
                    if (dailyTradeData.getHighestPrice() > highest) {
                        highest = dailyTradeData.getHighestPrice();
                        highestDate = dailyTradeData.getCurrentDate();
                    }
                }

                CurrentTradeData ctd = sinaStockClient.getStock(stockCode);

                if (ctd == null || StringUtils.contains(ctd.getStockName(), "ST")) {
                    continue;
                }
                if (ctd.getOpenPrice() == 0) {
                    continue;
                }

                if (!CollectionUtils.isEmpty(dailyTradeDatas)
                    && ctd.getCurrentPrice() >= 0.9 * highest) {
                    StockCodeNearestPeakGroup group = new StockCodeNearestPeakGroup(stockCode,
                        ctd.getStockName(), highest, highestDate, ctd.getCurrentPrice());
                    group.setCurTradeDate(ctd);
                    stockCodes.add(group);
                }
            }
        }

        List<StockCodeNearestPeakGroup> groups = sortByLimitUp(stockCodes);
        for (StockCodeNearestPeakGroup group : groups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                group.getStockCode(), "2015-07-07");
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
            System.out.print(group.getStockCode() + "," + group.getStockName() + ","
                             + highestDate06Str + "," + highestPrice06 + ","
                             + DecimalUtil.formatPercent(rasingRate06) + ","
                             + DateUtil.simpleFormat(highestDate0707) + "," + highestPrice0707
                             + "," + DateUtil.simpleFormat(group.getHighestDate()) + ","
                             + group.getHighestPrice() + "," + group.getCurrentPrice() + ",");
            System.out.print(group.getCurTradeDate().getTradingVolumn()
                             + ","
                             + DecimalUtil
                                 .formatDecimal(group.getCurTradeDate().getTradingAmount()) + ",");
            System.out.println(CollectionUtils.isEmpty(group.getDates()) ? "无涨停" : DateUtil
                .simpleFormat(group.getDates().get(group.getDates().size() - 1)));

        }
    }

    private static List<StockCodeNearestPeakGroup> sortByLimitUp(List<StockCodeNearestPeakGroup> stockGroups) {
        List<StockCodeNearestPeakGroup> results = Lists.newArrayList();
        for (StockCodeNearestPeakGroup stockGroup : stockGroups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                stockGroup.getStockCode(), "2015-01-01");
            List<Date> result = Lists.newArrayList();

            CurrentTradeData ctd = sinaStockClient.getStock(stockGroup.getStockCode());

            if (ctd == null || StringUtils.contains(ctd.getStockName(), "ST")) {
                continue;
            }
            if (ctd.getOpenPrice() == 0) {
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
                stockGroup.getStockCode(), ctd.getStockName(), result,
                stockGroup.getHighestPrice(), stockGroup.getHighestDate(),
                stockGroup.getCurrentPrice());
            group.setCurTradeDate(stockGroup.getCurTradeDate());
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

    private static BollValueTuple calcBoll(List<DailyTradeData> dtds, int start, int end) {
        BollValueTuple bollValueTuple = new BollValueTuple();
        double sum = 0;
        int N = end - start;
        double avg = 0;
        for (int i = start; i < end; ++i) {
            sum += dtds.get(i).getClosingPrice();
        }
        avg = sum / N;

        sum = 0;
        for (int i = start; i < end; ++i) {
            double diff = dtds.get(i).getClosingPrice() - avg;
            sum += diff * diff;
        }
        bollValueTuple.setAvgPrice(avg);
        bollValueTuple.setStandardDeviation(Math.sqrt(sum / N));
        bollValueTuple.setUpPrice(avg + K * bollValueTuple.getStandardDeviation());
        bollValueTuple.setStandardDeviationRate(K * bollValueTuple.getStandardDeviation()
                                                / bollValueTuple.getAvgPrice());
        return bollValueTuple;
    }
}
