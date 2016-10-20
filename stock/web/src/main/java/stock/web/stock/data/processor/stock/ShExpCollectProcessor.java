/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.util.DateUtil;
import stock.web.context.StockContext;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: ShExpCollectProcessor.java, v 0.1 2016/2/16 16:56 yuanren.syr Exp $
 */
public class ShExpCollectProcessor extends AbstractDataProcessor<DailyTradeData> {
    public List<TitleLabel> getTitleLables() {
        return null;
    }

    public List<TitleLabel> getForcasTitleLabels() {
        return null;
    }

    private ThreadLocal<Boolean> hasCollected = new ThreadLocal<Boolean>();

    public DailyTradeData processEach(String stockCode, Date currentDate, StockContext stockContext) {
        if (hasCollected.get() == null || !hasCollected.get()) {
            try {
                collectData("SH000001", "上证指数", currentDate);
                collectData("SZ399106", "深圳综指", currentDate);
                hasCollected.set(true);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(500);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<DailyTradeData> sortDatas(List<DailyTradeData> originTradeDatas) {
        hasCollected.set(false);
        return null;
    }

    public List<TitleLabel> getSpecialTitleLabels() {
        return null;
    }

    public List<ContentLabel> getSpecialContents(DailyTradeData dailyTradeData) {
        return null;
    }

    private void collectData(String stockCode, String stockName, Date currentDate) throws Exception {
        URL url = new URL("http://hq.sinajs.cn/list=" + StringUtils.lowerCase(stockCode));
        URLConnection conn = url.openConnection();
        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
            "gb2312"));
        StringBuffer sb = new StringBuffer();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            sb.append(line);
        }
        String result = StringUtils.substringAfter(sb.toString(), "\"");
        result = StringUtils.substringBefore(result, "\"");
        String[] splits = StringUtils.split(result, ",");
        DailyTradeData dtd = new DailyTradeData();
        dtd.setStockCode(StringUtils.upperCase(stockCode));
        dtd.setStockName(stockName);
        dtd.setDate(DateUtil.simpleFormat(currentDate));
        dtd.setCurrentDate(currentDate);
        dtd.setOpeningPrice(Double.parseDouble(splits[1]));

        dtd.setClosingPrice(Double.parseDouble(splits[3]));
        dtd.setHighestPrice(Double.parseDouble(splits[4]));
        dtd.setLowestPrice(Double.parseDouble(splits[5]));
        dtd.setTradingVolume(Double.parseDouble(splits[8]));
        try {
            dtd.setTradingAmount(Double.parseDouble(splits[9]));
        } catch (NumberFormatException e) {
            dtd.setTradingAmount(-1);
        }
        dtd.setStockDailyId(sequenceGenerator.getSequence(SequenceEnum.STOCK_DAILY_SEQ));
        try {
            dailyTradeDAO.insert(dtd);
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }

    private double parseDouble(String doubleStr) {
        return Double.parseDouble(StringUtils.remove(doubleStr, ","));
    }
}
