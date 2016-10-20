/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.service.impl;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.web.config.AlgorithmConfig;
import stock.web.service.AlgorithmService;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanren.syr
 * @version $Id: AlgorithmServiceImpl.java, v 0.1 2016/1/4 23:19 yuanren.syr Exp $
 */
public abstract class AlgorithmServiceImpl<T extends AlgorithmConfig> implements AlgorithmService {

    protected static DailyTradeDAO dailyTradeDAO;

    protected T                    algorithmConfig;

    public String getParamConfig() {
        return JSON.toJSONString(algorithmConfig);
    }

    public void setAlgorithmConfig(T algorithmConfig) {
        this.algorithmConfig = algorithmConfig;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        AlgorithmServiceImpl.dailyTradeDAO = dailyTradeDAO;
    }
}
