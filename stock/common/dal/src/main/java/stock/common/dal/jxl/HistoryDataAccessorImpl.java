/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.jxl;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.datainterface.HistoryDataAccessor;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.PathUtil;

import com.csvreader.CsvReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * @author yuanren.syr
 * @version $Id: HistoryDataAccessorImpl.java, v 0.1 2015/11/18 0:05 yuanren.syr Exp $
 */
public class HistoryDataAccessorImpl implements HistoryDataAccessor {

    private Map<String, Map<String, DailyTradeData>> dailyPlateHistory = Maps.newHashMap();

    private Map<String, DailyTradeData>              shExpHistory      = Maps.newHashMap();

    private Map<String, DailyTradeData>              szExpHistory      = Maps.newHashMap();

    private static final String                      SHENWAN_PLATE     = "/plate/shenwan/";

    private static final String                      MIN_FIVE          = "/5min/";

    private static final String                      SUFFIX            = ".xls";

    public DailyTradeData getDailySHExp(Date date) {
        DailyTradeData dtd = shExpHistory.get(DateUtil.slashDateFormat(date));
        if (dtd == null) {
            loadDailyExp(shExpHistory, "/2014/SH1A0001.csv");
        }
        return shExpHistory.get(DateUtil.slashDateFormat(date));
    }

    public DailyTradeData getDailySZExp(Date date) {
        DailyTradeData dtd = szExpHistory.get(DateUtil.slashDateFormat(date));
        if (dtd == null) {
            loadDailyExp(szExpHistory, "/2014/SZ399001.csv");
        }
        return szExpHistory.get(DateUtil.slashDateFormat(date));
    }

    public List<DailyTradeData> get5MinTrade(File f) {
        List<DailyTradeData> dtds = Lists.newArrayList();
        try {
            CsvReader csvReader = new CsvReader(new FileReader(f));
            while (csvReader.readRecord()) {
                String[] cells = csvReader.getValues();
                DailyTradeData dtd = new DailyTradeData();
                String fileName = StringUtils.substringAfterLast(f.getAbsolutePath(),
                    File.separator);
                dtd.setStockCode(StringUtils.removeEnd(fileName, ".csv"));
                dtd.setCurrentDate(DateUtil.parseDate(cells[0] + " " + cells[1]));
                dtd.setDate(cells[0]);
                dtd.setTime(cells[1]);
                dtd.setOpeningPrice(Double.parseDouble(cells[2]));
                dtd.setHighestPrice(Double.parseDouble(cells[3]));
                dtd.setLowestPrice(Double.parseDouble(cells[4]));
                dtd.setClosingPrice(Double.parseDouble(cells[5]));
                dtds.add(dtd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtds;
    }

    public List<DailyTradeData> getDailyTradeData(File f) {
        List<DailyTradeData> dtds = Lists.newArrayList();
        try {
            CsvReader csvReader = new CsvReader(new FileReader(f));
            while (csvReader.readRecord()) {
                String[] cells = csvReader.getValues();
                DailyTradeData dtd = new DailyTradeData();
                String fileName = StringUtils.substringAfterLast(f.getAbsolutePath(),
                    File.separator);
                dtd.setStockCode(StringUtils.removeEnd(fileName, ".csv"));
                //                dtd.setStockCode("SH000001");
                dtd.setCurrentDate(DateUtil.parseSlashDate(cells[0]));
                dtd.setDate(cells[0]);
                dtd.setTime(cells[1]);

                dtd.setOpeningPrice(Double.parseDouble(cells[2]));
                dtd.setHighestPrice(Double.parseDouble(cells[3]));
                dtd.setLowestPrice(Double.parseDouble(cells[4]));
                dtd.setClosingPrice(Double.parseDouble(cells[5]));
                dtd.setTradingVolume(Double.parseDouble(cells[6]));
                try {
                    dtd.setTradingAmount(Double.parseDouble(cells[7]));
                } catch (NumberFormatException e) {
                    dtd.setTradingAmount(-1);
                }
                dtds.add(dtd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtds;
    }

    public DailyTradeData getDailyTradeDate(String stockCode, Date date) {
        return null;
    }

    private void loadDailyExp(Map<String, DailyTradeData> map, String f) {
        synchronized (map) {
            try {
                String fileName = PathUtil.getDataPath() + MIN_FIVE + f;
                CsvReader csvReader = new CsvReader(fileName);
                while (csvReader.readRecord()) {
                    String[] cells = csvReader.getValues();
                    if (StringUtils.equals(cells[1], "15:00")) {
                        DailyTradeData dtd = new DailyTradeData();
                        dtd.setCurrentDate(DateUtil.parseSlashDate(cells[0]));
                        dtd.setOpeningPrice(Double.parseDouble(cells[2]));
                        dtd.setHighestPrice(Double.parseDouble(cells[3]));
                        dtd.setLowestPrice(Double.parseDouble(cells[4]));
                        dtd.setClosingPrice(Double.parseDouble(cells[5]));
                        map.put(cells[0], dtd);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public DailyTradeData getDailyPlateHistoryByName(String name, Date date) {
        if (dailyPlateHistory == null || dailyPlateHistory.get(name) == null) {
            loadDailyPlateHistory(name);
        }
        if (dailyPlateHistory == null) {
            return null;
        }
        Map<String, DailyTradeData> plate = dailyPlateHistory.get(name);
        if (plate == null) {
            return null;
        }
        return plate.get(DateUtil.slashDateFormat(date));
    }

    private void loadDailyPlateHistory(String name) {
        synchronized (dailyPlateHistory) {
            if (dailyPlateHistory == null) {
                dailyPlateHistory = Maps.newHashMap();
            }
            if (dailyPlateHistory.get(name) != null) {
                return;
            }
            try {
                Map<String, DailyTradeData> map = Maps.newHashMap();
                dailyPlateHistory.put(name, map);
                String fileName = PathUtil.getDataPath() + SHENWAN_PLATE + name + SUFFIX;
                Workbook workbook = Workbook.getWorkbook(new File(fileName));
                Sheet sheet = workbook.getSheet(0);
                for (int i = 1; i < sheet.getRows(); ++i) {
                    Cell[] cells = sheet.getRow(i);
                    if (cells[2] instanceof DateCell) {
                        DailyTradeData dtd = new DailyTradeData();
                        Date date = ((DateCell) cells[2]).getDate();
                        dtd.setCurrentDate(date);
                        dtd.setOpeningPrice(Double.parseDouble(cells[3].getContents()));
                        dtd.setHighestPrice(Double.parseDouble(cells[4].getContents()));
                        dtd.setLowestPrice(Double.parseDouble(cells[5].getContents()));
                        dtd.setClosingPrice(Double.parseDouble(cells[6].getContents()));
                        dtd.setTradingVolume(((NumberCell) cells[7]).getValue());
                        dtd.setTradingAmount(((NumberCell) cells[8]).getValue());
                        map.put(DateUtil.slashDateFormat(date), dtd);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
