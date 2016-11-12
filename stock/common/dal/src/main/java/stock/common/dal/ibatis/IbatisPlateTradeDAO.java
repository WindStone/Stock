/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.PlateTradeDAO;
import stock.common.dal.dataobject.DailyPlateData;
import stock.common.util.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: IbatisPlateTradeDAO.java, v 0.1 2016/2/2 0:17 yuanren.syr Exp $
 */
public class IbatisPlateTradeDAO extends SqlSessionDaoSupport implements PlateTradeDAO {

    public boolean insert(DailyPlateData dailyPlateData) {
        return getSqlSession().insert("MS-DAILY-PLATE-DATA-INSERT", dailyPlateData) > 0;
    }

    public DailyPlateData queryByNameAndDate(String plateName, String processName, Date calcDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("plateName", plateName);
        map.put("processName", processName);
        map.put("tradingDate", DateUtil.simpleFormat(calcDate));
        return (DailyPlateData) getSqlSession().selectOne(
                "MS-DAILY-PLATE-DATA-QUERY-BY-NAME-AND-DATE", map);
    }

    public List<DailyPlateData> queryIntervalByNameAndDate(String plateName, String processName,
                                                           Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("plateName", plateName);
        map.put("processName", processName);
        map.put("startDate", DateUtil.simpleFormat(startDate));
        map.put("endDate", DateUtil.simpleFormat(endDate));
        return getSqlSession().selectList(
                "MS-DAILY-PLATE-DATA-QUERY-INTERVAL-BY-NAME-AND-DATE", map);
    }

    public int update(DailyPlateData dailyPlateData) {
        return getSqlSession().update("MS-DAILY-PLATE-DATA-UPDATE", dailyPlateData);
    }
}
