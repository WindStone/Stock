/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.config;

import stock.web.stock.data.processor.plate.PlateProcessor;

/**
 * @author yuanren.syr
 * @version $Id: PlateAlgorithmWorkerConfig.java, v 0.1 2016/2/16 17:36 yuanren.syr Exp $
 */
public class PlateAlgorithmWorkerConfig {

    private String         plateName;

    private PlateProcessor plateProcessor;

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public PlateProcessor getPlateProcessor() {
        return plateProcessor;
    }

    public void setPlateProcessor(PlateProcessor plateProcessor) {
        this.plateProcessor = plateProcessor;
    }
}
