/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.PlateTradeDAO;
import stock.common.util.DateUtil;
import stock.web.stock.data.processor.plate.BollDiffPlateProcessor;
import stock.web.stock.data.processor.plate.BollPlateProcessor;
import stock.web.stock.data.processor.plate.DMAPlateProcessor;
import stock.web.stock.data.processor.plate.MACDPlateProcessor;
import stock.web.utils.HolidayUtils;

/**
 * @author yuanren.syr
 * @version $Id: AlgorithmController.java, v 0.1 2016/1/30 16:30 yuanren.syr Exp $
 */
@Controller
public class AlgorithmTestController {

    @Autowired
    private DailyTradeDAO          dailyTradeDAO;

    @Autowired
    private PlateTradeDAO          plateTradeDAO;

    @Autowired
    private BollDiffPlateProcessor bollDiffPlateProcessor;

    @Autowired
    private BollPlateProcessor     bollPlateProcessor;

    @Autowired
    private MACDPlateProcessor     macdPlateProcessor;

    @Autowired
    private DMAPlateProcessor      dmaPlateProcessor;

    //    @Autowired
    //    private PlateProcessor bollPlateProcessor;
    //
    //    @Autowired
    //    private PlateProcessor macdPlateProcessor;
    //
    //    @Autowired
    //    private PlateProcessor dmaPlateProcessor;

    @RequestMapping(value = "/algorithmTest.html", method = RequestMethod.GET)
    public String getAlgorithmResult(Map<String, Object> model) {

        Date endDate = HolidayUtils.getLastAvailableDate();
        bollPlateProcessor.getPlateDailyProcessResult("上证指数", dailyTradeDAO.queryForStockCodes(),
            DateUtil.parseSimpleDate("2016-08-08"), new Date());
        bollDiffPlateProcessor.getPlateDailyProcessResult("上证指数",
            dailyTradeDAO.queryForStockCodes(), new Date(), DateUtil.parseSimpleDate("2016-08-08"));
        macdPlateProcessor.getPlateDailyProcessResult("上证指数", dailyTradeDAO.queryForStockCodes(),
            DateUtil.parseSimpleDate("2016-08-08"), new Date());
        dmaPlateProcessor.getPlateDailyProcessResult("上证指数", dailyTradeDAO.queryForStockCodes(),
            DateUtil.parseSimpleDate("2016-08-08"), new Date());
        return "algorithmTest";
    }
}
