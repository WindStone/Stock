/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.web.utils.RasingRateUtils;
import stock.web.view.CorrelationView;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: CorrelationController.java, v 0.1 2016/3/13 14:06 yuanren.syr Exp $
 */
@Controller
public class CorrelationController {

    private static final String startDate = "2015-01-01";

    private static final String endDate   = "2016-03-15";

    @Autowired
    private DailyTradeDAO       dailyTradeDAO;

    @RequestMapping(value = "/correlation.html")
    public String getCorrelation(Map<String, Object> model) {
        List<DailyTradeData> shExpDtds = dailyTradeDAO.queryByIntervalTradingData("SZ399106",
            startDate, endDate);
        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();

        List<CorrelationView> correlations = Lists.newArrayList();
        for (String stockCode : stockCodes) {
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }
            List<DailyTradeData> dtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                startDate, endDate);

            if (dtds.size() < 180) {
                continue;
            }

            List<Double> shRaisingRates = Lists.newArrayList();
            List<Double> raisingRates = Lists.newArrayList();
            for (int i = 1, j = 1; i < shExpDtds.size() && j < dtds.size();) {
                DailyTradeData shExpDtd = shExpDtds.get(i);
                DailyTradeData dtd = dtds.get(j);
                // 提前一天预判
                //                int diff = DateUtil.getDiffInDays(dtd.getCurrentDate(),
                //                    HolidayUtils.getPrevWorkDate(shExpDtd.getCurrentDate()));
                // 当天相关性
                int diff = DateUtil.getDiffInDays(dtd.getCurrentDate(), shExpDtd.getCurrentDate());
                if (diff == 0) {
                    raisingRates.add(RasingRateUtils.getRasingRate(dtds.get(j - 1), dtd));
                    shRaisingRates
                        .add(RasingRateUtils.getRasingRate(shExpDtds.get(i - 1), shExpDtd));
                    i++;
                    j++;
                } else if (diff < 0) {
                    j++;
                } else if (diff > 0) {
                    i++;
                }
            }

            CorrelationView correlationView = new CorrelationView();
            correlationView.setCorrelation(getCorrelation(raisingRates, shRaisingRates));
            correlationView.setStockCodeA("SH000001");
            correlationView.setStockNameA("上证指数");
            correlationView.setStockCodeB(stockCode);
            correlationView.setStockNameB(dtds.get(0).getStockName());
            correlationView.setPosibility(getPercent(raisingRates, shRaisingRates));
            correlationView.setCodeAValues(Lists.transform(shRaisingRates,
                new Function<Double, String>() {
                    public String apply(Double aDouble) {
                        return StringUtils.substringBefore(DecimalUtil.formatPercent(aDouble), "%");
                    }
                }));
            correlationView.setCodeBValues(Lists.transform(raisingRates,
                new Function<Double, String>() {
                    public String apply(Double aDouble) {
                        return StringUtils.substringBefore(DecimalUtil.formatPercent(aDouble), "%");
                    }
                }));
            correlations.add(correlationView);
        }
        Collections.sort(correlations, new Comparator<CorrelationView>() {
            public int compare(CorrelationView o1, CorrelationView o2) {
                return (int) ((o1.getCorrelation() - o2.getCorrelation()) * 100000);
            }
        });
        model.put("correlations", correlations);
        return "correlation";
    }

    private double getPercent(List<Double> raisingRates, List<Double> shRaisingRates) {
        int revert = 0;
        for (int i = 0; i < raisingRates.size(); i++) {
            if (raisingRates.get(i) > 0 && shRaisingRates.get(i) > 0) {
                revert++;
            }
            if (raisingRates.get(i) < 0 && shRaisingRates.get(i) < 0) {
                revert++;
            }
        }
        return (double) revert / raisingRates.size();
    }

    private double getCorrelation(List<Double> al, List<Double> bl) {
        double avgA = 0;
        for (double a : al) {
            avgA += a;
        }
        avgA /= al.size();

        double avgB = 0;
        for (double b : bl) {
            avgB += b;
        }
        avgB /= bl.size();

        double molecule = 0, varA = 0, varB = 0;
        for (int i = 0; i < al.size(); ++i) {
            molecule += (al.get(i) - avgA) * (bl.get(i) - avgB);
            varA += (al.get(i) - avgA) * (al.get(i) - avgA);
            varB += (bl.get(i) - avgB) * (bl.get(i) - avgB);
        }
        return molecule / Math.sqrt(varA) / Math.sqrt(varB);
    }
}
