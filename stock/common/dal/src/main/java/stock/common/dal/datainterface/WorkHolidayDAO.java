/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.List;

import stock.common.dal.dataobject.WorkHolidayConfig;

/**
 * @author yuanren.syr
 * @version $Id: WorkHolidayDAO.java, v 0.1 2016/2/3 17:28 yuanren.syr Exp $
 */
public interface WorkHolidayDAO {
    List<WorkHolidayConfig> queryWorkHolidays();
}
