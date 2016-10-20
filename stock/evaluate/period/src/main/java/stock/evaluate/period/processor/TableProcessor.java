/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.PathUtil;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: TableProcessor.java, v 0.1 2016/3/22 13:46 yuanren.syr Exp $
 */
public class TableProcessor {

    private static String closingPriceStr = "27.22 20.89 10.48\n" +
            "21.70 15.85 37.50";

    private static String stockCodeStr    = "";

    private static String openingPriceStr = "";

    private static String table           = "<table>\n"
                                            + "  <tr>\n"
                                            + "    <th>日期</th>\n"
                                            + "    <th>参与者</th>\n"
                                            + "    <th>预测股票</th>\n"
                                            + "    <th>次日最低点</th>\n"
                                            + "    <th>第三日开盘平均值</th>\n"
                                            + "    <th>得分</th>\n"
                                            + "    <th>累计得分</th>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\" rowspan=\"9\">{0}</td>\n"
                                            + "    <td align=\"center\" rowspan=\"3\">Roger</td>\n"
                                            + "    <td align=\"center\"><b>{1}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\" rowspan=\"3\"></td>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\"><b>{2}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\"><b>{3}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\" rowspan=\"3\">Aphonese.qp</td>\n"
                                            + "    <td align=\"center\"><b>{4}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\" rowspan=\"3\"></td>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\"><b>{5}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "  </tr>\n"
                                            + "  <tr>\n"
                                            + "    <td align=\"center\"><b>{6}</b></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "    <td align=\"center\"></td>\n"
                                            + "  </tr>\n"
                                            + "</table>\n";

    public static void main(String[] args) throws IOException {
        String[] stockCodes = StringUtils.split(stockCodeStr, " \n");
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        List<String> stockNamesAndCodes = Lists.newArrayList(DateUtil.simpleFormat(DateUtils
            .addDays(new Date(), -1)));
        for (String stockCode : stockCodes) {
            String newStockCode = "";
            if (StringUtils.startsWith(stockCode, "60")) {
                newStockCode = "SH" + stockCode;
            } else {
                newStockCode = "SZ" + stockCode;
            }
            DailyTradeData dtd = dailyTradeDAO.queryByPrevKTradingData(newStockCode, "2016-03-02",
                1).get(0);
            dtd.getStockName();
            stockNamesAndCodes.add(dtd.getStockName() + "(" + stockCode + ")");
        }

        FileInputStream f = new FileInputStream(
            "./stock/evaluate/period/src/main/resource/template/currentTable.xml");
        byte[] b = new byte[2048];
        StringBuffer sb = new StringBuffer();
        int size = 0;
        while ((size = f.read(b)) >= 0) {
            sb.append(new String(b, 0, size));
        }
        String[] openingPrices = StringUtils.split(openingPriceStr, " \n");
        String[] closingPrices = StringUtils.split(closingPriceStr, " \n");
        int lastPredictStart = sb.lastIndexOf("<table>");
        if (lastPredictStart != -1) {
            if (closingPrices.length != 0) {
                fillClosingPrice(sb, lastPredictStart, closingPrices);
            }
        }
        int nextPredictStart = sb.length();
        if (StringUtils.isNotBlank(stockCodeStr)) {
            sb.append(MessageFormat.format(table, stockNamesAndCodes.toArray()));
            if (openingPrices.length != 0) {
                fillOpeningPrice(sb, nextPredictStart, openingPrices);
            }
        }

        System.out.println(sb.toString());
    }

    private static void fillOpeningPrice(StringBuffer sb, int nextPredictStart,
                                         String openingPrices[]) {
        String startTag = "<td align=\"center\">";
        String rowStartTag = "<tr>";
        int rowStart = sb.indexOf(rowStartTag, nextPredictStart);
        for (int i = 0; i < openingPrices.length; ++i) {
            rowStart = sb.indexOf(rowStartTag, rowStart + rowStartTag.length());
            int colOpenStart = sb.indexOf(startTag, rowStart);
            colOpenStart = sb.indexOf(startTag, colOpenStart + startTag.length());
            sb.insert(colOpenStart + startTag.length(), openingPrices[i]);
        }
    }

    private static void fillClosingPrice(StringBuffer sb, int lastPredictStart,
                                         String[] closingPrices) {
        String startTag = "<td align=\"center\">";
        String summaryTag = "<td align=\"center\" rowspan=\"3\">";
        int stockNumPerPerson = 3;
        double[] rasingRate = new double[closingPrices.length];
        int rowStart = sb.indexOf("<tr>", lastPredictStart + 4);
        for (int i = 0; i < closingPrices.length; ++i) {
            rowStart = sb.indexOf("<tr>", rowStart + 4);
            int colOpenStart = sb.indexOf(startTag, rowStart);
            colOpenStart = sb.indexOf(startTag, colOpenStart + startTag.length());
            int colOpenEnd = sb.indexOf("</td>", colOpenStart);
            double openingPrice, closingPrice;
            int colCloseStart = sb.indexOf(startTag, colOpenEnd);

            if (!StringUtils.equals(closingPrices[i], "停牌")) {
                openingPrice = Double.parseDouble(sb.substring(colOpenStart + startTag.length(),
                    colOpenEnd));
                closingPrice = Double.parseDouble(closingPrices[i]);
                rasingRate[i] = (closingPrice - openingPrice) / openingPrice;
                sb.insert(colCloseStart + startTag.length(),
                    DecimalUtil.formatDecimal(closingPrice));
                int colRasingRate = sb.indexOf(startTag, colCloseStart + startTag.length());
                sb.insert(colRasingRate + startTag.length(),
                    DecimalUtil.formatPercent(rasingRate[i]));
            } else {
                sb.insert(colCloseStart + startTag.length(), closingPrices[i]);
                rasingRate[i] = 0;
                int colRasingRate = sb.indexOf(startTag, colCloseStart + startTag.length());
                sb.insert(colRasingRate + startTag.length(), "0%");
            }
        }
        double[] originalRate = new double[closingPrices.length / stockNumPerPerson];
        int preTableStart = sb.lastIndexOf("<table>", lastPredictStart - 1);

        int preIndex = preTableStart;
        for (int i = 0; i < originalRate.length; ++i) {
            if (preTableStart >= 0) {
                int titleStart = sb.indexOf(summaryTag, preIndex);
                int summaryStart = sb.indexOf(summaryTag, titleStart + summaryTag.length());
                int summaryEnd = sb.indexOf("</td>", summaryStart);
                originalRate[i] = DecimalUtil.parsePercent(sb.substring(
                    summaryStart + summaryTag.length(), summaryEnd));
                preIndex = summaryEnd;
            } else {
                originalRate[i] = 0;
            }
        }

        for (int i = 0; i < originalRate.length; ++i) {
            for (int j = 0; j < stockNumPerPerson; ++j) {
                originalRate[i] += rasingRate[i * stockNumPerPerson + j];
            }
        }

        preIndex = lastPredictStart;
        for (int i = 0; i < originalRate.length; ++i) {
            int titleStart = sb.indexOf(summaryTag, preIndex);
            int summaryStart = sb.indexOf(summaryTag, titleStart + summaryTag.length());
            sb.insert(summaryStart + summaryTag.length(),
                DecimalUtil.formatPercent(originalRate[i]));
            preIndex = summaryStart + summaryTag.length();
        }
    }
}
