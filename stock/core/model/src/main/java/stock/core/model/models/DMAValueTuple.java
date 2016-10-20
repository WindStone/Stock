/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

/**
 * @author yuanren.syr
 * @version $Id: DMAValueTuple.java, v 0.1 2016/1/31 23:56 yuanren.syr Exp $
 */
public class DMAValueTuple extends SerializableModel {

    private double DMA;

    private double AMA;

    public double getDMA() {
        return DMA;
    }

    public void setDMA(double DMA) {
        this.DMA = DMA;
    }

    public double getAMA() {
        return AMA;
    }

    public void setAMA(double AMA) {
        this.AMA = AMA;
    }

}
