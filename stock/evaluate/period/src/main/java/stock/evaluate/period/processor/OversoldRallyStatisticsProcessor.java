/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

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
public class OversoldRallyStatisticsProcessor {

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
                if (highestIndex != -1) {
                    for (int i = highestIndex + 1; i < dailyTradeDatas.size(); ++i) {
                        DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                        if (dailyTradeData.getClosingPrice() <= 0.75 * highest) {
                            System.out.println(stockCode
                                               + " "
                                               + ctd.getStockName()
                                               + " "
                                               + DateUtil.simpleFormat(highestDate)
                                               + " "
                                               + highest
                                               + " "
                                               + DateUtil.simpleFormat(dailyTradeData
                                                   .getCurrentDate()) + " "
                                               + dailyTradeData.getClosingPrice());
                            System.out.print("后续7个交易日: ");
                            for (int j = i; j < i + 7 && j < dailyTradeDatas.size(); ++j) {
                                DailyTradeData nextDtd = dailyTradeDatas.get(j);
                                System.out.print(DateUtil.simpleFormat(nextDtd.getCurrentDate())
                                                 + " " + nextDtd.getClosingPrice() + " ");
                            }
                            System.out.println();
                            for (int j = i + 1; j < dailyTradeDatas.size(); ++j) {
                                DailyTradeData prevDtd = dailyTradeDatas.get(j - 1);
                                DailyTradeData curDtd = dailyTradeDatas.get(j);

                                if (curDtd.getClosingPrice() > prevDtd.getClosingPrice()) {
                                    double raisingRate = (curDtd.getClosingPrice() - prevDtd
                                        .getClosingPrice()) / prevDtd.getClosingPrice();
                                    System.out.println("回升点: "
                                                       + DateUtil.simpleFormat(curDtd
                                                           .getCurrentDate())
                                                       + " "
                                                       + curDtd.getClosingPrice()
                                                       + " "
                                                       + new DecimalFormat("#0.00%")
                                                           .format(raisingRate));
                                    break;
                                }
                            }

                            for (int j = i + 1; j < dailyTradeDatas.size(); ++j) {
                                DailyTradeData prevDtd = dailyTradeDatas.get(j - 1);
                                DailyTradeData curDtd = dailyTradeDatas.get(j);
                                double raisingRate = (curDtd.getClosingPrice() - prevDtd
                                    .getClosingPrice()) / prevDtd.getClosingPrice();
                                if (raisingRate >= 0.07) {
                                    System.out.println("反弹点: "
                                                       + DateUtil.simpleFormat(curDtd
                                                           .getCurrentDate())
                                                       + " "
                                                       + curDtd.getClosingPrice()
                                                       + " "
                                                       + new DecimalFormat("#0.00%")
                                                           .format(raisingRate)
                                                       + " 周期: "
                                                       + DateUtil.getDiffInDays(
                                                           curDtd.getCurrentDate(),
                                                           dailyTradeData.getCurrentDate()));
                                    double toHighestRate = prevDtd.getClosingPrice() / highest;
                                    System.out.println("反弹点前一天: "
                                                       + DateUtil.simpleFormat(prevDtd
                                                           .getCurrentDate())
                                                       + " "
                                                       + prevDtd.getClosingPrice()
                                                       + " "
                                                       + new DecimalFormat("#0.00%")
                                                           .format(toHighestRate));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
