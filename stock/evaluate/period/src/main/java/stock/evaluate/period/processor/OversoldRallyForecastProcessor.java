/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.text.DecimalFormat;
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
import stock.common.util.PathUtil;
import stock.core.model.models.StockCodeNearestPeakGroup;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: NearestPeakForecastProcessor.java, v 0.1 2015/12/7 23:57 yuanren.syr Exp $
 */
public class OversoldRallyForecastProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    private static DailyTradeDAO   dailyTradeDAO;

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
                int highestIndex = -1;
                for (int i = 0; i < dailyTradeDatas.size(); ++i) {
                    DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                    if (dailyTradeData.getHighestPrice() > highest) {
                        highest = dailyTradeData.getHighestPrice();
                        highestDate = dailyTradeData.getCurrentDate();
                        highestIndex = i;
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
                    && ctd.getCurrentPrice() <= 0.75 * highest
                    && ctd.getCurrentPrice() >= 0.5 * highest) {
                    stockCodes.add(new StockCodeNearestPeakGroup(stockCode, ctd.getStockName(),
                        highest, highestDate, ctd.getCurrentPrice()));
                }
            }
        }

        List<StockCodeNearestPeakGroup> groups = sortByLimitUp(stockCodes);
        for (StockCodeNearestPeakGroup group : groups) {
            double rate = group.getCurrentPrice() / group.getHighestPrice();
            System.out.println(group.getStockCode() + "," + group.getStockName() + ","
                               + DateUtil.simpleFormat(group.getHighestDate()) + ","
                               + group.getHighestPrice() + "," + group.getCurrentPrice() + ","
                               + new DecimalFormat("#0.00%").format(1 - rate));
        }
    }

    private static List<StockCodeNearestPeakGroup> sortByLimitUp(List<StockCodeNearestPeakGroup> stockGroups) {
        List<StockCodeNearestPeakGroup> results = Lists.newArrayList();
        for (StockCodeNearestPeakGroup stockGroup : stockGroups) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                stockGroup.getStockCode(), "2015-07-07");
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
            results.add(new StockCodeNearestPeakGroup(stockGroup.getStockCode(),
                ctd.getStockName(), result, stockGroup.getHighestPrice(), stockGroup
                    .getHighestDate(), stockGroup.getCurrentPrice()));
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
