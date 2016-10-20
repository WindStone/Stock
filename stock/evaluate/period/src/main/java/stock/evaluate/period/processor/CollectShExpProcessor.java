/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;

/**
 * @author yuanren.syr
 * @version $Id: HistoryDataTransProcessor.java, v 0.1 2015/12/6 16:09 yuanren.syr Exp $
 */
public class CollectShExpProcessor {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    private static final String[][]    STOCK_INFOS         = { { "000001", "SH000001", "上证指数" },
            { "399106", "SZ399106", "深圳综指" }              };

    public static void main(String[] args) {

        int index = 1;

        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        Date startDate = DateUtil.parseSimpleDate("2016-04-06");
        Date endDate = DateUtil.parseSimpleDate("2016-07-24");
        for (; DateUtil.getDiffInDays(startDate, endDate) < 0; startDate = DateUtils.addDays(
            startDate, 1)) {
            int season = startDate.getMonth() / 3 + 1;
            String link = MessageFormat.format(
                "http://quotes.money.163.com/trade/lsjysj_zhishu_{0}.html?year={1}&season={2}",
                STOCK_INFOS[index][0], String.valueOf(startDate.getYear() + 1900),
                String.valueOf(season));
            try {
                URL url = new URL(link);
                Parser parser = new Parser(url.openConnection());
                parser.setEncoding("utf-8");

                NodeFilter tableFilter = new NodeFilter() {
                    public boolean accept(Node node) {
                        if (node.getText().startsWith(
                            "table class=\"table_bg001 border_box limit_sale\"")) {
                            return true;
                        }
                        return false;
                    }
                };
                NodeList tables = parser.extractAllNodesThatMatch(tableFilter);
                NodeList trs = tables.elementAt(0).getChildren();
                for (int trIndex = 0; trIndex < trs.size(); ++trIndex) {
                    Node tr = trs.elementAt(trIndex);
                    if (tr instanceof TableRow) {
                        NodeList tds = tr.getChildren().extractAllNodesThatMatch(
                            new NodeClassFilter(TableColumn.class));
                        if (tds.size() == 0) {
                            continue;
                        }

                        String dateStr = tds.elementAt(0).getChildren().toHtml();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        Date curDate = sdf.parse(dateStr);
                        if (!DateUtils.isSameDay(curDate, startDate)) {
                            continue;
                        }

                        DailyTradeData dtd = new DailyTradeData();
                        dtd.setStockCode(STOCK_INFOS[index][1]);
                        dtd.setStockName(STOCK_INFOS[index][2]);

                        dtd.setStockDailyId(sequenceGenerator
                            .getSequence(SequenceEnum.STOCK_DAILY_SEQ));
                        dtd.setCurrentDate(curDate);
                        dtd.setDate(DateUtil.simpleFormat(curDate));
                        dtd.setOpeningPrice(parseDouble(tds.elementAt(1).getChildren().toHtml()));
                        dtd.setHighestPrice(parseDouble(tds.elementAt(2).getChildren().toHtml()));
                        dtd.setLowestPrice(parseDouble(tds.elementAt(3).getChildren().toHtml()));
                        dtd.setClosingPrice(parseDouble(tds.elementAt(4).getChildren().toHtml()));
                        dtd.setTradingVolume(parseDouble(tds.elementAt(7).getChildren().toHtml()));
                        dtd.setTradingAmount(parseDouble(tds.elementAt(8).getChildren().toHtml()));

                        try {
                            dailyTradeDAO.insert(dtd);
                            System.out.println("Done " + DateUtil.simpleFormat(startDate));
                        } catch (Exception e) {
                            System.out.println("Ignore " + DateUtil.simpleFormat(startDate));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception " + e);
            }
        }

    }

    private static double parseDouble(String doubleStr) {
        return Double.parseDouble(StringUtils.remove(doubleStr, ","));
    }
}
