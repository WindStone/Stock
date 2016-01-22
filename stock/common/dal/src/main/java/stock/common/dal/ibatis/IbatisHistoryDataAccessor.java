/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.dataobject.DailyTradeData;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: IbatisHistoryDataAccessor.java, v 0.1 2015/12/5 23:23 yuanren.syr Exp $
 */
public class IbatisHistoryDataAccessor implements HistoryDataAccessor {
    public DailyTradeData getDailyPlateHistoryByName(String name, Date date) {
        return null;
    }

    public DailyTradeData getDailySHExp(Date date) {
        return null;
    }

    public DailyTradeData getDailySZExp(Date date) {
        return null;
    }

    public List<DailyTradeData> get5MinTrade(File f) {
        return null;
    }

    public DailyTradeData getDailyTradeDate(String stockCode, Date date) {
        return null;
    }

    public List<DailyTradeData> getDailyTradeData(File f) {
        return null;
    }
}
