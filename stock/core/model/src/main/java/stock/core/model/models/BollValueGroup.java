/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import java.util.Date;
import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: BollValueGroup.java, v 0.1 2015/12/20 15:55 yuanren.syr Exp $
 */
public class BollValueGroup {

    private double       curSDRaisingRate;

    private double       prevSDRaisingRate;

    private List<Double> sdRate;

    private String       stockCode;

    private Date         date;

    public double getCurSDRaisingRate() {
        return curSDRaisingRate;
    }

    public void setCurSDRaisingRate(double curSDRaisingRate) {
        this.curSDRaisingRate = curSDRaisingRate;
    }

    public double getPrevSDRaisingRate() {
        return prevSDRaisingRate;
    }

    public void setPrevSDRaisingRate(double prevSDRaisingRate) {
        this.prevSDRaisingRate = prevSDRaisingRate;
    }

    public List<Double> getSdRate() {
        return sdRate;
    }

    public void setSdRate(List<Double> sdRate) {
        this.sdRate = sdRate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
