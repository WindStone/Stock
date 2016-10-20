/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DuplicateKeyException;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.datainterface.MinuteTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.dataobject.MinuteTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.sal.sina.SinaStockClient;
import stock.common.sal.sina.SinaStockClientImpl;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;

import com.google.common.collect.Maps;

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
        MinuteTradeDAO minuteTradeDAO = (MinuteTradeDAO) xmlBeanFactory.getBean("minuteTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        SinaStockClient sinaStockClient = new SinaStockClientImpl();

        File file = new File(PathUtil.getDataPath() + "/5min/" + "/201501-09");
        File[] files = file.listFiles();
        for (File f : files) {
            String fileName = StringUtils.substringAfterLast(f.getAbsolutePath(), File.separator);
            String stockCode = StringUtils.removeEnd(fileName, ".csv");
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }
            if (stockCode.compareTo("SZ300319") < 0) {
                continue;
            }
            List<DailyTradeData> dtds = historyDataAccessor.getDailyTradeData(f);
            double openingPrice = 0, closingPrice = 0;
            double highestPrice = 0, lowestPrice = 0;
            Map<Date, Double> factors = Maps.newHashMap();
            String stockName = null;
            for (DailyTradeData dtd : dtds) {
                if (factors.get(dtd.getCurrentDate()) == null) {
                    DailyTradeData factor = dailyTradeDAO.queryByStockCodeAndDate(
                        dtd.getStockCode(), dtd.getCurrentDate());
                    stockName = factor.getStockName();
                    stockCode = factor.getStockCode();
                    factors.put(factor.getCurrentDate(), factor.getWarrantFactor());
                }
                MinuteTradeData mtd = new MinuteTradeData();
                mtd.setStockMinuteId(sequenceGenerator.getSequence(SequenceEnum.STOCK_MINUTE_SEQ));
                mtd.setStockCode(dtd.getStockCode());
                mtd.setStockName(stockName);
                mtd.setSampleTime(DateUtil.parseDate(dtd.getDate() + " " + dtd.getTime()));
                mtd.setSampleInterval(5);
                mtd.setOpeningPrice(dtd.getOpeningPrice(null));
                mtd.setHighestPrice(dtd.getHighestPrice(null));
                mtd.setClosingPrice(dtd.getClosingPrice(null));
                mtd.setLowestPrice(dtd.getLowestPrice(null));
                mtd.setTradingVolume(dtd.getTradingVolume());
                mtd.setTradingAmount(dtd.getTradingAmount());
                mtd.setWarrantFactor(factors.get(dtd.getCurrentDate()));

                try {
                    minuteTradeDAO.insert(mtd);
                } catch (DuplicateKeyException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(stockCode + " Done!");
        }
    }
}
