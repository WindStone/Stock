/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor;

import java.util.List;
import java.util.Map;

import stock.common.dal.dataobject.DailyTradeData;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: FollowedDataProcessor.java, v 0.1 2016/1/17 17:16 yuanren.syr Exp $
 */
public interface FollowedDataProcessor {

    /**
     * 获取第一行对应的标题
     * @param param
     * @return
     */
    List<TitleLabel> getTitle(Map<String, Object> param);

    /**
     * 获取后续处理值
     * @param dtd
     * @param param
     * @return
     */
    List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param);
}
