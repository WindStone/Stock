package stock.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import stock.common.dal.datainterface.DailyTradeDAO;

/**
 * Created by songyuanren on 2016/8/5.
 */
@Controller
public class StockCodeController implements InitializingBean {

    @Autowired
    private DailyTradeDAO       dailyTradeDAO;

    private Map<String, String> stockCodeMap;

    @RequestMapping(value = { "/stockcode.html" })
    public String getStockCode(Map<String, Object> model, HttpServletRequest request) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : stockCodeMap.entrySet()) {
            jsonObject.put(entry.getKey(), entry.getKey() + "-" + entry.getValue());
        }
        model.put("callback", request.getParameter("callback"));
        model.put("json", jsonObject.toJSONString());
        return "stockcode";
    }

    public void afterPropertiesSet() throws Exception {
        Map<String, String> tempMap = dailyTradeDAO.queryForStockCodesAndNames();
        stockCodeMap = Maps.newHashMap();
        for (Map.Entry<String, String> entry : tempMap.entrySet()) {
            if (StringUtils.startsWith(entry.getKey(), "SH60")
                || StringUtils.startsWith(entry.getKey(), "SZ00")
                || StringUtils.startsWith(entry.getKey(), "SZ300")) {
                stockCodeMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
