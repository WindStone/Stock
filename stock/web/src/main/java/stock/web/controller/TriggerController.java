/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;
import stock.web.config.DailyDataWorkerConfig;
import stock.web.scheduler.DailyDataProcessWorker;
import stock.web.scheduler.DailyScheduler;
import stock.web.scheduler.ProcessQueue;
import stock.web.stock.data.processor.followed.RasingRateProcessor;
import stock.web.utils.HolidayUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: HomeController.java, v 0.1 2016/1/5 23:55 yuanren.syr Exp $
 */
@Controller
public class TriggerController {

    @Autowired
    private ProcessQueue processQueue;

    @Autowired
    private DailyScheduler dailyScheduler;

    @RequestMapping(value = {"/trigger.html"}, method = {RequestMethod.GET})
    public String trigger(Map<String, Object> model,
                          @RequestParam(value = "password", required = false) String password) {
        if (StringUtils.equals(password, "Dream612009")) {
            model.put("END", "Now trigger a process to collect data!");
            dailyScheduler.execute();
        } else {
            model.put("END", "Hi tricker, do not trigger!");
        }
        return "index";
    }

    @RequestMapping(value = {"/trigger.json"}, method = {RequestMethod.GET})
    public void trigger(@RequestParam("fileName") String fileName) {
        try {
            fileName = new String(fileName.getBytes("iso-8859-1"), "UTF-8");
            String date = StringUtils.substringBetween(fileName, "_", ".");
            File oldFile = new File(PathUtil.getExcelPath() + date + File.separatorChar + fileName);
            oldFile.delete();
            for (Map.Entry<String, DailyDataWorkerConfig> entry : dailyScheduler
                    .getWorkerConfigMap().entrySet()) {
                if (StringUtils.contains(fileName, entry.getKey())) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put(RasingRateProcessor.CALC_DAY,
                            HolidayUtils.getNextWorkDate(DateUtil.parseSimpleDate(date)));
                    processQueue.offer(new DailyDataProcessWorker(DateUtil.parseSimpleDate(date),
                            entry.getValue(), map));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
