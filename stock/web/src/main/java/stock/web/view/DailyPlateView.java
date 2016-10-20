/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

import stock.common.dal.dataobject.DailyPlateData;
import stock.common.util.DateUtil;

/**
 * @author yuanren.syr
 * @version $Id: DailyPlateView.java, v 0.1 2016/2/15 21:31 yuanren.syr Exp $
 */
public class DailyPlateView {

    public String date;

    public String value;

    public Double shExp;

    public Double szExp;

    public Double avg;

    public DailyPlateView(DailyPlateData dailyPlateData) {
        this.date = DateUtil.slashDateFormat(dailyPlateData.getTradingDate());
        this.value = dailyPlateData.getValue();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getShExp() {
        return shExp;
    }

    public void setShExp(Double shExp) {
        this.shExp = shExp;
    }

    public Double getSzExp() {
        return szExp;
    }

    public void setSzExp(Double szExp) {
        this.szExp = szExp;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }
}
