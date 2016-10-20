/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller.wiki;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import stock.common.dal.datainterface.DocRecordDAO;
import stock.common.dal.datainterface.TradeDetailRecordDAO;
import stock.common.dal.datainterface.TradeRecordDAO;
import stock.common.dal.dataobject.DocRecord;
import stock.common.dal.dataobject.TradeDetailRecord;
import stock.common.dal.dataobject.TradeRecord;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.core.model.enums.RecordRelationTypeEnum;
import stock.web.view.wiki.TradeDetailRecordView;
import stock.web.view.wiki.TradeRecordView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: SummaryController.java, v 0.1 2016/2/22 20:32 yuanren.syr Exp $
 */
@Controller
public class SummaryController {

    @Autowired
    private DocRecordDAO         docRecordDAO;

    @Autowired
    private TradeRecordDAO       tradeRecordDAO;

    @Autowired
    private TradeDetailRecordDAO tradeDetailRecordDAO;

    @RequestMapping(value = "/wiki/summary.html", method = RequestMethod.GET)
    public String getSummary(Map<String, Object> model, @RequestParam("docId") String docId) {
        DocRecord docRecord = docRecordDAO.queryDocRecordById(docId);
        model.put("title", docRecord.getDocTitle());
        List<TradeRecord> tradeRecords = tradeRecordDAO.queryTradeRecordsByDocId(
            RecordRelationTypeEnum.DOC_2_TRADE_RECORD.name(), docRecord.getDocRecordId());

        Map<String, List<TradeRecordView>> tradeRecordMap = Maps.newHashMap();
        for (TradeRecord tradeRecord : tradeRecords) {
            TradeRecordView tradeRecordView = new TradeRecordView();
            tradeRecordView.setStockCode(tradeRecord.getStockCode());
            tradeRecordView.setStockName(tradeRecord.getStockName());
            tradeRecordView.setOperator(tradeRecord.getOperator());
            List<TradeDetailRecord> tradeDetailRecords = tradeDetailRecordDAO
                .queryTradeDetailRecordByTradeId(tradeRecord.getTradeRecordId());

            List<TradeDetailRecordView> recordDetailViews = Lists.newArrayList();
            for (TradeDetailRecord tradeDetailRecord : tradeDetailRecords) {
                recordDetailViews.add(convertTradeDetailRecord(tradeDetailRecord));
            }
            tradeRecordView.setTradeDetailRecordViews(recordDetailViews);
            List<TradeRecordView> operatorRecords = tradeRecordMap.get(tradeRecord.getOperator());
            if (operatorRecords == null) {
                operatorRecords = Lists.newArrayList();
                tradeRecordMap.put(tradeRecord.getOperator(), operatorRecords);
            }
            operatorRecords.add(tradeRecordView);
        }

        for (List<TradeRecordView> tradeRecordViews : tradeRecordMap.values()) {
            Collections.sort(tradeRecordViews, new Comparator<TradeRecordView>() {
                public int compare(TradeRecordView o1, TradeRecordView o2) {
                    return (int) (o1.getTradeDetailRecordViews().get(0).getGmtTrade().getTime() - o2
                        .getTradeDetailRecordViews().get(0).getGmtTrade().getTime());
                }
            });
        }

        model.put("tradeRecords", tradeRecords);

        return "summary";
    }

    public TradeDetailRecordView convertTradeDetailRecord(TradeDetailRecord tradeDetailRecord) {
        TradeDetailRecordView tradeDetailRecordView = new TradeDetailRecordView();
        if (StringUtils.equals(tradeDetailRecord.getTradeFlow(), "buy_in")) {
            tradeDetailRecordView.setTradeFlow("买入");
        } else if (StringUtils.equals(tradeDetailRecord.getTradeFlow(), "sell_out")) {
            tradeDetailRecordView.setTradeFlow("卖出");
        }
        tradeDetailRecordView.setPrice(DecimalUtil.formatDecimal(tradeDetailRecord.getPrice()));
        tradeDetailRecordView.setTradeDate(DateUtil.simpleFormat(tradeDetailRecord.getTradeTime()));
        tradeDetailRecordView.setTradeTime(DateUtil.simpleTimeFormat(tradeDetailRecord
            .getTradeTime()));
        tradeDetailRecordView.setGmtTrade(tradeDetailRecord.getTradeTime());
        tradeDetailRecordView.setConclusion(tradeDetailRecord.getConclusion());

        return tradeDetailRecordView;
    }
}
