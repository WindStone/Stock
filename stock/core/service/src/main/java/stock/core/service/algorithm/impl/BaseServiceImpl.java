/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.service.algorithm.impl;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.util.PathUtil;

/**
 * @author yuanren.syr
 * @version $Id: BaseServiceImpl.java, v 0.1 2016/1/4 23:19 yuanren.syr Exp $
 */
public class BaseServiceImpl {

    protected static DailyTradeDAO dailyTradeDAO;

    public BaseServiceImpl() {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
    }
}
