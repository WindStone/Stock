/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.sal.sina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Date;

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

import stock.common.sal.model.CurrentTradeData;
import stock.common.util.DateUtil;

/**
 * @author yuanren.syr
 * @version $Id: SinaStockClientImpl.java, v 0.1 2015/12/8 0:35 yuanren.syr Exp $
 */
public class SinaStockClientImpl implements SinaStockClient {

    public CurrentTradeData getStock(String stockCode) {
        CurrentTradeData ctd = null;
        BufferedReader br = null;
        try {
            URL url = new URL("http://hq.sinajs.cn/list=" + StringUtils.lowerCase(stockCode));
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.connect();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));
            StringBuffer sb = new StringBuffer();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            String result = StringUtils.substringAfter(sb.toString(), "\"");
            result = StringUtils.substringBefore(result, "\"");
            String[] splits = StringUtils.split(result, ",");
            ctd = new CurrentTradeData();
            if (splits.length == 0) {
                return null;
            }
            ctd.setStockName(splits[0]);
            ctd.setOpenPrice(Double.parseDouble(splits[1]));
            ctd.setLastClosePrice(Double.parseDouble(splits[2]));
            ctd.setCurrentPrice(Double.parseDouble(splits[3]));
            ctd.setHighestPrice(Double.parseDouble(splits[4]));
            ctd.setLowestPrice(Double.parseDouble(splits[5]));
            ctd.setTradingVolumn(Long.parseLong(splits[8]));
            ctd.setTradingAmount(Double.parseDouble(splits[9]));
            ctd.setDate(splits[30]);
            ctd.setTime(splits[31]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return ctd;
    }

    public double getWarrantFactor(String stockCode, Date date) {
        String trimmedStockCode = StringUtils.removeStart(stockCode, "SH");
        trimmedStockCode = StringUtils.removeStart(trimmedStockCode, "SZ");
        int year = date.getYear() + 1900;
        int month = date.getMonth() / 3 + 1;
        String warrantUrl = MessageFormat
            .format(
                "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_FuQuanMarketHistory/stockid/{0}.phtml?year={1}&jidu={2}",
                trimmedStockCode, String.valueOf(year), month);
        try {
            URL url = new URL(warrantUrl);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            Parser parser = new Parser(conn);
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
                return 0;
            }
            NodeList trs = tables.elementAt(0).getChildren();
            for (int trIndex = 0; trIndex < trs.size(); ++trIndex) {
                Node tr = trs.elementAt(trIndex);
                if (tr instanceof TableRow) {
                    NodeList tds = tr.getChildren().extractAllNodesThatMatch(
                        new NodeClassFilter(TableColumn.class));
                    if (tds.size() < 8) {
                        continue;
                    }

                    LinkTag linkTag = (LinkTag) tds.elementAt(0).getFirstChild().getChildren()
                        .extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class)).elementAt(0);
                    if (linkTag == null) {
                        continue;
                    }
                    String divDate = linkTag.getLinkText().trim();
                    if (StringUtils.equals(divDate, DateUtil.simpleFormat(date))) {
                        try {
                            Div div = (Div) tds.elementAt(7).getChildren()
                                .extractAllNodesThatMatch(new NodeClassFilter(Div.class))
                                .elementAt(0);
                            return Double.parseDouble(div.getStringText().trim());
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
