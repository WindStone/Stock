/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author yuanren.syr
 * @version $Id: DailyPlateData.java, v 0.1 2016/2/2 0:08 yuanren.syr Exp $
 */
public class DailyPlateData {

    public String plateDailyId;

    public String plateName;

    public String processName;

    public Date   tradingDate;

    public String value;

    public String param;

    public Date   gmtCreate;

    public Date   gmtModified;

    public String getPlateDailyId() {
        return plateDailyId;
    }

    public void setPlateDailyId(String plateDailyId) {
        this.plateDailyId = plateDailyId;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Date getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Date tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
