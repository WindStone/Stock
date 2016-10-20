/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
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
public class SinaHistoryDataCollectorProcessor {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");
        SequenceGenerator sequenceGenerator = (SequenceGenerator) xmlBeanFactory
            .getBean("sequenceGenerator");

        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();
        Date[] dates = new Date[] { DateUtil.parseSimpleDate("2016-04-14"),
                DateUtil.parseSimpleDate("2016-04-14") };

        for (String stockCode : stockCodes) {
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }
            for (int i = 0; i < dates.length - 1; ++i) {
                String trimmedStockCode = StringUtils.removeStart(stockCode, "SH");
                trimmedStockCode = StringUtils.removeStart(trimmedStockCode, "SZ");
                int year = dates[i].getYear() + 1900;
                int month = dates[i].getMonth() / 3 + 1;
                String warrantUrl = MessageFormat
                    .format(
                        "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_FuQuanMarketHistory/stockid/{0}.phtml?year={1}&jidu={2}",
                        trimmedStockCode, String.valueOf(year), month);
                try {
                    URL url = new URL(warrantUrl);
                    Parser parser = new Parser(url.openConnection());
                    parser.setEncoding("gb2312");
                    NodeFilter tableFilter = new NodeFilter() {
                        public boolean accept(Node node) {
                            if (node.getText().startsWith("table id=\"FundHoldSharesTable\"")) {
                                return true;
                            }
                            return false;
                        }
                    };
                    NodeList tables = parser.extractAllNodesThatMatch(tableFilter);
                    if (tables.size() == 0) {
                        continue;
                    }
                    NodeList trs = tables.elementAt(0).getChildren();
                    String stockName = null;
                    for (int trIndex = 0; trIndex < trs.size(); ++trIndex) {
                        Node tr = trs.elementAt(trIndex);
                        if (tr instanceof TableRow) {
                            NodeList tds = tr.getChildren().extractAllNodesThatMatch(
                                new NodeClassFilter(TableColumn.class));
                            if (tds.size() == 0) {
                                TableHeader th = (TableHeader) tr
                                    .getChildren()
                                    .extractAllNodesThatMatch(
                                        new NodeClassFilter(TableHeader.class)).elementAt(0);
                                stockName = StringUtils.substringBefore(th.getStringText().trim(),
                                    "(");
                            }
                            if (tds.size() < 8) {
                                continue;
                            }

                            try {
                                DailyTradeData dtd = new DailyTradeData();
                                LinkTag linkTag = (LinkTag) tds.elementAt(0).getFirstChild()
                                    .getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class))
                                    .elementAt(0);
                                if (linkTag == null) {
                                    continue;
                                }
                                String dateStr = linkTag.getLinkText().trim();
                                dtd.setDate(dateStr);
                                dtd.setStockName(stockName);
                                dtd.setStockCode(stockCode);
                                dtd.setCurrentDate(DateUtil.parseSimpleDate(dateStr));

                                Div div = (Div) tds.elementAt(7).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                double warrantFactor = Double.parseDouble(div.getStringText()
                                    .trim());
                                dtd.setWarrantFactor(warrantFactor);

                                div = (Div) tds.elementAt(1).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setOpeningPrice(Double.parseDouble(div.getStringText().trim())
                                                    / warrantFactor);

                                div = (Div) tds.elementAt(2).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setHighestPrice(Double.parseDouble(div.getStringText().trim())
                                                    / warrantFactor);

                                div = (Div) tds.elementAt(3).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setClosingPrice(Double.parseDouble(div.getStringText().trim())
                                                    / warrantFactor);

                                div = (Div) tds.elementAt(4).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setLowestPrice(Double.parseDouble(div.getStringText().trim())
                                                   / warrantFactor);

                                div = (Div) tds.elementAt(5).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setTradingVolume(Double.parseDouble(div.getStringText().trim()));

                                div = (Div) tds.elementAt(6).getChildren()
                                    .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                    .elementAt(0);
                                dtd.setTradingAmount(Double.parseDouble(div.getStringText().trim()));

                                dtd.setStockDailyId(sequenceGenerator
                                    .getSequence(SequenceEnum.STOCK_DAILY_SEQ));

                                dailyTradeDAO.insert(dtd);
                            } catch (Exception e) {
                            }
                        }
                    }
                    System.out.println("Done " + stockCode + " " + DateUtil.simpleFormat(dates[i]));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
