/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: HighestPriceProcessor.java, v 0.1 2016/1/17 17:26 yuanren.syr Exp $
 */
public class HighestPriceProcessor extends AbstractFollowedDataProcessor implements
                                                                        InitializingBean {

    private String titleName;

    private String startDate;

    private String endDate;

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(3, titleName + "最高点"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        List<DailyTradeData> dailyTradeDatas;

        if (StringUtils.isEmpty(endDate)) {
            dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(dtd.getStockCode(),
                startDate, DateUtil.simpleFormat(dtd.getCurrentDate()));
        } else {
            dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(dtd.getStockCode(),
                startDate, endDate);
        }
        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return Lists.newArrayList(new ContentLabel(3, "停牌"));
        }
        double highestPrice = 0;
        Date highestDate = null;
        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            if (highestPrice < dailyTradeData.getHighestPrice(dtd)) {
                highestDate = dailyTradeData.getCurrentDate();
                highestPrice = dailyTradeData.getHighestPrice(dtd);
            }
        }
        double rate = (dtd.getClosingPrice(dtd) - highestPrice) / highestPrice;

        return Lists.newArrayList(new ContentLabel(DateUtil.simpleFormat(highestDate)),
            new ContentLabel(DecimalUtil.formatDecimal(highestPrice)),
            new ContentLabel(DecimalUtil.formatPercent(rate)));
    }

    public String getTitleName() {
        return titleName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(startDate)) {
            startDate = DateUtil.simpleFormat(DateUtil.parseSimpleDate(startDate));
        }
        if (!StringUtils.isEmpty(endDate)) {
            endDate = DateUtil.simpleFormat(DateUtil.parseSimpleDate(endDate));
        }
    }
}
