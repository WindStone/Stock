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

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.sal.model.CurrentTradeData;
import stock.common.sal.sina.SinaStockClient;
import stock.common.sal.sina.SinaStockClientImpl;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;
import stock.core.model.models.StockCodeGroup;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: LimitUpCalcProcessor.java, v 0.1 2015/12/8 22:53 yuanren.syr Exp $
 */
public class LimitUpCalcProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        List<StockCodeGroup> results = Lists.newArrayList();
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")) {
                List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
                    stockCode, "2015-10-01");
                List<Date> result = Lists.newArrayList();

                CurrentTradeData ctd = sinaStockClient.getStock(stockCode);

                if (ctd == null || StringUtils.contains(ctd.getStockName(), "ST")) {
                    continue;
                }
                if (ctd.getOpenPrice() == 0) {
                    continue;
                }

                for (int i = 0; i < dailyTradeDatas.size() - 1; ++i) {
                    DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                    DailyTradeData nextTradeData = dailyTradeDatas.get(i + 1);
                    double closingPrice = dailyTradeData.getClosingPrice(null);
                    double nextClosingPrice = nextTradeData.getClosingPrice(null);
                    if (((nextClosingPrice - closingPrice) / closingPrice) > 0.09) {
                        result.add(nextTradeData.getCurrentDate());
                    }
                }
                results.add(new StockCodeGroup(stockCode, ctd.getStockName(), result));
            }
        }

        Collections.sort(results, new Comparator<StockCodeGroup>() {
            public int compare(StockCodeGroup o1, StockCodeGroup o2) {
                return o2.getDates().size() - o1.getDates().size();
            }
        });
        for (StockCodeGroup stockCodeGroup : results) {
            if (stockCodeGroup.getDates().size() < 1) {
                continue;
            }
            System.out.println(stockCodeGroup.getStockCode() + ":" + stockCodeGroup.getStockName()
                               + " " + stockCodeGroup.getDates().size());
            for (Date date : stockCodeGroup.getDates()) {
                System.out.print(DateUtil.slashDateFormat(date) + " ");
            }
            System.out.println("");
        }
    }

}
