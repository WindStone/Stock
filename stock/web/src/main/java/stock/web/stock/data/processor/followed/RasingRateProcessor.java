/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import java.util.Date;
import java.util.List;
import java.util.Map;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DecimalUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.stock.data.processor.followed.AbstractFollowedDataProcessor;
import stock.web.utils.RasingRateUtils;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: RasingRateProcessor.java, v 0.1 2016/1/20 23:28 yuanren.syr Exp $
 */
public class RasingRateProcessor extends AbstractFollowedDataProcessor {

    private String       titleName;

    public static String CALC_DAY = "CALC_DAY";

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(2, titleName + "涨幅"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        Date calcDate = (Date) param.get(CALC_DAY);
        double raisingRate = RasingRateUtils.getRasingRate(dtd.getStockCode(), calcDate);
        ContentLabel contentLabel = new ContentLabel(DecimalUtil.formatPercent(raisingRate));
        if (RasingRateUtils.isRaisingUpLimit(raisingRate)) {
            return Lists.newArrayList(contentLabel, new ContentLabel("*"));
        } else {
            return Lists.newArrayList(contentLabel, new ContentLabel(""));
        }
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
