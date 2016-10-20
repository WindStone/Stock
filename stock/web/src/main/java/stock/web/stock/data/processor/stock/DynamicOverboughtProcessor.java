package stock.web.stock.data.processor.stock;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.dataobject.DynamicOverboughtWrapper;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.context.StockContext;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.CollectionUtil;

/**
 * Created by songyuanren on 2016/8/16.
 */
public class DynamicOverboughtProcessor extends AbstractDataProcessor<DynamicOverboughtWrapper> {

    private static final double OVERBOUGHT_PERCENT = 0.33;

    private static final int    NEAREST_MONTH      = 2;

    public List<TitleLabel> getTitleLables() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "股票代码"));
        titleRow.add(new TitleLabel(1, "股票名称"));

        titleRow.add(new TitleLabel(1, "当前价格"));
        titleRow.add(new TitleLabel(1, "当日涨幅"));

        return titleRow;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        return null;
    }

    public DynamicOverboughtWrapper processEach(String stockCode, Date currentDate,
                                                StockContext stockContext) {
        if (!StringUtils.startsWith(stockCode, "SZ00")
            && !StringUtils.startsWith(stockCode, "SH60")
            && !StringUtils.startsWith(stockCode, "SZ300")) {
            return null;
        }

        Date startDate = DateUtils.addMonths(currentDate, -NEAREST_MONTH);
        List<DailyTradeData> dtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
            DateUtil.simpleFormat(startDate), DateUtil.simpleFormat(currentDate));
        if (CollectionUtils.isEmpty(dtds)) {
            return null;
        }
        DailyTradeData currentDtd = CollectionUtil.fetchLastElement(dtds);

        double minPrice = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < dtds.size(); ++i) {
            if (minPrice > dtds.get(i).getLowestPrice(currentDtd)) {
                minPrice = dtds.get(i).getLowestPrice(currentDtd);
                minIndex = i;
            }
        }
        double curPrice = currentDtd.getClosingPrice(null);
        double oversoldPrice = dtds.get(minIndex).getLowestPrice(currentDtd);
        if (curPrice >= (1 + OVERBOUGHT_PERCENT) * oversoldPrice) {
            DynamicOverboughtWrapper wrapper = new DynamicOverboughtWrapper(currentDtd);
            wrapper.setOverboughtRate(curPrice / oversoldPrice - 1);
            wrapper.setLowestClosingPrice(minPrice);
            wrapper
                .setLowestClosingDate(DateUtil.simpleFormat(dtds.get(minIndex).getCurrentDate()));
            return wrapper;
        }
        return null;
    }

    public List<DynamicOverboughtWrapper> sortDatas(List<DynamicOverboughtWrapper> originTradeDatas) {
        Collections.sort(originTradeDatas, new Comparator<DynamicOverboughtWrapper>() {
            public int compare(DynamicOverboughtWrapper dtd1, DynamicOverboughtWrapper dtd2) {
                return (int) (dtd1.getOverboughtRate() * 100000 - dtd2.getOverboughtRate() * 100000);
            }
        });
        return originTradeDatas;
    }

    public List<TitleLabel> getSpecialTitleLabels() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "最低点价格"));
        titleRow.add(new TitleLabel(1, "最低点日期"));
        titleRow.add(new TitleLabel(1, "超胀百分比百分比"));
        return titleRow;
    }

    public List<ContentLabel> getSpecialContents(DynamicOverboughtWrapper dailyTradeData) {
        List<ContentLabel> contentRow = Lists.newArrayList();
        contentRow.add(new ContentLabel(1, DecimalUtil.formatDecimal(dailyTradeData
            .getLowestClosingPrice())));
        contentRow.add(new ContentLabel(1, dailyTradeData.getLowestClosingDate()));
        contentRow.add(new ContentLabel(1, DecimalUtil.formatPercent(dailyTradeData
            .getOverboughtRate())));
        return contentRow;
    }

}
