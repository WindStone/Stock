/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import stock.common.dal.datainterface.DailyTradeDAO;

/**
 * @author yuanren.syr
 * @version $Id: AbstractProcessWorker.java, v 0.1 2016/2/16 17:17 yuanren.syr Exp $
 */
public abstract class AbstractProcessWorker implements ProcessWorker {

    private boolean              finished = false;

    private static DailyTradeDAO dailyTradeDAO;

    public void work() {
        execute();
        finished = true;
        ProcessQueue.pollToWork();
    }

    public boolean isFinished() {
        return finished;
    }

    protected List<String> getStockCodes() {
        List<String> originalStockCodes = dailyTradeDAO.queryForStockCodes();
        List<String> stockCodes = Lists.newArrayList();
        for (String stockCode : originalStockCodes) {
            if (StringUtils.startsWith(stockCode, "SH60")
                || StringUtils.startsWith(stockCode, "SZ300")
                || StringUtils.startsWith(stockCode, "SZ00")) {
                stockCodes.add(stockCode);
            }
        }
        return stockCodes;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        AbstractProcessWorker.dailyTradeDAO = dailyTradeDAO;
    }

    protected abstract void execute();
}
