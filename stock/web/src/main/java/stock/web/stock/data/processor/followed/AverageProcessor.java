/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

import com.google.common.collect.Lists;
import stock.web.stock.data.processor.followed.AbstractFollowedDataProcessor;

/**
 * @author yuanren.syr
 * @version $Id: AverageProcessor.java, v 0.1 2016/4/11 23:31 yuanren.syr Exp $
 */
public class AverageProcessor extends AbstractFollowedDataProcessor {

    private int length;

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(2, String.valueOf(length) + "日均线"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        List<DailyTradeData> dtds = dailyTradeDAO.queryByPrevKTradingData(dtd.getStockCode(),
            DateUtil.simpleFormat(dtd.getCurrentDate()), length);
        double avg = 0;
        if (CollectionUtils.isEmpty(dtds)) {
            return Lists.newArrayList(new ContentLabel(2, "没有数据"));
        }
        for (DailyTradeData eachDtd : dtds) {
            avg += eachDtd.getClosingPrice(dtd);
        }
        avg /= dtds.size();
        String percent = DecimalUtil.formatPercent((dtd.getClosingPrice(null) - avg) / avg);
        return Lists.newArrayList(new ContentLabel(1, DecimalUtil.formatDecimal(avg)),
            new ContentLabel(1, percent));
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
