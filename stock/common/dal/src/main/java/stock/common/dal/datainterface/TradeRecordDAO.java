/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.List;

import stock.common.dal.dataobject.TradeRecord;

/**
 * @author yuanren.syr
 * @version $Id: TradeRecordDAO.java, v 0.1 2016/2/25 15:10 yuanren.syr Exp $
 */
public interface TradeRecordDAO {

    public List<TradeRecord> queryTradeRecordsByDocId(String relationType, String docId);
}
