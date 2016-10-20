/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service;

import java.util.Date;

import stock.common.dal.dataobject.DailyTradeData;
import stock.core.model.models.BollValueTuple;

/**
 * @author yuanren.syr
 * @version $Id: BollService.java, v 0.1 2016/1/4 22:39 yuanren.syr Exp $
 */
public interface BollService extends AlgorithmService {

    BollValueTuple getBollValueTuple(String stockCode, Date date, DailyTradeData stdDtd);
}
