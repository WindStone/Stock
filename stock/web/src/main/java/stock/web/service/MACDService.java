/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service;

import java.util.Date;

import stock.common.dal.dataobject.DailyTradeData;
import stock.core.model.models.MACDValueTuple;

/**
 * @author yuanren.syr
 * @version $Id: MACDService.java, v 0.1 2016/1/4 23:12 yuanren.syr Exp $
 */
public interface MACDService extends AlgorithmService {

    MACDValueTuple getMACDValueTuple(String stockCode, Date date, DailyTradeData stdDtd);
}
