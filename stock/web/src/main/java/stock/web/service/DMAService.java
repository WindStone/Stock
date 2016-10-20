/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service;

import java.util.Date;

import stock.common.dal.dataobject.DailyTradeData;
import stock.core.model.models.DMAValueTuple;

/**
 * @author yuanren.syr
 * @version $Id: DMAService.java, v 0.1 2016/1/31 23:56 yuanren.syr Exp $
 */
public interface DMAService extends AlgorithmService {

    DMAValueTuple getDMAValueTuple(String stockCode, Date date, DailyTradeData stdDtd);

}
