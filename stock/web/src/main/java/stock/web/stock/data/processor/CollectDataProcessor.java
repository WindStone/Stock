/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.sal.model.CurrentTradeData;
import stock.common.util.DateUtil;
import stock.web.context.StockContext;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: CollectDataProcessor.java, v 0.1 2016/1/18 23:50 yuanren.syr Exp $
 */
public class CollectDataProcessor extends AbstractDataProcessor {
    public List<TitleLabel> getTitleLables() {
        return null;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        return null;
    }

    public List<List<String>> forcast(Date currentDate) {
        return null;
    }

    public List<List<String>> evaluate(Date prevDate) {
        return null;
    }

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {

        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }
        CurrentTradeData ctd = sinaStockClient.getStock(stockCode);

        if (ctd == null) {
            return null;
        }
        if (ctd.getOpenPrice() == 0) {
            return null;
        }

        DailyTradeData dailyTradeData = new DailyTradeData();
        dailyTradeData.setCurrentDate(DateUtil.parseSimpleDate(ctd.getDate()));
        dailyTradeData.setStockCode(stockCode);
        dailyTradeData.setOpeningPrice(ctd.getOpenPrice());
        dailyTradeData.setClosingPrice(ctd.getCurrentPrice());
        dailyTradeData.setHighestPrice(ctd.getHighestPrice());
        dailyTradeData.setLowestPrice(ctd.getLowestPrice());
        dailyTradeData.setTradingVolume(ctd.getTradingVolumn());
        dailyTradeData.setTradingAmount(ctd.getTradingAmount());
        dailyTradeData.setStockDailyId(sequenceGenerator.getSequence(SequenceEnum.STOCK_DAILY_SEQ));
        dailyTradeData.setStockName(ctd.getStockName());
        try {
            dailyTradeDAO.insert(dailyTradeData);
        } catch (Exception e) {
            System.out.println(e);
        }
        return dailyTradeData;

    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        return null;
    }
}
