/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.RecordRelationDAO;
import stock.common.dal.dataobject.RecordRelation;

/**
 * @author yuanren.syr
 * @version $Id: IbatisRecordRelationDAO.java, v 0.1 2016/2/25 16:18 yuanren.syr Exp $
 */
public class IbatisRecordRelationDAO extends SqlMapClientDaoSupport implements RecordRelationDAO {

    public List<RecordRelation> queryRecordRelationByFromId(String recordRelationType, String fromId) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("recordRelationType", recordRelationType);
        map.put("fromId", fromId);

        return getSqlMapClientTemplate().queryForList("MS-TRADE-DETAIL-RECORD-QUERY-BY-FROM-ID");
    }
}
