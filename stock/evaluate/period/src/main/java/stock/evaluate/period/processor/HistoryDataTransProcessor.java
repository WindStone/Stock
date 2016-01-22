/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;

import java.io.File;
import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: HistoryDataTransProcessor.java, v 0.1 2015/12/6 16:09 yuanren.syr Exp $
 */
public class HistoryDataTransProcessor {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    public static void main(String[] args) {

        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        File file = new File(PathUtil.getDataPath() + "/daily/" + "/201512");
        File[] files = file.listFiles();
        for (File f : files) {
            List<DailyTradeData> dtds = historyDataAccessor.getDailyTradeData(f);
            double openingPrice = 0, closingPrice = 0;
            double highestPrice = 0, lowestPrice = 0;

            String dateString = null;
            for (DailyTradeData dtd : dtds) {
                dtd.setStockDailyId(sequenceGenerator.getSequence(SequenceEnum.STOCK_DAILY_SEQ));
                try {
                    dailyTradeDAO.insert(dtd);
                } catch (Exception e) {
                    System.out.println(openingPrice + " " + closingPrice + " " + highestPrice + " "
                                       + lowestPrice);
                    System.out.println(e);
                }
            }
            //            for (DailyTradeData dtd : dtds) {
            //                if (highestPrice == 0) {
            //                    highestPrice = dtd.getHighestPrice();
            //                    lowestPrice = dtd.getLowestPrice();
            //                }
            //                if (StringUtils.equals(dtd.getTime(), "09:35")) {
            //                    openingPrice = dtd.getOpeningPrice();
            //                    dateString = dtd.getDate();
            //                }
            //                highestPrice = Math.max(highestPrice, dtd.getHighestPrice());
            //                lowestPrice = Math.min(lowestPrice, dtd.getLowestPrice());
            //                if (StringUtils.equals(dtd.getTime(), "15:00")
            //                    && StringUtils.equals(dateString, dtd.getDate())) {
            //                    closingPrice = dtd.getClosingPrice();
            //                    DailyTradeData dailyTradeData = new DailyTradeData();
            //                    dailyTradeData.setStockCode(dtds.get(0).getStockCode());
            //                    dailyTradeData.setCurrentDate(DateUtil.parseSlashDate(dateString));
            //                    dailyTradeData.setOpeningPrice(openingPrice);
            //                    dailyTradeData.setClosingPrice(closingPrice);
            //                    dailyTradeData.setHighestPrice(highestPrice);
            //                    dailyTradeData.setLowestPrice(lowestPrice);
            //                    dailyTradeData.setStockDailyId(sequenceGenerator
            //                        .getSequence(SequenceEnum.STOCK_DAILY_SEQ));
            //                    try {
            //                        dailyTradeDAO.insert(dailyTradeData);
            //                    } catch (Exception e) {
            //                        System.out.println(openingPrice + " " + closingPrice + " " + highestPrice
            //                                           + " " + lowestPrice);
            //                        System.out.println(e);
            //                    }
            //                    dateString = null;
            //                    highestPrice = 0;
            //                    lowestPrice = 0;
            //                }
            //            }
        }
    }
}
