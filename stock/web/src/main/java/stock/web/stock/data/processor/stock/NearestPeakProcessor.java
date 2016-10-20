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
import stock.web.utils.RasingRateUtils;

/**
 * @author yuanren.syr
 * @version $Id: NearestPeakProcessor.java, v 0.1 2016/1/7 11:59 yuanren.syr Exp $
 */
public class NearestPeakProcessor extends AbstractDataProcessor<DailyTradeData> {

    private String calcDate;

    public List<TitleLabel> getTitleLables() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "股票代码"));
        titleRow.add(new TitleLabel(1, "股票名称"));
        titleRow.add(new TitleLabel(2, "6月份最高点"));
        titleRow.add(new TitleLabel(1, "当前股价到最高点值"));
        titleRow.add(new TitleLabel(2, "7月7日后最高点"));
        titleRow.add(new TitleLabel(2, "10月01日之后最高点"));
        titleRow.add(new TitleLabel(1, "当前价格"));
        titleRow.add(new TitleLabel(1, "当天涨幅"));
        titleRow.add(new TitleLabel(1, "成交量(股)"));
        titleRow.add(new TitleLabel(1, "成交金额(元)"));
        titleRow.add(new TitleLabel(1, "前一次涨停"));
        titleRow.add(new TitleLabel(1, "最近一次涨停"));
        return titleRow;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        List<TitleLabel> forcastLabelRow = Lists.newArrayList();
        forcastLabelRow.addAll(getTitleLables());
        forcastLabelRow.add(new TitleLabel(1, "涨幅"));
        forcastLabelRow.add(new TitleLabel(1, "是否涨停"));
        return forcastLabelRow;
    }

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {
        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }

        // 1. 查询从2015-10-01到currentDate之间的数据
        List<DailyTradeData> dailyTradeDatas = dailyTradeDAO.queryByIntervalTradingData(stockCode,
            calcDate, DateUtil.simpleFormat(currentDate));
        if (CollectionUtils.isEmpty(dailyTradeDatas)) {
            return null;
        }
        double highest = 0;

        // 2. 取最高值并且取当前交易日的数据
        DailyTradeData curDtd = CollectionUtil.fetchLastElement(dailyTradeDatas);
        if (!DateUtils.isSameDay(curDtd.getCurrentDate(), currentDate)) {
            return null;
        }

        for (DailyTradeData dailyTradeData : dailyTradeDatas) {
            if (dailyTradeData.getHighestPrice(curDtd) > highest) {
                highest = dailyTradeData.getHighestPrice(curDtd);
            }
        }

        // 3.2 stockName为st开头的返回false
        if (StringUtils.contains(curDtd.getStockName(), "ST")) {
            return null;
        }

        // 4. 接近最高点90%时返回true
        if (curDtd.getClosingPrice(curDtd) >= 0.9 * highest) {
            return curDtd;
        }

        return null;
    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        final Map<String, DailyTradeData> map = Maps.newHashMap();
        for (DailyTradeData originTradeData : originTradeDatas) {
            String stockCode = originTradeData.getStockCode();
            List<DailyTradeData> dtds = RasingRateUtils.getLimitUpTradeDatas(stockCode);
            map.put(stockCode, CollectionUtil.fetchLastElement(dtds));
        }
        Collections.sort(originTradeDatas, new Comparator<DailyTradeData>() {
            public int compare(DailyTradeData o1, DailyTradeData o2) {
                DailyTradeData dtd1 = map.get(o1.getStockCode());
                DailyTradeData dtd2 = map.get(o2.getStockCode());
                if (dtd1 == null && dtd2 == null) {
                    return 0;
                }
                if (dtd1 == null) {
                    return -1;
                } else if (dtd2 == null) {
                    return 1;
                }
                return DateUtil.getDiffInDays(map.get(o1.getStockCode()).getCurrentDate(),
                    map.get(o2.getStockCode()).getCurrentDate());
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
