/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.stock;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.sal.sina.SinaStockClient;

/**
 * @author yuanren.syr
 * @version $Id: AbstractDataProcessor.java, v 0.1 2016/1/7 11:55 yuanren.syr Exp $
 */
public abstract class AbstractDataProcessor<T extends DailyTradeData> implements DataProcessor<T> {

    protected String            fileName;

    protected DailyTradeDAO     dailyTradeDAO;

    protected SinaStockClient   sinaStockClient;

    protected SequenceGenerator sequenceGenerator;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        this.dailyTradeDAO = dailyTradeDAO;
    }

    public void setSinaStockClient(SinaStockClient sinaStockClient) {
        this.sinaStockClient = sinaStockClient;
    }

    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }
}
