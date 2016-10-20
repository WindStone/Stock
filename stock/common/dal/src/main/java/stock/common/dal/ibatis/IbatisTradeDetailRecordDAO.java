/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.*;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.TradeDetailRecordDAO;
import stock.common.dal.dataobject.TradeDetailRecord;
import stock.common.util.DateUtil;

/**
 * @author yuanren.syr
 * @version $Id: IbatisTradeDetailRecordDAO.java, v 0.1 2016/2/22 0:09 yuanren.syr Exp $
 */
public class IbatisTradeDetailRecordDAO extends SqlMapClientDaoSupport implements
                                                                      TradeDetailRecordDAO {

    public List<TradeDetailRecord> queryTradeDetailRecordByInterval(Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar calendar = Calendar.getInstance();
        if (startDate != null) {
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR, 9);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            startDate = calendar.getTime();
            map.put("startDate", DateUtil.dateFormat(startDate));
        }
        if (endDate != null) {
            calendar.setTime(endDate);
            calendar.set(Calendar.HOUR, 15);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            endDate = calendar.getTime();
            map.put("endDate", DateUtil.dateFormat(endDate));
        }
        return getSqlMapClientTemplate().queryForList("MS-TRADE-DETAIL-RECORD-QUERY-BY-INTERVAL",
            map);
    }

    public List<TradeDetailRecord> queryTradeDetailRecordByTradeId(String tradeRecordId) {
        return getSqlMapClientTemplate().queryForList("MS-TRADE-DETAIL-RECORD-QUERY-BY-TRADE-ID",
            tradeRecordId);
    }
}
