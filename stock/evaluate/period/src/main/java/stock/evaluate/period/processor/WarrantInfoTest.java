/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.evaluate.period.processor;

import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.jxl.HistoryDataAccessorImpl;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanren.syr
 * @version $Id: HistoryDataTransProcessor.java, v 0.1 2015/12/6 16:09 yuanren.syr Exp $
 */
public class WarrantInfoTest {

    private static HistoryDataAccessor historyDataAccessor = new HistoryDataAccessorImpl();

    public static void main(String[] args) {
        String warrantUrl = MessageFormat
                .format(
                        "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_FuQuanMarketHistory/stockid/{0}.phtml?year={1}&jidu={2}",
                        "600008", 2016, 1);
        try {
            URL url = new URL(warrantUrl);
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Pragma", "no-cache");
            conn.setRequestProperty("Expires", "0");
            conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.connect();
            System.out.println(JSON.toJSONString(conn.getHeaderFields()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
