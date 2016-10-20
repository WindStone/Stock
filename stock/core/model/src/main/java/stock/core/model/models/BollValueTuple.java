/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

/**
 * boll值的元组
 * @author yuanren.syr
 * @version $Id: BollValueTuple.java, v 0.1 2015/12/17 23:38 yuanren.syr Exp $
 */
public class BollValueTuple extends SerializableModel {
    private double avgPrice;

    private double standardDeviation;

    private double standardDeviationRate;

    private double upPrice;

    private double downPrice;

    private double openingPrice;

    private double closingPrice;

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(double upPrice) {
        this.upPrice = upPrice;
    }

    public double getStandardDeviationRate() {
        return standardDeviationRate;
    }

    public void setStandardDeviationRate(double standardDeviationRate) {
        this.standardDeviationRate = standardDeviationRate;
    }

    public double getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(double downPrice) {
        this.downPrice = downPrice;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(double closingPrice) {
        this.closingPrice = closingPrice;
    }
}
