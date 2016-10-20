/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.plate;

import java.util.Date;
import java.util.List;

import stock.common.dal.dataobject.DailyPlateData;

/**
 * @author yuanren.syr
 * @version $Id: PlateProcessor.java, v 0.1 2016/2/1 23:59 yuanren.syr Exp $
 */
public interface PlateProcessor {

    List<DailyPlateData> getPlateDailyProcessResult(String plateName, List<String> stockCodes,
                                                    Date startDate, Date endDate);

    String getProcessName();

}
