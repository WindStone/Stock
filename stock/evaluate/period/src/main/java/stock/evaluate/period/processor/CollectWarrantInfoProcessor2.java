/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.CollectionUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.dal.jxl.HistoryDataAccessorImpl;
import stock.common.util.PathUtil;

/**
 * @author yuanren.syr
 * @version $Id: HistoryDataTransProcessor.java, v 0.1 2015/12/6 16:09 yuanren.syr Exp $
 */
public class CollectWarrantInfoProcessor2 {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    public static void main(String[] args) {
        XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(
            PathUtil.getDalPath() + "/src/main/resource/META-INF/spring/common-dal.xml"));
        DailyTradeDAO dailyTradeDAO = (DailyTradeDAO) xmlBeanFactory.getBean("dailyTradeDAO");

        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();

        for (String stockCode : stockCodes) {
            if (!StringUtils.startsWith(stockCode, "SZ00")
                && !StringUtils.startsWith(stockCode, "SH60")
                && !StringUtils.startsWith(stockCode, "SZ300")) {
                continue;
            }

            List<DailyTradeData> dtds = dailyTradeDAO.queryByPrevKTradingData(stockCode,
                "2016-02-19", 1);
            if (CollectionUtils.isEmpty(dtds)) {
                continue;
            }
            if (dtds.get(0).getWarrantFactor() != 0) {
                continue;
            }
            String trimmedStockCode = StringUtils.removeStart(stockCode, "SH");
            trimmedStockCode = StringUtils.removeStart(trimmedStockCode, "SZ");
            int year = 2016;
            int month = 1;
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
                Map<String, Double> factorMap = new HashMap<String, Double>();
                NodeList trs = tables.elementAt(0).getChildren();
                for (int trIndex = 0; trIndex < trs.size(); ++trIndex) {
                    Node tr = trs.elementAt(trIndex);
                    if (tr instanceof TableRow) {
                        NodeList trChildren = tr.getChildren();
                        NodeList tds = tr.getChildren().extractAllNodesThatMatch(
                            new NodeClassFilter(TableColumn.class));
                        if (tds.size() < 8) {
                            continue;
                        }

                        LinkTag linkTag = (LinkTag) tds.elementAt(0).getFirstChild().getChildren()
                            .extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class))
                            .elementAt(0);
                        if (linkTag == null) {
                            continue;
                        }
                        String date = linkTag.getLinkText().trim();
                        try {
                            Div div = (Div) tds.elementAt(7).getChildren()
                                .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                .elementAt(0);
                            double factor = Double.parseDouble(div.getStringText().trim());
                            factorMap.put(date, factor);
                        } catch (NumberFormatException e) {
                        }
                    }
                }
                Double warrant = factorMap.get("2016-02-19");
                if (dtds != null && dtds.size() != 0) {
                    dtds.get(0).setWarrantFactor(warrant);
                    dailyTradeDAO.updateDailyTradingData(dtds.get(0));
                }
                System.out.println("Done " + stockCode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
