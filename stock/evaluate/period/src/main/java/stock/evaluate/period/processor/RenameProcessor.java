/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

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
 * @version $Id: HistoryDataTransProcessor.java, v 0.1 2015/12/6 16:09 yuanren.syr Exp $
 */
public class RenameProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        String[] stockCodes = { "SH600651", "SH601333", "SH601998", "SH600630", "SH600690",
                "SH600713", "SH600323", "SH600383", "SH600619", "SH600637", "SH600650", "SH600594",
                "SH600648", "SH600779", "SH600021", "SH600280", "SH603701", "SH600803", "SH600818",
                "SH600101", "SH600288", "SH600004", "SH600128", "SH600289", "SH600316", "SH600720",
                "SH601007", "SH601226", "SH601688", "SH601766", "SH603000", "SH600797" };

        String[] stockNames = { "XD飞乐音", "XD广深铁", "XD中信银", "XD龙头股", "XD青岛海", "XD南京医", "XD瀚蓝环",
                "XD金地集", "XD海立股", "XD东方明", "XD锦江投", "XD益佰制", "XD外高桥", "XD水井坊", "XD上海电", "XD中央商",
                "XD德宏股", "XD新奥股", "XD中路股", "XD明星电", "XD大恒科", "XD白云机", "XD弘业股", "XD亿阳信", "XD洪都航",
                "XD祁连山", "XD金陵饭", "XD华电重", "XD华泰证", "XD中国中", "XD人民网", "XD浙大网" };

        for (int i = 0; i < stockCodes.length; ++i) {
            CurrentTradeData ctd = sinaStockClient.getStock(stockCodes[i]);
            System.out.println("update stock_daily_data set stock_name='" + ctd.getStockName()
                               + "' where stock_name='" + stockNames[i] + "';");
        }
    }
}
