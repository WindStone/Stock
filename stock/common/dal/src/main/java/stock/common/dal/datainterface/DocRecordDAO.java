/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.List;

import stock.common.dal.dataobject.DocRecord;

/**
 * @author yuanren.syr
 * @version $Id: DocRecordDAO.java, v 0.1 2016/2/25 16:24 yuanren.syr Exp $
 */
public interface DocRecordDAO {

    DocRecord queryDocRecordById(String docId);

    List<DocRecord> queryAllDocRecords();
}
