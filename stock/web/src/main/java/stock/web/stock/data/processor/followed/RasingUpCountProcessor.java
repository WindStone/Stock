/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Lists;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.RasingRateUtils;

/**
 * @author yuanren.syr
 * @version $Id: RasingUpCountProcessor.java, v 0.1 2016/2/17 23:31 yuanren.syr Exp $
 */
public class RasingUpCountProcessor extends AbstractFollowedDataProcessor implements
                                                                         InitializingBean {

    private String calcDate;

    private String titleName;

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(1, titleName + "涨停数量"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByLatestTradingData(
            dtd.getStockCode(), calcDate);
        int count = 0;
        for (int i = 1; i < dailyTradeDatas.size(); ++i) {
            if (RasingRateUtils.isRaisingUpLimit(RasingRateUtils.getRasingRate(
                dailyTradeDatas.get(i - 1), dailyTradeDatas.get(i)))) {
                count++;
            }
        }
        return Lists.newArrayList(new ContentLabel(String.valueOf(count)));
    }

    public String getCalcDate() {
        return calcDate;
    }

    public void setCalcDate(String calcDate) {
        this.calcDate = calcDate;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void afterPropertiesSet() throws Exception {
        calcDate = DateUtil.simpleFormat(DateUtil.parseSimpleDate(calcDate));
    }
}
