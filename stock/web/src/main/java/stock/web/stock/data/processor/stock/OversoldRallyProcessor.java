/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.web.context.StockContext;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.CollectionUtil;

/**
 * @author yuanren.syr
 * @version $Id: OversoldRallyProcessor.java, v 0.1 2016/1/10 23:11 yuanren.syr Exp $
 */
public class OversoldRallyProcessor extends AbstractDataProcessor<DailyTradeData> {

    private String calcDate;

    public List<TitleLabel> getTitleLables() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "股票代码"));
        titleRow.add(new TitleLabel(1, "股票名称"));

        titleRow.add(new TitleLabel(1, "10.01最高点时间"));
        titleRow.add(new TitleLabel(1, "10.01最高点价格"));
        titleRow.add(new TitleLabel(1, "10.01最高点百分比"));
        titleRow.add(new TitleLabel(1, "当前价格"));
        titleRow.add(new TitleLabel(1, "当日涨幅"));
        titleRow.add(new TitleLabel(1, "前一次涨停"));
        titleRow.add(new TitleLabel(1, "最近一次涨停"));
        return titleRow;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        return null;
    }

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {
        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }

        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(stockCode,
            calcDate, DateUtil.simpleFormat(currentDate));

        DailyTradeData dtd = CollectionUtil.fetchLastElement(dailyTradeDatas);
        if (dtd == null || !DateUtils.isSameDay(dtd.getCurrentDate(), currentDate)) {
            return null;
        }

        String stockName = dailyTradeDatas.get(0).getStockName();
        if (StringUtils.contains(stockName, "ST")) {
            return null;
        }

        double highest = 0;
        for (int i = 0; i < dailyTradeDatas.size(); ++i) {
            DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
            if (dailyTradeData.getHighestPrice(dtd) > highest) {
                highest = dailyTradeData.getHighestPrice(dtd);
            }
        }

        if (!CollectionUtils.isEmpty(dailyTradeDatas) && dtd.getClosingPrice(dtd) <= 0.75 * highest) {
            return dtd;
        }
        return null;
    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        if (CollectionUtils.isEmpty(originTradeDatas)) {
            return null;
        }
        final Map<String, Double> map = Maps.newHashMap();
        for (DailyTradeData dtd : originTradeDatas) {
            List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(
                dtd.getStockCode(), calcDate, DateUtil.simpleFormat(dtd.getCurrentDate()));
            DailyTradeData stdDtd = CollectionUtil.fetchLastElement(dailyTradeDatas);
            double highest = 0;
            for (int i = 0; i < dailyTradeDatas.size(); ++i) {
                DailyTradeData dailyTradeData = dailyTradeDatas.get(i);
                if (dailyTradeData.getHighestPrice(stdDtd) > highest) {
                    highest = dailyTradeData.getHighestPrice(stdDtd);
                }
            }
            map.put(dtd.getStockCode(), dtd.getClosingPrice(stdDtd) / highest);
        }
        Collections.sort(originTradeDatas, new Comparator<DailyTradeData>() {
            public int compare(DailyTradeData o1, DailyTradeData o2) {
                return (int) (map.get(o1.getStockCode()) * 1000000 - map.get(o2.getStockCode()) * 1000000);
            }
        });

        return originTradeDatas;
    }

    public List<TitleLabel> getSpecialTitleLabels() {
        return null;
    }

    public List<ContentLabel> getSpecialContents(DailyTradeData dailyTradeData) {
        return null;
    }

    public void setCalcDate(String calcDate) {
        this.calcDate = calcDate;
    }
}
