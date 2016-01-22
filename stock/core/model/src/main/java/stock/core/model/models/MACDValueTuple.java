/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

/**
 * @author yuanren.syr
 * @version $Id: MACDValueTuple.java, v 0.1 2016/1/4 23:13 yuanren.syr Exp $
 */
public class MACDValueTuple {

    private double EMA1;

    private double EMA2;

    private double DIF;

    private double DEA;

    private double MACD;

    public double getEMA1() {
        return EMA1;
    }

    public void setEMA1(double EMA1) {
        this.EMA1 = EMA1;
    }

    public double getEMA2() {
        return EMA2;
    }

    public void setEMA2(double EMA2) {
        this.EMA2 = EMA2;
    }

    public double getDIF() {
        return DIF;
    }

    public void setDIF(double DIF) {
        this.DIF = DIF;
    }

    public double getDEA() {
        return DEA;
    }

    public void setDEA(double DEA) {
        this.DEA = DEA;
    }

    public double getMACD() {
        return MACD;
    }

    public void setMACD(double MACD) {
        this.MACD = MACD;
    }
}
