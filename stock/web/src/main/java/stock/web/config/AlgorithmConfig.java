/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.config;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanren.syr
 * @version $Id: AlgorithmConfig.java, v 0.1 2016/2/2 17:21 yuanren.syr Exp $
 */
public abstract class AlgorithmConfig {

    public String toString() {
        return JSON.toJSONString(this);
    }
}
