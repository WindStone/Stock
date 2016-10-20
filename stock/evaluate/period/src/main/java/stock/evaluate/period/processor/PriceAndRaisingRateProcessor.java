/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.PathUtil;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: PriceAndRaisingRateProcessor.java, v 0.1 2016/3/22 23:12 yuanren.syr Exp $
 */
public class PriceAndRaisingRateProcessor {

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        Date startDate = DateUtil.parseSimpleDate("2016-01-01");
        Date endDate = DateUtil.parseSimpleDate("2016-03-22");
        List<String> rasingRates0 = Lists.newArrayList();
        List<String> rasingRates1 = Lists.newArrayList();
        List<String> rasingRates2 = Lists.newArrayList();
        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();
        List<String> dates = Lists.newArrayList();
        for (Date date = startDate; DateUtil.getDiffInDays(date, endDate) < 0; date = DateUtils
            .addDays(date, 1)) {
            int priceCnt[] = { 0, 0, 0 };
            int rasingRateCnt[] = { 0, 0, 0 };
            int totalCnt = 0;
            for (String stockCode : stockCodes) {
                if (!StringUtils.startsWith(stockCode, "SZ00")
                    && !StringUtils.startsWith(stockCode, "SH60")
                    && !StringUtils.startsWith(stockCode, "SZ300")) {
                    continue;
                }

                List<DailyTradeData> dtds = dailyTradeDAO.queryByPrevKTradingData(stockCode,
                    DateUtil.simpleFormat(date), 2);
                if (CollectionUtils.isEmpty(dtds) || dtds.size() < 2) {
                    continue;
                }
                if (!StringUtils.equals(DateUtil.simpleFormat(dtds.get(0).getCurrentDate()),
                    DateUtil.simpleFormat(date))) {
                    continue;
                }
                double price0 = dtds.get(0).getClosingPrice(dtds.get(0));
                double price1 = dtds.get(1).getClosingPrice(dtds.get(0));
                double rasingRate = (price0 - price1) / price1;
                String percent = DecimalUtil.formatPercent(rasingRate);
                if (price0 < 10) {
                    if (rasingRate > 0.08) {
                        rasingRateCnt[0]++;
                        totalCnt++;
                    }
                } else if (price0 >= 10 && price0 <= 30) {
                    if (rasingRate > 0.08) {
                        rasingRateCnt[1]++;
                        totalCnt++;
                    }
                } else {
                    if (rasingRate > 0.08) {
                        rasingRateCnt[2]++;
                        totalCnt++;
                    }
                }
            }
            if (totalCnt == 0) {
            } else {
                dates.add(DateUtil.simpleFormat(date));
                addPercent(totalCnt, rasingRateCnt[0], rasingRates0);
                addPercent(totalCnt, rasingRateCnt[1], rasingRates1);
                addPercent(totalCnt, rasingRateCnt[2], rasingRates2);
            }
        }
        System.out.print("Y1=[");
        for (String rasingRate : rasingRates0) {
            System.out.print(StringUtils.substring(rasingRate, 0, rasingRate.length() - 1) + " ");
        }
        System.out.println("];");

        System.out.print("Y2=[");
        for (String rasingRate : rasingRates1) {
            System.out.print(StringUtils.substring(rasingRate, 0, rasingRate.length() - 1) + " ");
        }
        System.out.println("];");

        System.out.print("Y3=[");
        for (String rasingRate : rasingRates2) {
            System.out.print(StringUtils.substring(rasingRate, 0, rasingRate.length() - 1) + " ");
        }
        System.out.println("];");

        System.out.print("X=[1:1:" + dates.size() + "]");
        System.out.print("set(gca, 'XTickLabel', [");
        for (String date : dates) {
            System.out.print(date + " ");
        }
        System.out.println("];");
        System.out.println("plot(X, Y1, X, Y2, X, Y3)");
    }

    private static void addPercent(int priceCnt, int rasingRateCnt, List<String> rasing) {
        if (priceCnt == 0) {
            rasing.add("0%");
        } else {
            rasing.add(DecimalUtil.formatPercent((double) rasingRateCnt / (double) priceCnt));
        }
    }
}
