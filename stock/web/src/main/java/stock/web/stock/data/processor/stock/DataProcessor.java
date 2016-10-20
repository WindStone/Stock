/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.stock;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyTradeData;
import stock.web.context.StockContext;
import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: DataProcessor.java, v 0.1 2016/1/7 11:33 yuanren.syr Exp $
 */
public interface DataProcessor<T extends DailyTradeData> {

    List<TitleLabel> getTitleLables();

    List<TitleLabel> getForcasTitleLabels();

    T processEach(String stockCode, Date currentDate, StockContext stockContext);

    List<T> sortDatas(List<T> originTradeDatas);

    List<TitleLabel> getSpecialTitleLabels();

    List<ContentLabel> getSpecialContents(T dailyTradeData);
}
