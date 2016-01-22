/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.sal.sina;

import stock.common.sal.model.CurrentTradeData;

/**
 * @author yuanren.syr
 * @version $Id: SinaStockClient.java, v 0.1 2015/12/8 0:25 yuanren.syr Exp $
 */
public interface SinaStockClient {
    CurrentTradeData getStock(String stockCode);
}
