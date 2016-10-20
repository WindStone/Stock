/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.processor.plate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.datainterface.PlateTradeDAO;
import stock.common.dal.datainterface.SequenceGenerator;
import stock.common.dal.dataobject.DailyPlateData;
import stock.common.dal.enums.SequenceEnum;
import stock.common.util.DateUtil;
import stock.web.utils.HolidayUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author yuanren.syr
 * @version $Id: AbstractPlateProcessor.java, v 0.1 2016/2/2 0:01 yuanren.syr Exp $
 */
public abstract class AbstractPlateProcessor implements PlateProcessor {

    protected PlateTradeDAO     plateTradeDAO;

    protected DailyTradeDAO     dailyTradeDAO;

    protected String            processName;

    protected SequenceGenerator sequenceGenerator;

    public List<DailyPlateData> getPlateDailyProcessResult(String plateName,
                                                           List<String> stockCodes, Date startDate,
                                                           Date endDate) {
        List<DailyPlateData> result = Lists.newArrayList();
        List<DailyPlateData> dailyPlateDatas = plateTradeDAO.queryIntervalByNameAndDate(plateName,
            processName, startDate, endDate);
        Map<String, DailyPlateData> map = Maps.newHashMap();
        for (DailyPlateData dailyPlateData : dailyPlateDatas) {
            map.put(DateUtil.simpleFormat(dailyPlateData.getTradingDate()), dailyPlateData);
        }

        for (Date tmpDate = new Date(startDate.getTime()); DateUtil.getDiffInDays(tmpDate, endDate) <= 0; tmpDate = DateUtils
            .addDays(tmpDate, 1)) {
            if (!HolidayUtils.isAvailable(tmpDate)) {
                continue;
            }
            DailyPlateData dailyPlateData = map.get(DateUtil.simpleFormat(tmpDate));
            if (dailyPlateData == null) {
                dailyPlateData = processPlate(plateName, stockCodes, tmpDate);
                if (dailyPlateData != null) {
                    dailyPlateData.setProcessName(processName);
                    dailyPlateData.setPlateDailyId(sequenceGenerator
                        .getSequence(SequenceEnum.STOCK_PLATE_SEQ));
                    plateTradeDAO.insert(dailyPlateData);
                }
            } else {
                String plateDailyId = dailyPlateData.getPlateDailyId();
                if (!StringUtils.equals(dailyPlateData.getParam(), getParam())) {
                    dailyPlateData = processPlate(plateName, stockCodes, tmpDate);
                    if (dailyPlateData != null) {
                        dailyPlateData.setPlateDailyId(plateDailyId);
                        plateTradeDAO.update(dailyPlateData);
                    }
                }
            }
            if (dailyPlateData != null) {
                result.add(dailyPlateData);
            }
        }
        return result;
    }

    protected void printResultList(Date calcDate, List<String> stockCodes) {
        System.out.println(DateUtil.simpleFormat(calcDate) + ":");
        for (String stockCode : stockCodes) {
            System.out.print(stockCode + ",");
        }
        System.out.println("");
    }

    protected abstract String getParam();

    protected abstract DailyPlateData processPlate(String plateName, List<String> stockCodes,
                                                   Date date);

    public void setPlateTradeDAO(PlateTradeDAO plateTradeDAO) {
        this.plateTradeDAO = plateTradeDAO;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    public void setDailyTradeDAO(DailyTradeDAO dailyTradeDAO) {
        this.dailyTradeDAO = dailyTradeDAO;
    }
}
