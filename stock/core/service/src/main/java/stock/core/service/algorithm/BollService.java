/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.service.algorithm;

import java.util.Date;

import stock.core.model.models.BollValueTuple;

/**
 * @author yuanren.syr
 * @version $Id: BollService.java, v 0.1 2016/1/4 22:39 yuanren.syr Exp $
 */
public interface BollService {

    BollValueTuple getBollValueTuple(String stockCode, Date date);
}
