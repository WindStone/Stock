/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import stock.common.dal.datainterface.DailyTradeDAO;

/**
 * @author yuanren.syr
 * @version $Id: AbstractFollowedDataProcessor.java, v 0.1 2016/1/17 17:30 yuanren.syr Exp $
 */
public abstract class AbstractFollowedDataProcessor implements FollowedDataProcessor {

    protected DailyTradeDAO dailyTradeDAO;

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        this.dailyTradeDAO = dailyTradeDAO;
    }
}
