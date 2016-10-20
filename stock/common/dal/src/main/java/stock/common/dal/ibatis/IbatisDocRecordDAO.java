/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import stock.common.dal.datainterface.DocRecordDAO;
import stock.common.dal.dataobject.DocRecord;

/**
 * @author yuanren.syr
 * @version $Id: IbatisDocRecordDAO.java, v 0.1 2016/2/25 16:24 yuanren.syr Exp $
 */
public class IbatisDocRecordDAO extends SqlMapClientDaoSupport implements DocRecordDAO {

    public List<DocRecord> queryAllDocRecords() {
        return getSqlMapClientTemplate().queryForList("MS-TRADE-DETAIL-RECORD-QUERY-ALL");
    }

    public DocRecord queryDocRecordById(String docId) {
        return (DocRecord) getSqlMapClientTemplate().queryForObject(
            "MS-TRADE-DETAIL-RECORD-QUERY-BY-ID", docId);
    }
}
