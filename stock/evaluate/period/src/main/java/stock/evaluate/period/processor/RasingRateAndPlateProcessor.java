/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.PathUtil;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: RasingRateAndPlateProcessor.java, v 0.1 2016/3/23 22:40 yuanren.syr Exp $
 */
public class RasingRateAndPlateProcessor {

    private static final int WINDOW = 10;

    public static void main(String[] args) throws FileNotFoundException {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        //        Date date = DateUtil.parseSimpleDate("2016-03-01");

        FileOutputStream fos = new FileOutputStream("result2.txt");
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            stockCode = "SZ002615";
            List<Pair<Double, Double>> results = Lists.newArrayList();
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }
            List<DailyTradeData> stockDtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                "2014-01-01", "2016-04-01");
            DailyTradeData lastDtd = stockDtds.get(stockDtds.size() - 1);

            List<DateGroup> avgs = calcAvg(stockDtds);
            for (int i = 0; i < stockDtds.size(); ++i) {
                if (i == 0
                    || stockDtds.get(i).getHighestPrice(lastDtd) < stockDtds.get(i - 1)
                        .getHighestPrice(lastDtd)) {
                    continue;
                }
                int j;
                for (j = i + 1; j < stockDtds.size(); ++j) {
                    if (DateUtil.getDiffInDays(stockDtds.get(j).getCurrentDate(), stockDtds.get(i)
                        .getCurrentDate()) < 20) {
                        continue;
                    }
                    Double rate = stockDtds.get(j).getLowestPrice(lastDtd)
                                  / stockDtds.get(i).getHighestPrice(lastDtd);
                    if (rate < 0.75) {
                        break;
                    }
                }
                if (j >= stockDtds.size()) {
                    continue;
                }
                int start = j, end = j;
                double lowestPrice = stockDtds.get(start).getLowestPrice(lastDtd);
                double maxRate = 0;
                for (j = start + 1; j < stockDtds.size(); ++j) {
                    double rate = (stockDtds.get(j).getHighestPrice(lastDtd) - lowestPrice)
                                  / lowestPrice;
                    if (rate > maxRate) {
                        maxRate = rate;
                    }
                    if (stockDtds.get(j).getClosingPrice(lastDtd) < stockDtds.get(j - 1)
                        .getClosingPrice(lastDtd)) {
                        end = j;
                        break;
                    }
                }
                if (j < stockDtds.size()) {
                    if (stockDtds.get(i).getHighestPrice(lastDtd) * 0.75 > lowestPrice) {
                        double rate = lowestPrice / stockDtds.get(i).getHighestPrice(lastDtd) * 100;
                        double diff = DateUtil.getDiffInDays(stockDtds.get(start).getCurrentDate(),
                                stockDtds.get(i).getCurrentDate());
                        System.out.println(DecimalUtil.formatDecimal(rate) + " " + diff + " "
                                + DecimalUtil.formatDecimal(maxRate * 100));
//                        System.out.println(stockDtds.get(i).getCurrentDate() + " " + stockDtds.get(start).getCurrentDate() + " " + stockDtds.get(end).getCurrentDate());
                    }
                }
            }
            break;
        }
    }

    private static int getNextFalling(List<DateGroup> closePrices, int start) {
        for (; start < closePrices.size() - 1; ++start) {
            if (closePrices.get(start).getPrice() > closePrices.get(start + 1).getPrice()) {
                return start;
            }
        }
        return -1;
    }

    private static int getNextRasing(List<DateGroup> closePrices, int start) {
        for (; start < closePrices.size() - 1; ++start) {
            if (closePrices.get(start).getPrice() < closePrices.get(start + 1).getPrice()) {
                return start;
            }
        }
        return -1;
    }

    private static double getRasingRate(DailyTradeData dtd0, DailyTradeData dtd1) {
        double price0 = dtd0.getClosingPrice(dtd1);
        double price1 = dtd1.getClosingPrice(dtd1);
        return (price1 - price0) / price0;
    }

    private static List<DateGroup> calcAvg(List<DailyTradeData> stockDtds) {
        DailyTradeData lastDtd = stockDtds.get(stockDtds.size() - 1);
        List<DateGroup> closePrices = Lists.newArrayList();
        double sum = 0;
        for (int i = 0; i < WINDOW; ++i) {
            sum += stockDtds.get(i).getClosingPrice(lastDtd);
        }
        DateGroup dg0 = new DateGroup();
        dg0.setDate(stockDtds.get(WINDOW).getCurrentDate());
        dg0.setPrice(sum / WINDOW);
        closePrices.add(dg0);
        for (int i = WINDOW; i < stockDtds.size(); ++i) {
            sum -= stockDtds.get(i - WINDOW).getClosingPrice(lastDtd);
            sum += stockDtds.get(i).getClosingPrice(lastDtd);
            DateGroup dg = new DateGroup();
            dg.setDate(stockDtds.get(i).getCurrentDate());
            dg.setPrice(sum / WINDOW);
            closePrices.add(dg);
        }
        return closePrices;
    }

    private static class DateGroup {

        private Date   date;

        private double price;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
