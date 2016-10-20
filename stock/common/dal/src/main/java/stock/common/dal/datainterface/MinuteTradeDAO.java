/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import stock.common.dal.dataobject.MinuteTradeData;

/**
 * @author yuanren.syr
 * @version $Id: MinuteTradeDAO.java, v 0.1 2016/3/7 16:11 yuanren.syr Exp $
 */
public interface MinuteTradeDAO {

    void insert(MinuteTradeData miniteTradeData);
}
