/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.PlateTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyPlateData;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.web.stock.data.processor.plate.PlateProcessor;
import stock.web.utils.HolidayUtils;
import stock.web.view.DailyPlateView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yuanren.syr
 * @version $Id: AlgorithmController.java, v 0.1 2016/1/30 16:30 yuanren.syr Exp $
 */
@Controller
public class AlgorithmController {

    @Autowired
    private DailyTradeDAO dailyTradeDAO;

    @Autowired
    private PlateTradeDAO plateTradeDAO;

    private
    @Resource(name = "plateProcessors")
    List<PlateProcessor> plateProcessors;

    @Autowired
    private SequenceGenerator sequenceGenerator;

    private static final String BOLL_START_DATE = "2014/02/07";
    private static final String BOLL_DIFF_START_DATE = "2014/02/07";
    private static final String MACD_START_DATE = "2015/06/01";
    private static final String DMA_START_DATE = "2015/07/09";

    private static final String DEFAULT_DATE = "2015/06/01";

    //    @Autowired
    //    private PlateProcessor bollPlateProcessor;
    //
    //    @Autowired
    //    private PlateProcessor macdPlateProcessor;
    //
    //    @Autowired
    //    private PlateProcessor dmaPlateProcessor;

    @RequestMapping(value = "/algorithm.html", method = RequestMethod.GET)
    public String getAlgorithmResult(Map<String, Object> model) {

        Date endDate = HolidayUtils.getLastAvailableDate();
        model.put("bollStartDate", BOLL_START_DATE);
        model.put("bollDefaultDate", BOLL_START_DATE.compareTo(DEFAULT_DATE) > 0 ? BOLL_START_DATE
                : DEFAULT_DATE);
        model.put("bollDiffStartDate", BOLL_DIFF_START_DATE);
        model.put("bollDiffDefaultDate",
                BOLL_DIFF_START_DATE.compareTo(DEFAULT_DATE) > 0 ? BOLL_DIFF_START_DATE : DEFAULT_DATE);
        model.put("macdStartDate", MACD_START_DATE);
        model.put("macdDefaultDate", MACD_START_DATE.compareTo(DEFAULT_DATE) > 0 ? MACD_START_DATE
                : DEFAULT_DATE);
        model.put("dmaStartDate", DMA_START_DATE);
        model.put("dmaDefaultDate", DMA_START_DATE.compareTo(DEFAULT_DATE) > 0 ? DMA_START_DATE
                : DEFAULT_DATE);
        model.put("endDate", DateUtil.slashDateFormat(endDate));

        model.put("macdAvg", 0.125);

        return "algorithm";
    }

    @RequestMapping(value = {"/getAlgorithmResult.json"})
    public String getAlgorithmResult(Map<String, Object> model, HttpServletRequest request) {
        String algorithm = request.getParameter("algorithm");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        try {
            String decodedAlgorithm = new String(algorithm.getBytes(System.getProperty("file.encoding")));
            // 参数校验
            String startDateStd = null;
            Double avgPrice = null;
            if (StringUtils.equals(decodedAlgorithm, "BOLL超过中轴百分比")) {
                startDateStd = BOLL_START_DATE;
            } else if (StringUtils.equals(decodedAlgorithm, "BOLL距上下轴3%股票数量差值百分比")) {
                startDateStd = BOLL_DIFF_START_DATE;
            } else if (StringUtils.equals(decodedAlgorithm, "MACD中轴上方百分比")) {
                startDateStd = MACD_START_DATE;
            } else if (StringUtils.equals(decodedAlgorithm, "DMA向上百分比")) {
                startDateStd = DMA_START_DATE;
            }

            List<DailyPlateData> dailyPlateDatas = plateTradeDAO.queryIntervalByNameAndDate("上证指数",
                    decodedAlgorithm, DateUtil.parseSlashDate(startDateStd),
                    DateUtil.parseSlashDate(endDate));

            if (DateUtil.getDiffInDays(DateUtil.parseSlashDate(startDate),
                    DateUtil.parseSlashDate(startDateStd)) < 0) {
                return null;
            }
            if (DateUtil.getDiffInDays(DateUtil.parseSlashDate(endDate),
                    HolidayUtils.getLastAvailableDate()) > 0) {
                return null;
            }

            // 读取指数信息
            List<DailyTradeData> shExps = dailyTradeDAO.queryByIntervalTradingData("SH000001",
                    startDate, endDate);
            List<DailyTradeData> szExps = dailyTradeDAO.queryByIntervalTradingData("SZ399106",
                    startDate, endDate);
            Map<Date, Double> shExpMap = Maps.newHashMap();
            Map<Date, Double> szExpMap = Maps.newHashMap();
            for (DailyTradeData shExp : shExps) {
                shExpMap.put(shExp.getCurrentDate(), shExp.getClosingPrice(null));
            }
            for (DailyTradeData szExp : szExps) {
                szExpMap.put(szExp.getCurrentDate(), szExp.getClosingPrice(null));
            }

            List<DailyPlateView> dailyPlateViews = Lists.newArrayList();
            for (int i = 0; i < dailyPlateDatas.size(); ++i) {
                DailyPlateData dailyPlateData = dailyPlateDatas.get(i);
                if (DateUtil.getDiffInDays(dailyPlateData.getTradingDate(),
                        DateUtil.parseSlashDate(startDate)) < 0) {
                    continue;
                }
                DailyPlateView dailyPlateView = new DailyPlateView(dailyPlateData);
                Double shExp = shExpMap.get(dailyPlateData.getTradingDate());
                Double szExp = szExpMap.get(dailyPlateData.getTradingDate());
                if (shExp != null && szExp != null) {
                    dailyPlateView.setShExp(shExp);
                    dailyPlateView.setSzExp(szExp);
                    if (StringUtils.equals(decodedAlgorithm, "BOLL距上下轴3%股票数量差值百分比")) {
                        dailyPlateView.setAvg(calcAvg(dailyPlateDatas, i - 60, i));
                    }
                    dailyPlateViews.add(dailyPlateView);
                }
            }

            model.put("callback", request.getParameter("callback"));
            model.put("json", JSONArray.toJSONString(dailyPlateViews));
            return "stockcode";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Double calcAvg(List<DailyPlateData> dpds) {
        Double sum = 0.0;
        for (DailyPlateData dpd : dpds) {
            sum += Double.parseDouble(dpd.getValue());
        }
        return sum / dpds.size();
    }

    private Double calcAvg(List<DailyPlateData> dpds, int start, int end) {
        Double sum = 0.0;
        start = Math.max(start, 0);
        for (int i = start; i < end; ++i) {
            sum += Double.parseDouble(dpds.get(i).getValue());
        }
        return sum / (end - start);
    }
}
