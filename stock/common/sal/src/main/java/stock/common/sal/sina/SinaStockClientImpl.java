/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.sal.sina;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.Current;
import stock.common.sal.model.CurrentTradeData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

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
}
