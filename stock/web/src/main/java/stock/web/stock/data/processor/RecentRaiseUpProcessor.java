/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor;

import java.util.List;
import java.util.Map;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.RasingRateUtils;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: RecentRaiseUpProcessor.java, v 0.1 2016/1/18 22:46 yuanren.syr Exp $
 */
public class RecentRaiseUpProcessor extends AbstractFollowedDataProcessor {

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(1, "前一次涨停时间"), new TitleLabel(1, "最近一次涨停时间"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        List<DailyTradeData> raiseUpDtds = RasingRateUtils.getLimitUpTradeDatas(dtd.getStockCode());
        if (raiseUpDtds == null) {
            return Lists.newArrayList(new ContentLabel(2, "无涨停"));
        }

        return Lists.newArrayList(buildContentLabel(raiseUpDtds, 2),
            buildContentLabel(raiseUpDtds, 1));
    }

    private ContentLabel buildContentLabel(List<DailyTradeData> raiseUpDtds, int lastIndex) {
        int size = raiseUpDtds.size();
        DailyTradeData dtd = raiseUpDtds.size() >= lastIndex ? raiseUpDtds.get(size - lastIndex)
            : null;

        if (dtd == null) {
            return new ContentLabel("无涨停");
        } else {
            return new ContentLabel(DateUtil.simpleFormat(dtd.getCurrentDate()));
        }
    }
}
