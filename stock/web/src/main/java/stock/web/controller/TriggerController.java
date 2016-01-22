/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import stock.web.scheduler.ProcessQueue;
import stock.web.scheduler.ProcessWorker;
import stock.web.scheduler.WorkerConfig;
import stock.web.stock.data.processor.RasingRateProcessor;
import stock.web.utils.HolidayUtils;

import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: HomeController.java, v 0.1 2016/1/5 23:55 yuanren.syr Exp $
 */
@Controller
public class TriggerController {

    @Autowired
    private WorkerConfig collectorConfig;
    @Autowired
    private WorkerConfig nearestPeakForcastConfig;
    @Autowired
    private WorkerConfig nearestPeakEvaluateConfig;
    @Autowired
    private WorkerConfig oversoldRallyForcastConfig;
    @Autowired
    private ProcessQueue processQueue;

    @Autowired
    private HolidayUtils holidayUtils;

    @RequestMapping(value = { "/trigger.html" }, method = { RequestMethod.GET })
    public String trigger(Map<String, Object> model) {
        model.put("END", "Hello world!");
        Date forcastDate = DateUtils.addHours(new Date(), -16);
        if (holidayUtils.isAvailable(forcastDate)) {
            processQueue.offer(new ProcessWorker(forcastDate, collectorConfig, null));
            processQueue.offer(new ProcessWorker(forcastDate, nearestPeakForcastConfig, null));
            processQueue.offer(new ProcessWorker(forcastDate, oversoldRallyForcastConfig, null));

            Date evaluateDate = holidayUtils.getPrevWorkDate(forcastDate);

            Map<String, Object> map = Maps.newHashMap();
            map.put(RasingRateProcessor.CALC_DAY, forcastDate);
            processQueue.offer(new ProcessWorker(evaluateDate, nearestPeakEvaluateConfig, map));
        }

        return "index";
    }

    @RequestMapping(value = { "/trigger.json" }, method = { RequestMethod.GET })
    public void trigger(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam("fileName") String fileName) {
        int firstSplit = StringUtils.indexOf(fileName, "_");
    }
}
