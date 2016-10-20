/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.util.Date;
import java.util.List;

import stock.web.config.PlateAlgorithmWorkerConfig;
import stock.web.stock.data.processor.plate.PlateProcessor;

/**
 * @author yuanren.syr
 * @version $Id: PlateAlgorithmWorkerConfig.java, v 0.1 2016/2/16 17:20 yuanren.syr Exp $
 */
public class PlateAlgorithmProcessWorker extends AbstractProcessWorker {

    private PlateProcessor plateProcessor;

    private Date           curDate;

    private String         plateName;

    public PlateAlgorithmProcessWorker(Date curDate, PlateAlgorithmWorkerConfig config) {
        this.curDate = curDate;
        this.plateName = config.getPlateName();
        this.plateProcessor = config.getPlateProcessor();
    }

    @Override
    protected void execute() {
        List<String> stockCodes = getStockCodes();
        plateProcessor.getPlateDailyProcessResult(plateName, stockCodes, curDate, curDate);
    }

    public double getProcess() {
        return 0;
    }

}
