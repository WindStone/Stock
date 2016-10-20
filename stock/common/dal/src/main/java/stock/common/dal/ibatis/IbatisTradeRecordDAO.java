/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.TradeRecordDAO;
import stock.common.dal.dataobject.TradeRecord;

/**
 * @author yuanren.syr
 * @version $Id: IbatisTradeRecordDAO.java, v 0.1 2016/2/25 15:12 yuanren.syr Exp $
 */
public class IbatisTradeRecordDAO extends SqlMapClientDaoSupport implements TradeRecordDAO {

    public List<TradeRecord> queryTradeRecordsByDocId(String relationType, String docId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("relationType", relationType);
        map.put("docId", docId);

        return getSqlMapClientTemplate().queryForList("MS-TRADE-RECORD-QUERY-BY-DOC-ID", map);
    }
}
