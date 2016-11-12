/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.ibatis;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import stock.common.dal.datainterface.DocRecordDAO;
import stock.common.dal.dataobject.DocRecord;

import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: IbatisDocRecordDAO.java, v 0.1 2016/2/25 16:24 yuanren.syr Exp $
 */
public class IbatisDocRecordDAO extends SqlSessionDaoSupport implements DocRecordDAO {

    public List<DocRecord> queryAllDocRecords() {
        return getSqlSession().selectList("MS-TRADE-DETAIL-RECORD-QUERY-ALL");
    }

    public DocRecord queryDocRecordById(String docId) {
        return (DocRecord) getSqlSession().selectOne(
                "MS-TRADE-DETAIL-RECORD-QUERY-BY-ID", docId);
    }
}
