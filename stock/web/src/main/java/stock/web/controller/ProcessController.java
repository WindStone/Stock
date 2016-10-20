/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.web.scheduler.ProcessQueue;
import stock.web.scheduler.ProcessWorker;
import stock.web.view.AjaxResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: ProcessController.java, v 0.1 2016/1/23 23:13 yuanren.syr Exp $
 */
@Controller
public class ProcessController {

    @Autowired
    private ProcessQueue processQueue;

    @RequestMapping(value = {"/process.json"})
    public String getProcess(Map<String, Object> model, HttpServletRequest request) {
        ProcessWorker processWorker = processQueue.peek();
        double totalPercent = 1.0;
        if (processWorker != null) {
            totalPercent = processWorker.getProcess();
        }

        model.put("callback", request.getParameter("callback"));
        model.put("json", JSONObject.toJSONString(new AjaxResult((int) (totalPercent * 100))));
        return "stockcode";
    }
}
