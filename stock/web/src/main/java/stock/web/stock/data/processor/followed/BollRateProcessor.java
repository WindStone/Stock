/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.followed;

import java.util.List;
import java.util.Map;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DecimalUtil;
import stock.core.model.models.BollValueTuple;
import stock.web.service.BollService;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

import com.google.common.collect.Lists;
import stock.web.stock.data.processor.followed.AbstractFollowedDataProcessor;

/**
 * @author yuanren.syr
 * @version $Id: BollRateProcessor.java, v 0.1 2016/2/10 15:11 yuanren.syr Exp $
 */
public class BollRateProcessor extends AbstractFollowedDataProcessor {

    private BollService bollService;

    public List<TitleLabel> getTitle(Map<String, Object> param) {
        return Lists.newArrayList(new TitleLabel(1, "Boll中线"), new TitleLabel(1, "收盘价/Boll中线"),
            new TitleLabel(1, "Boll差值/Boll中线"));
    }

    public List<ContentLabel> getFollowedDatas(DailyTradeData dtd, Map<String, Object> param) {
        BollValueTuple bollValueTuple = bollService.getBollValueTuple(dtd.getStockCode(),
            dtd.getCurrentDate(), dtd);
        String avgPrice = DecimalUtil.formatDecimal(bollValueTuple.getAvgPrice());
        String rate = DecimalUtil.formatPercent(dtd.getClosingPrice(dtd)
                                                / bollValueTuple.getAvgPrice());
        String diffRate = DecimalUtil.formatPercent((bollValueTuple.getAvgPrice() - bollValueTuple
            .getDownPrice()) / bollValueTuple.getAvgPrice());
        return Lists.newArrayList(new ContentLabel(avgPrice), new ContentLabel(rate),
            new ContentLabel(diffRate));
    }

    public void setBollService(BollService bollService) {
        this.bollService = bollService;
    }
}
