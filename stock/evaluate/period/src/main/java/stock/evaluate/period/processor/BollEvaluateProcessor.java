/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.sal.sina.SinaStockClient;
import stock.common.sal.sina.SinaStockClientImpl;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.PathUtil;
import stock.core.model.models.BollValueGroup;
import stock.core.model.models.BollValueTuple;

/**
 * @author yuanren.syr
 * @version $Id: BollForecastProcessor.java, v 0.1 2015/12/17 23:14 yuanren.syr Exp $
 */
public class BollEvaluateProcessor {

    private static SinaStockClient sinaStockClient = new SinaStockClientImpl();

    private static DailyTradeDAO   dailyTradeDAO;

    private static String          processDate     = "2015-11-09";

    private static int             N               = 15;

    private static int             K               = 3;

    private static int             WINDOW_SIZE     = 3;

    public static void main(String[] args) throws IOException {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        List<BollValueGroup> group = Lists.newArrayList();
        FileOutputStream fos = new FileOutputStream(
            "C:\\Users\\yuanren.syr\\Desktop\\股票数据\\output2.txt");
        for (Date startDate = DateUtil.parseSimpleDate("2015-11-01"); !DateUtils.isSameDay(
            startDate, DateUtil.parseSimpleDate("2015-12-18")); startDate = DateUtils.addDays(
            startDate, 1)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                continue;
            }
            processDate = DateUtil.simpleFormat(startDate);
            for (String stockCode : dailyTradeDAO.queryForStockCodes()) {
                if (StringUtils.startsWith(stockCode, "SZ00")
                    || StringUtils.startsWith(stockCode, "SH60")) {
                    List<DailyTradeData> dtds = dailyTradeDAO.queryByPrevKTradingData(stockCode,
                        processDate, N + WINDOW_SIZE + 1);
                    if (CollectionUtils.isEmpty(dtds) || dtds.size() < N + WINDOW_SIZE + 1) {
                        continue;
                    }
                    DailyTradeData curDtd = dtds.get(0);
                    DailyTradeData prevDtd = null;
                    boolean isEvalute = false;
                    if (StringUtils.equals(processDate,
                        DateUtil.simpleFormat(curDtd.getCurrentDate()))) {
                        isEvalute = true;
                        prevDtd = dtds.get(1);
                    } else {
                        prevDtd = curDtd;
                        curDtd = null;
                    }
                    // 带有当天数据那么从第1天算到第N天，否则会多取出一天数据，从第0天算到第N-1天
                    List<BollValueTuple> bollValueTuples = Lists.newArrayList();
                    int startIndex = isEvalute ? 1 : 0;
                    for (int i = startIndex; i < WINDOW_SIZE + startIndex; ++i) {
                        bollValueTuples.add(calcBoll(dtds, i, i + N));
                    }
                    if (isFirstOpen(bollValueTuples)) {
                        BollValueTuple lastestValueTuple = bollValueTuples.get(0);
                        double openRate = (prevDtd.getOpeningPrice(null) - lastestValueTuple
                            .getAvgPrice()) / lastestValueTuple.getAvgPrice();
                        double highestToHighest = (prevDtd.getClosingPrice(null) - lastestValueTuple
                            .getUpPrice()) / lastestValueTuple.getUpPrice();
                        if (openRate < 0.06 && openRate > -0.02
                            && prevDtd.getClosingPrice(null) > prevDtd.getOpeningPrice(null)) {
                            if (isEvalute) {
                                double standardRate = bollValueTuples.get(1)
                                    .getStandardDeviationRate();
                                double bollRaisingRate = (bollValueTuples.get(0)
                                    .getStandardDeviationRate() - standardRate);
                                double bollFallingRate = (bollValueTuples.get(2)
                                    .getStandardDeviationRate() - standardRate);
                                double raisingRate = (curDtd.getClosingPrice(null) - prevDtd
                                    .getClosingPrice(null)) / prevDtd.getClosingPrice(null);
                                if (bollRaisingRate > 0.01) {
                                    String output = stockCode + "," + processDate + ","
                                                    + DecimalUtil.formatPercent(bollRaisingRate)
                                                    + ","
                                                    + DecimalUtil.formatPercent(bollFallingRate)
                                                    + ",," + DecimalUtil.formatPercent(raisingRate)
                                                    + "\n";
                                    System.out.print(output);
                                    fos.write(output.getBytes());
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    private static BollValueTuple calcBoll(List<DailyTradeData> dtds, int start, int end) {
        BollValueTuple bollValueTuple = new BollValueTuple();
        double sum = 0;
        int N = end - start;
        double avg = 0;
        for (int i = start; i < end; ++i) {
            sum += dtds.get(i).getClosingPrice(null);
        }
        avg = sum / N;

        sum = 0;
        for (int i = start; i < end; ++i) {
            double diff = dtds.get(i).getClosingPrice(null) - avg;
            sum += diff * diff;
        }
        bollValueTuple.setAvgPrice(avg);
        bollValueTuple.setStandardDeviation(Math.sqrt(sum / N));
        bollValueTuple.setUpPrice(avg + K * bollValueTuple.getStandardDeviation());
        bollValueTuple.setStandardDeviationRate(K * bollValueTuple.getStandardDeviation()
                                                / bollValueTuple.getAvgPrice());
        return bollValueTuple;
    }

    private static boolean isFirstOpen(List<BollValueTuple> bollValueTuples) {
        if (bollValueTuples.get(0).getStandardDeviationRate() < bollValueTuples.get(1)
            .getStandardDeviationRate()) {
            return false;
        }
        for (int i = 1; i < bollValueTuples.size() - 1; ++i) {
            if (bollValueTuples.get(i).getStandardDeviationRate() > bollValueTuples.get(i + 1)
                .getStandardDeviationRate()) {
                return false;
            }
        }
        return true;
    }
}
