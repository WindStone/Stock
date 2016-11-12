/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: IbatisDailyTradeDAO.java, v 0.1 2015/12/5 23:33 yuanren.syr Exp $
 */
public class IbatisDailyTradeDAO extends SqlSessionDaoSupport implements DailyTradeDAO {

    public boolean insert(DailyTradeData dailyTradeData) {
        return getSqlSession().insert("MS-DAILY-TRADE-DATA-INSERT", dailyTradeData) > 0;
    }

    public List<String> queryForStockCodes() {
        Map<String, String> dtds = getSqlSession()
                .selectMap("MS-SELECT-STOCK-CODE", "code");
        return Lists.newArrayList(dtds.keySet());
    }

    public Map<String, String> queryForStockCodesAndNames() {
        return getSqlSession().selectMap("MS-SELECT-STOCK-CODE", "code");
    }

    public List<DailyTradeData> queryByIntervalTradingData(String stockCode, String startDate,
                                                           String endDate) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return getSqlSession().selectList("MS-SELECT-BY-INTERVAL-TRADING-DATE", map);
    }

    public List<DailyTradeData> queryByLatestTradingData(String stockCode, String date) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("date", date);
        return getSqlSession().selectList("MS-SELECT-BY-LATEST-TRADING-DATE", map);
    }

    public List<DailyTradeData> queryByPrevKTradingData(String stockCode, String date, int k) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("date", date);
        map.put("k", k);
        return getSqlSession().selectList("MS-SELECT-PREV-K-TRADING-DATE", map);
    }

    public DailyTradeData queryByStockCodeAndDate(String stockCode, Date date) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("stockCode", stockCode);
        map.put("date", DateUtil.simpleFormat(date));
        return (DailyTradeData) getSqlSession().selectOne(
                "MS-SELECT-BY-STOCK-CODE-AND-DATE", map);
    }

    public int updateDailyTradingData(DailyTradeData dailyTradeData) {
        return getSqlSession().update("MS-UPDATE-DAILY-TRADING-DATA", dailyTradeData);
    }
}
