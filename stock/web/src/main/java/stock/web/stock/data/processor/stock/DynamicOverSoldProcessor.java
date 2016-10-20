package stock.web.stock.data.processor.stock;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.CollectionUtils;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.dataobject.DynamicOversoldWrapper;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.context.StockContext;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;
import stock.web.utils.CollectionUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by songyuanren on 2016/7/24.
 */
public class DynamicOverSoldProcessor extends AbstractDataProcessor<DynamicOversoldWrapper> {

    private static final int SMOOTH_WINDOW = 4;

    private static final double OVERSOLD_PERCENT = 0.25;

    private static final int NEAREST_MONTH = 3;

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

    public DynamicOversoldWrapper processEach(String stockCode, Date currentDate,
                                              StockContext stockContext) {
        Date startDate = DateUtils.addMonths(currentDate, -NEAREST_MONTH);
        List<DailyTradeData> dtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                DateUtil.simpleFormat(startDate), DateUtil.simpleFormat(currentDate));
        if (CollectionUtils.isEmpty(dtds)) {
            return null;
        }
        DailyTradeData currentDtd = CollectionUtil.fetchLastElement(dtds);
        //        double sum = 0;
        //        for (int i = 0; i < SMOOTH_WINDOW * 2 + 1; ++i) {
        //            sum += dtds.get(i).getClosingPrice(currentDtd);
        //        }
        //        double maxAvg = 0;
        //        int maxIndex = -1, len = SMOOTH_WINDOW * 2 + 1;
        //        for (int i = SMOOTH_WINDOW; i < dtds.size() - SMOOTH_WINDOW - 1; ++i) {
        //            if (maxAvg < sum / len) {
        //                maxAvg = sum / len;
        //                maxIndex = i;
        //            }
        //            sum -= dtds.get(i - SMOOTH_WINDOW).getClosingPrice(currentDtd);
        //            sum += dtds.get(i + SMOOTH_WINDOW + 1).getClosingPrice(currentDtd);
        //        }

        double maxPrice = 0;
        int maxIndex = -1;
        for (int i = 0; i < dtds.size(); ++i) {
            if (maxPrice < dtds.get(i).getHighestPrice(currentDtd)) {
                maxPrice = dtds.get(i).getHighestPrice(currentDtd);
                maxIndex = i;
            }
        }
        double curPrice = currentDtd.getClosingPrice(null);
        double oversoldPrice = dtds.get(maxIndex).getHighestPrice(currentDtd);
        if (curPrice < (1 - OVERSOLD_PERCENT) * oversoldPrice) {
            DynamicOversoldWrapper wrapper = new DynamicOversoldWrapper(currentDtd);
            wrapper.setOversoldRate(1 - curPrice / oversoldPrice);
            wrapper.setHighestClosingPrice(maxPrice);
            wrapper.setHighestClosingDate(DateUtil
                    .simpleFormat(dtds.get(maxIndex).getCurrentDate()));
            return wrapper;
        }
        return null;
    }

    public List<DynamicOversoldWrapper> sortDatas(List<DynamicOversoldWrapper> originTradeDatas) {
        Collections.sort(originTradeDatas, new Comparator<DynamicOversoldWrapper>() {
            public int compare(DynamicOversoldWrapper dtd1, DynamicOversoldWrapper dtd2) {
                return (int) (dtd1.getOversoldRate() * 100000 - dtd2.getOversoldRate() * 100000);
            }
        });
        return originTradeDatas;
    }

    public List<TitleLabel> getSpecialTitleLabels() {
        List<TitleLabel> titleRow = Lists.newArrayList();
        titleRow.add(new TitleLabel(1, "最高点价格"));
        titleRow.add(new TitleLabel(1, "最高点日期"));
        titleRow.add(new TitleLabel(1, "超跌百分比"));
        return titleRow;
    }

    public List<ContentLabel> getSpecialContents(DynamicOversoldWrapper dailyTradeData) {
        List<ContentLabel> contentRow = Lists.newArrayList();
        contentRow.add(new ContentLabel(1, DecimalUtil.formatDecimal(dailyTradeData
                .getHighestClosingPrice())));
        contentRow.add(new ContentLabel(1, dailyTradeData.getHighestClosingDate()));
        contentRow.add(new ContentLabel(1, DecimalUtil.formatPercent(dailyTradeData
                .getOversoldRate())));
        return contentRow;
    }
}
