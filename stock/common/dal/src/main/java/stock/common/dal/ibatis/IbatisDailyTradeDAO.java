/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;

import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: IbatisDailyTradeDAO.java, v 0.1 2015/12/5 23:33 yuanren.syr Exp $
 */
public class IbatisDailyTradeDAO extends SqlMapClientDaoSupport implements DailyTradeDAO {
    public boolean insert(DailyTradeData dailyTradeData) {
        return getSqlMapClientTemplate().insert("MS-DAILY-TRADE-DATA-INSERT", dailyTradeData) != null;
    }

    public List<String> queryForStockCodes() {
        return getSqlMapClientTemplate().queryForList("MS-SELECT-STOCK-CODE");
    }

    public List<DailyTradeData> queryByIntervalTradingData(String stockCode, String startDate,
                                                           String endDate) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return getSqlMapClientTemplate().queryForList("MS-SELECT-BY-INTERVAL-TRADING-DATE", map);
    }

    public List<DailyTradeData> queryByLatestTradingData(String stockCode, String date) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("date", date);
        return getSqlMapClientTemplate().queryForList("MS-SELECT-BY-LATEST-TRADING-DATE", map);
    }

    public List<DailyTradeData> queryByPrevKTradingData(String stockCode, String date, int k) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("date", date);
        map.put("k", k);
        return getSqlMapClientTemplate().queryForList("MS-SELECT-PREV-K-TRADING-DATE", map);
    }

    public void updateDailyTradingData(String stockCode, String stockName) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("stockName", stockName);
        getSqlMapClientTemplate().update("MS-UPDATE-DAILY-TRADING-DATA", map);
    }
}
