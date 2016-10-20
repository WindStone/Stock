/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.dataobject;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yuanren.syr
 * @version $Id: WorkHolidayConfig.java, v 0.1 2016/2/3 17:28 yuanren.syr Exp $
 */
public class WorkHolidayConfig {

    private Date   day;

    private String holiday;

    private Date   gmtCreate;

    private Date   gmtModified;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public boolean isHoliday() {
        return StringUtils.equals(holiday, "T");
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
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
}
