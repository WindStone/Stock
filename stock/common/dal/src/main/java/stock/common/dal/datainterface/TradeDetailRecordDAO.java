/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.TradeDetailRecord;

/**
 * @author yuanren.syr
 * @version $Id: TradeDetailRecordDAO.java, v 0.1 2016/2/22 0:06 yuanren.syr Exp $
 */
public interface TradeDetailRecordDAO {

    List<TradeDetailRecord> queryTradeDetailRecordByInterval(Date startDate, Date endDate);

    List<TradeDetailRecord> queryTradeDetailRecordByTradeId(String tradeRecordId);
}
