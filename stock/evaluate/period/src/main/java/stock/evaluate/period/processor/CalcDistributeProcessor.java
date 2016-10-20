/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.util.List;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.PathUtil;

/**
 * @author yuanren.syr
 * @version $Id: CalcDistributeProcessor.java, v 0.1 2016/2/21 17:18 yuanren.syr Exp $
 */
public class CalcDistributeProcessor {

    public static final void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        List<DailyTradeData> dtds = dailyTradeDAO
            .queryByLatestTradingData("SZ300135", "2015-06-30");

    }
}
