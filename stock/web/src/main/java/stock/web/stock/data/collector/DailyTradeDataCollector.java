/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.collector;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.sal.model.CurrentTradeData;
import stock.common.sal.sina.SinaStockClient;
import stock.common.util.DateUtil;

/**
 * @author yuanren.syr
 * @version $Id: DailyTradeDataCollector.java, v 0.1 2016/1/7 12:07 yuanren.syr Exp $
 */
public class DailyTradeDataCollector implements DataCollector {

    private DailyTradeDAO     dailyTradeDAO;

    private SequenceGenerator sequenceGenerator;

    private SinaStockClient   sinaStockClient;

    public void collectData() {
        for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
            if (StringUtils.startsWith(stockCode, "SZ00")
                || StringUtils.startsWith(stockCode, "SH60")
                || StringUtils.startsWith(stockCode, "SZ300")) {
                CurrentTradeData ctd = sinaStockClient.getStock(stockCode);

                if (ctd == null) {
                    continue;
                }
                if (ctd.getOpenPrice() == 0) {
                    continue;
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
                dailyTradeData.setStockDailyId(sequenceGenerator
                    .getSequence(SequenceEnum.STOCK_DAILY_SEQ));
                dailyTradeData.setStockName(ctd.getStockName());
                try {
                    dailyTradeDAO.insert(dailyTradeData);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        this.dailyTradeDAO = dailyTradeDAO;
    }

    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public void setSinaStockClient(SinaStockClient sinaStockClient) {
        this.sinaStockClient = sinaStockClient;
    }
}
