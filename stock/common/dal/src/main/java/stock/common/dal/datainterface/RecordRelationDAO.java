/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.List;

import stock.common.dal.dataobject.RecordRelation;

/**
 * @author yuanren.syr
 * @version $Id: RecordRelationDAO.java, v 0.1 2016/2/25 16:17 yuanren.syr Exp $
 */
public interface RecordRelationDAO {

    List<RecordRelation> queryRecordRelationByFromId(String recordType, String fromId);
}
