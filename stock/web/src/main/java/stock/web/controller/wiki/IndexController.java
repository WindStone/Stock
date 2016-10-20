/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.controller.wiki;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import stock.common.dal.datainterface.DocRecordDAO;
import stock.common.dal.datainterface.TradeDetailRecordDAO;
import stock.common.dal.dataobject.DocRecord;
import stock.web.view.wiki.WikiIndexView;

import com.google.common.collect.Lists;

/**
 * @author yuanren.syr
 * @version $Id: HomeController.java, v 0.1 2016/2/21 23:22 yuanren.syr Exp $
 */
@Controller
public class IndexController {

    @Autowired
    private DocRecordDAO         docRecordDAO;

    @Autowired
    private TradeDetailRecordDAO tradeDetailRecordDAO;

    private static String        INDEX_RETURN = "/wiki/index";

    @RequestMapping(value = "/wiki/index.html", method = RequestMethod.GET)
    public String getIndex(Map<String, Object> model) {
        List<DocRecord> docRecords = docRecordDAO.queryAllDocRecords();
        if (CollectionUtils.isEmpty(docRecords)) {
            return INDEX_RETURN;
        }

        List<WikiIndexView> wikiIndexViews = Lists.newArrayList();
        for (DocRecord docRecord : docRecords) {
            WikiIndexView wikiIndexView = new WikiIndexView();
            wikiIndexView.setPlainText(docRecord.getDocTitle());
            wikiIndexView.setLink(MessageFormat.format("/wiki/summary.html?docId={0}",
                docRecord.getDocRecordId()));
            wikiIndexViews.add(wikiIndexView);
        }

        model.put("wikiIndexViews", wikiIndexViews);
        return INDEX_RETURN;
    }
}
