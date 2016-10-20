/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.datainterface;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyPlateData;

/**
 * @author yuanren.syr
 * @version $Id: PlateTradeDAO.java, v 0.1 2016/2/2 0:08 yuanren.syr Exp $
 */
public interface PlateTradeDAO {

    boolean insert(DailyPlateData dailyPlateData);

    DailyPlateData queryByNameAndDate(String plateName, String processName, Date calcDate);

    List<DailyPlateData> queryIntervalByNameAndDate(String plateName, String processName,
                                                    Date startDate, Date endDate);

    int update(DailyPlateData dailyPlateData);

}
