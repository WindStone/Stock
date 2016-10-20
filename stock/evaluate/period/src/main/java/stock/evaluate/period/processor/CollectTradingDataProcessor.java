/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.sal.model.CurrentTradeData;
import stock.common.sal.sina.SinaStockClient;
import stock.common.sal.sina.SinaStockClientImpl;
import stock.common.util.PathUtil;

/**
 * @author yuanren.syr
 * @version $Id: CollectTradingDataProcessor.java, v 0.1 2015/12/8 1:51 yuanren.syr Exp $
 */
public class CollectTradingDataProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")
                || StringUtils.startsWith(stockCode, "SZ300")) {
                CurrentTradeData ctd = sinaStockClient.getStock(stockCode);

                if (ctd == null) {
                    continue;
                }

                //                DailyTradeData dailyTradeData = new DailyTradeData();
                //                dailyTradeData.setCurrentDate(DateUtil.parseSimpleDate(ctd.getDate()));
                //                dailyTradeData.setStockCode(stockCode);
                //                dailyTradeData.setOpeningPrice(ctd.getOpenPrice());
                //                dailyTradeData.setClosingPrice(ctd.getCurrentPrice());
                //                dailyTradeData.setHighestPrice(ctd.getHighestPrice());
                //                dailyTradeData.setLowestPrice(ctd.getLowestPrice());
                //                dailyTradeData.setTradingVolume(ctd.getTradingVolumn());
                //                dailyTradeData.setTradingAmount(ctd.getTradingAmount());
                //                dailyTradeData.setStockDailyId(sequenceGenerator
                //                    .getSequence(SequenceEnum.STOCK_DAILY_SEQ));
                //                try {
                //                    dailyTradeDAO.updateDailyTradingData(stockCode, ctd.getStockName());
                //                } catch (Exception e) {
                //                    e.printStackTrace();
                //                }
            }
        }
    }
}
