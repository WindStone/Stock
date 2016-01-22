/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;

/**
 * @author yuanren.syr
 * @version $Id: DailyTradeDAO.java, v 0.1 2015/12/5 23:24 yuanren.syr Exp $
 */
public interface DailyTradeDAO {

    boolean insert(DailyTradeData dailyTradeData);

    List<String> queryForStockCodes();

    List<DailyTradeData> queryByIntervalTradingData(String stockCode, String startDate,
                                                    String endDate);

    List<DailyTradeData> queryByLatestTradingData(String stockCode, String date);

    List<DailyTradeData> queryByPrevKTradingData(String stockCode, String date, int k);

    void updateDailyTradingData(String stockCode, String stockName);
}
