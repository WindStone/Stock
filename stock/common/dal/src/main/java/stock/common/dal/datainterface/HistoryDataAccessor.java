/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.io.File;
import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;

/**
 *
 * @author yuanren.syr
 * @version $Id: HistoryDataAccessor.java, v 0.1 2015/11/17 23:41 yuanren.syr Exp $
 */
public interface HistoryDataAccessor {

    DailyTradeData getDailyPlateHistoryByName(String name, Date date);

    DailyTradeData getDailySHExp(Date date);

    DailyTradeData getDailySZExp(Date date);

    List<DailyTradeData> get5MinTrade(File f);

    DailyTradeData getDailyTradeDate(String stockCode, Date date);

    List<DailyTradeData> getDailyTradeData(File f);
}
