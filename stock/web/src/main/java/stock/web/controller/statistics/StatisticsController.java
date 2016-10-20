package stock.web.controller.statistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.common.dal.datainterface.DailyTradeDAO;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DateUtil;
import stock.common.util.DecimalUtil;
import stock.common.util.LoggerUtil;
import stock.core.model.enums.FilterGroupEnum;
import stock.core.model.enums.PriceTypeEnum;
import stock.core.model.enums.SymbolEnum;
import stock.core.model.models.FilterParam;
import stock.core.model.models.PriceFilterParam;
import stock.core.model.models.StockCodeFilterParam;
import stock.core.model.models.VolumeFilterParam;
import stock.web.stock.data.statistics.ConditionFilter;
import stock.web.utils.RasingRateUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by songyuanren on 2016/8/8.
 */
@Controller
public class StatisticsController {

    @Autowired
    private DailyTradeDAO dailyTradeDAO;

    @Autowired
    private
    @Resource(name = "conditionFilters")
    List<ConditionFilter> conditionFilters;

    private static final String[] ROW_KEY = {"收益率", "相对沪指收益率", "相对深综收益率"};
    private static final String[] COLUMN_KEY = {"oneDayInc", "fiveDayInc", "tenDayInc"};

    private static final String DEFAULT_VALUE = "[{title: \"收益率\", oneDayInc: \"\", fiveDayInc: \"\", tenDayInc: \"\"},\n"
            + "{title: \"相对沪指收益率\", oneDayInc: \"\", fiveDayInc: \"\", tenDayInc: \"\"},\n"
            + "{title: \"相对深综收益率\", oneDayInc: \"\", fiveDayInc: \"\", tenDayInc: \"\"}]";

    private static final int[] nextDays = {1, 5, 10};

    @RequestMapping(value = {"/statistics.html"})
    public String getStatistics(Map<String, Object> model) {
        return "statistics";
    }

    @RequestMapping(value = {"/statistics/statistics"})
    public String statisticsResultV2(Map<String, Object> model, HttpServletRequest httpServletRequest) {
        model.put("callback", httpServletRequest.getParameter("callback"));
        JSONObject request = JSONObject.parseObject(httpServletRequest.getParameter("request"));
        StockCodeFilterParam stockCodeFilterParam = buildStockCodeFilter(request);
        Integer[] conditionNum = request.getJSONArray("conditionNum").toArray(new Integer[0]);

        // 计算一个最大的处理窗口
        int window = 0;
        for (int i = 0; i < conditionNum.length; ++i) {
            for (int j = 0; j < conditionNum[i]; ++j) {
                FilterParam param = conditionFilters.get(i).buildParam(request, j);
                window = Math.max(window, param.getMaxWindow());
            }
        }

        String startDateStr = request.getString("startDate");
        Date startDate = DateUtil.parseSimpleDate(startDateStr);
        String prevStartDateStr = DateUtil.simpleFormat(DateUtils.addDays(startDate, -1));
        String endDateStr = request.getString("endDate");

        Map<Date, Double> shRaisingRate = loadRaisingMap("SH000001", prevStartDateStr, endDateStr);
        Map<Date, Double> szRaisingRate = loadRaisingMap("SZ399106", prevStartDateStr, endDateStr);

        // 初始化结果集
        List<Double>[][] raisingRateList = new List[3][3];
        List<JSONObject>[] debugRaisingRate = new List[3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                raisingRateList[i][j] = Lists.newArrayList();
            }
            debugRaisingRate[i] = Lists.newArrayList();
        }

        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();
        for (String stockCode : stockCodes) {
            if (stockCodeFiltered(stockCode, stockCodeFilterParam)) {
                continue;
            }
            List<DailyTradeData> dtds = getDailyTradeData(stockCode, prevStartDateStr, startDateStr, endDateStr, window);
            for (int k = window + 1; k < dtds.size(); ++k) {
                if (DateUtil.getDiffInDays(dtds.get(k).getCurrentDate(), startDate) < 0) {
                    continue;
                }
                // 按条件依次过滤
                boolean available = true;
                for (int i = 0; available && i < conditionNum.length; ++i) {
                    for (int j = 0; available && j < conditionNum[i]; ++j) {
                        FilterParam param = conditionFilters.get(i).buildParam(request, j);
                        if (conditionFilters.get(i).filter(dtds, k, param)) {
                            available = false;
                        }
                    }
                }
                if (available) {
                    for (int j = 0; j < nextDays.length; ++j) {
                        int nextDay = nextDays[j];
                        if (k + nextDay < dtds.size()) {
                            Double raisingRate = RasingRateUtils.getRasingRate(dtds.get(k),
                                    dtds.get(k + nextDay));
                            Date date = dtds.get(k).getCurrentDate();
                            raisingRateList[0][j].add(raisingRate);
                            raisingRateList[1][j].add(raisingRate - shRaisingRate.get(date));
                            raisingRateList[2][j].add(raisingRate - szRaisingRate.get(date));

                            debugRaisingRate[j].add(buildDebugResult(dtds.get(k), raisingRate,
                                    shRaisingRate, szRaisingRate));
                        }
                    }
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 3; ++i) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", ROW_KEY[i]);
            for (int j = 0; j < 3; ++j) {
                String resultStr = MessageFormat.format("{0}({1})",
                        DecimalUtil.formatPercent(DecimalUtil.average(raisingRateList[i][j])),
                        raisingRateList[i][j].size());
                jsonObject.put(COLUMN_KEY[j], resultStr);
            }
            jsonArray.add(jsonObject);
        }
        JSONArray jsonRaisingRate = new JSONArray();
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[0], "1日涨幅"));
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[1], "5日涨幅"));
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[2], "10日涨幅"));
        JSONObject result = new JSONObject();
        result.put("result", jsonArray);
        result.put("debug", jsonRaisingRate);
        model.put("json", result.toJSONString());
        return "/statistics/index";
    }

    public JSONObject buildDebugResult(DailyTradeData dtd, double raisingRate,
                                       Map<Date, Double> shRaisingRate, Map<Date, Double> szRaisingRate) {
        JSONObject jsonObject = new JSONObject();
        Date date = dtd.getCurrentDate();
        jsonObject.put("raisingRate", DecimalUtil.formatPercent(raisingRate));
        jsonObject.put("date", DateUtil.simpleFormat(date));
        jsonObject.put("stockCode", dtd.getStockCode());
        jsonObject.put("stockName", dtd.getStockName());
        jsonObject.put("shRaisingRate",
                DecimalUtil.formatPercent(shRaisingRate.get(date)));
        jsonObject.put("szRaisingRate",
                DecimalUtil.formatPercent(szRaisingRate.get(date)));
        return jsonObject;
    }

    private List<DailyTradeData> getDailyTradeData(String stockCode, String prevStartDateStr, String startDateStr,
                                                   String endDateStr, int window) {
        List<DailyTradeData> dtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                startDateStr, endDateStr);
        List<DailyTradeData> dtds2 = dailyTradeDAO.queryByPrevKTradingData(stockCode,
                prevStartDateStr, window + 1);
        if (!CollectionUtils.isEmpty(dtds) && !CollectionUtils.isEmpty(dtds2)) {
            dtds.addAll(dtds2);
        }
        Collections.sort(dtds, new Comparator<DailyTradeData>() {
            public int compare(DailyTradeData dtd1, DailyTradeData dtd2) {
                return dtd1.getCurrentDate().compareTo(dtd2.getCurrentDate());
            }
        });
        return dtds;
    }

    @RequestMapping(value = {"/statistics/index.html"})
    public String statisticsResult(Map<String, Object> model, HttpServletRequest httpServletRequest) {
        JSONObject request = JSONObject.parseObject(httpServletRequest.getParameter("request"));
        StockCodeFilterParam stockCodeFilterParam = buildStockCodeFilter(request);
        VolumeFilterParam volumeFilterParam = buildVolumeFilter(request);
        PriceFilterParam priceFilterParam = buildPriceFilter(request);
        model.put("callback", httpServletRequest.getParameter("callback"));
        if (volumeFilterParam == null && priceFilterParam == null) {
            model.put("json", DEFAULT_VALUE);
            return "/statistics/index";
        }
        int window = volumeFilterParam == null ? 0 : volumeFilterParam.getVolumeWindow();
        if (priceFilterParam != null) {
            window = Math.max(window, priceFilterParam.getPriceWindow());
        }

        Date startDate = DateUtil.parseJsonDate(request.getString("startDate"));
        if (startDate == null) {
            startDate = DateUtil.parseSimpleDate(request.getString("startDate"));
        }
        String startDateStr = DateUtil.simpleFormat(startDate);
        String prevStartDateStr = DateUtil.simpleFormat(DateUtils.addDays(startDate, -1));
        Date endDate = DateUtil.parseJsonDate(request.getString("endDate"));
        if (endDate == null) {
            endDate = DateUtil.parseSimpleDate(request.getString("endDate"));
        }
        String endDateStr = DateUtil.simpleFormat(endDate);

        List<Double>[][] raisingRateList = new List[3][3];
        List<JSONObject>[] debugRaisingRate = new List[3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                raisingRateList[i][j] = Lists.newArrayList();
            }
            debugRaisingRate[i] = Lists.newArrayList();
        }
        int[] nextDays = {1, 5, 10};

        Map<Date, Double> shRaisingRate = loadRaisingMap("SH000001", prevStartDateStr, endDateStr);
        Map<Date, Double> szRaisingRate = loadRaisingMap("SZ399106", prevStartDateStr, endDateStr);

        List<String> stockCodes = dailyTradeDAO.queryForStockCodes();

        for (String stockCode : stockCodes) {
            if (stockCodeFiltered(stockCode, stockCodeFilterParam)) {
                continue;
            }
            List<DailyTradeData> dtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                    startDateStr, endDateStr);
            List<DailyTradeData> dtds2 = dailyTradeDAO.queryByPrevKTradingData(stockCode,
                    prevStartDateStr, window + 1);
            if (!CollectionUtils.isEmpty(dtds) && !CollectionUtils.isEmpty(dtds2)) {
                dtds.addAll(dtds2);
            }
            if (!CollectionUtils.isEmpty(dtds)) {
                Collections.sort(dtds, new Comparator<DailyTradeData>() {
                    public int compare(DailyTradeData dtd1, DailyTradeData dtd2) {
                        return dtd1.getCurrentDate().compareTo(dtd2.getCurrentDate());
                    }
                });
                for (int i = window + 1; i < dtds.size(); ++i) {
                    if (DateUtil.getDiffInDays(dtds.get(i).getCurrentDate(), startDate) < 0) {
                        continue;
                    }
                    if (volumeFilterParam != null
                            && volumeFiltered(volumeFilterParam,
                            dtds.subList(i - volumeFilterParam.getVolumeWindow() - 1, i - 1),
                            dtds.get(i))) {
                        continue;
                    }
                    if (priceFilterParam != null
                            && priceFiltered(priceFilterParam,
                            dtds.subList(i - priceFilterParam.getPriceWindow() - 1, i - 1),
                            dtds.get(i))) {
                        continue;
                    }
                    for (int j = 0; j < nextDays.length; ++j) {
                        int nextDay = nextDays[j];
                        if (i + nextDay < dtds.size()) {
                            Double raisingRate = RasingRateUtils.getRasingRate(dtds.get(i),
                                    dtds.get(i + nextDay));
                            Date date = dtds.get(i).getCurrentDate();
                            String stockName = dtds.get(i).getStockName();
                            raisingRateList[0][j].add(raisingRate);
                            raisingRateList[1][j].add(raisingRate - shRaisingRate.get(date));
                            raisingRateList[2][j].add(raisingRate - szRaisingRate.get(date));

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("raisingRate", DecimalUtil.formatPercent(raisingRate));
                            jsonObject.put("date", DateUtil.simpleFormat(date));
                            jsonObject.put("stockCode", stockCode);
                            jsonObject.put("stockName", stockName);
                            jsonObject.put("shRaisingRate",
                                    DecimalUtil.formatPercent(shRaisingRate.get(date)));
                            jsonObject.put("szRaisingRate",
                                    DecimalUtil.formatPercent(szRaisingRate.get(date)));
                            debugRaisingRate[j].add(jsonObject);
                        }
                    }
                }
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 3; ++i) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", ROW_KEY[i]);
            for (int j = 0; j < 3; ++j) {
                String resultStr = MessageFormat.format("{0}({1})",
                        DecimalUtil.formatPercent(DecimalUtil.average(raisingRateList[i][j])),
                        raisingRateList[i][j].size());
                jsonObject.put(COLUMN_KEY[j], resultStr);
            }
            jsonArray.add(jsonObject);
        }

        JSONArray jsonRaisingRate = new JSONArray();
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[0], "1日涨幅"));
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[1], "5日涨幅"));
        jsonRaisingRate.addAll(randomLoadResult(debugRaisingRate[2], "10日涨幅"));
        JSONObject result = new JSONObject();
        result.put("result", jsonArray);
        result.put("debug", jsonRaisingRate);
        model.put("json", result.toJSONString());
        return "/statistics/index";
    }

    private List<JSONObject> randomLoadResult(List<JSONObject> raisingRateList, String type) {
        Random rand = new Random(new Date().getTime());
        List<JSONObject> result = Lists.newArrayList();
        for (int i = 0; i < 100 && !CollectionUtils.isEmpty(raisingRateList); ++i) {
            int index = rand.nextInt(raisingRateList.size());
            raisingRateList.get(index).put("type", type);
            result.add(raisingRateList.get(index));
            raisingRateList.remove(index);
        }
        return result;
    }

    private Map<Date, Double> loadRaisingMap(String stockCode, String prevStartDateStr,
                                             String endDateStr) {
        Map<Date, Double> result = Maps.newHashMap();
        List<DailyTradeData> shDtds = dailyTradeDAO.queryByIntervalTradingData(stockCode,
                prevStartDateStr, endDateStr);
        for (int i = 1; i < shDtds.size(); ++i) {
            result.put(shDtds.get(i).getCurrentDate(),
                    RasingRateUtils.getRasingRate(shDtds.get(i - 1), shDtds.get(i)));
        }
        return result;
    }

    /**
     * true  表示应该被过滤掉
     * false 表示不应该被过滤掉
     *
     * @param param
     * @param dtds
     * @param dtd
     * @return
     */
    private boolean volumeFiltered(VolumeFilterParam param, List<DailyTradeData> dtds,
                                   DailyTradeData dtd) {
        List<Double> volumes = Lists.transform(dtds, new Function<DailyTradeData, Double>() {
            public Double apply(DailyTradeData dailyTradeData) {
                return dailyTradeData.getTradingVolume();
            }
        });
        double avgVolume = DecimalUtil.average(volumes);

        return !param.getVolumeSymbol().getSymbol()
                .compare(dtd.getTradingVolume(), avgVolume, param.getVolumePercent());
    }

    /**
     * true  表示应该被过滤掉
     * false 表示不应该被过滤掉
     *
     * @param param
     * @param dtds
     * @param dtd
     * @return
     */
    private boolean priceFiltered(final PriceFilterParam param, List<DailyTradeData> dtds,
                                  final DailyTradeData dtd) {
        List<Double> prices = Lists.transform(dtds, new Function<DailyTradeData, Double>() {
            public Double apply(DailyTradeData dailyTradeData) {
                try {
                    return (Double) param.getPriceWindowType().getPriceMethod()
                            .invoke(dailyTradeData, dtd);
                } catch (Exception e) {
                    LoggerUtil.error("Get specific type=[{0}] from dtd error",
                            param.getPriceWindowType(), e);
                    return 0.0;
                }
            }
        });

        Double maxPrice = Collections.max(prices);
        Double price = 0.0;
        try {
            price = (Double) param.getPriceType().getPriceMethod().invoke(dtd, dtd);
        } catch (Exception e) {
            LoggerUtil.error("Get specific type=[{0}] from dtd error", param.getPriceWindowType(),
                    e);
            return true;
        }

        return !param.getPriceSymbol().getSymbol()
                .compare(price, maxPrice, param.getPricePercent());
    }

    /**
     * true  表示应该被过滤掉
     * false 表示不应该被过滤掉
     *
     * @param stockCode
     * @param stockCodeFilterParam
     * @return
     */
    private boolean stockCodeFiltered(String stockCode, StockCodeFilterParam stockCodeFilterParam) {
        if (!(StringUtils.startsWith(stockCode, "SH60")
                || StringUtils.startsWith(stockCode, "SZ00") || StringUtils.startsWith(stockCode,
                "SZ300"))) {
            return true;
        }
        for (String filteredStockCode : stockCodeFilterParam.getFilterCodes()) {
            if (StringUtils.equals(stockCode, filteredStockCode)) {
                return true;
            }
        }
        for (FilterGroupEnum filteredGroup : stockCodeFilterParam.getFilterGroups()) {
            if (StringUtils.startsWith(stockCode, filteredGroup.getPrefix())) {
                return true;
            }
        }
        return false;
    }

    private StockCodeFilterParam buildStockCodeFilter(JSONObject request) {
        StockCodeFilterParam stockCodeFilterParam = new StockCodeFilterParam();

        String filterGroupsStr = request.getString("filterGroup");

        List<String> filterGroups = Lists.newArrayList();
        if (!StringUtils.isEmpty(filterGroupsStr)) {
            filterGroups.addAll(Lists.newArrayList(StringUtils.split(filterGroupsStr, ",")));
        }
        stockCodeFilterParam.setFilterGroups(Lists.transform(filterGroups,
                new Function<String, FilterGroupEnum>() {
                    public FilterGroupEnum apply(String s) {
                        return FilterGroupEnum.getByCode(s);
                    }
                }));

        String filterCodeStr = request.getString("specificFilterCode");
        if (!StringUtils.isEmpty(filterCodeStr)) {
            stockCodeFilterParam.setFilterCodes(Lists.newArrayList(StringUtils.split(filterCodeStr,
                    ",")));
        } else {
            stockCodeFilterParam.setFilterCodes(Lists.<String>newArrayList());
        }

        return stockCodeFilterParam;
    }

    private VolumeFilterParam buildVolumeFilter(JSONObject request) {
        VolumeFilterParam volumeFilterParam = new VolumeFilterParam();
        if (StringUtils.isEmpty(request.getString("volumeSymbol"))
                || StringUtils.isEmpty(request.getString("volumeWindow"))
                || StringUtils.isEmpty(request.getString("volumePercent"))) {
            return null;
        }
        volumeFilterParam.setVolumeSymbol(SymbolEnum.getByCode(request.getString("volumeSymbol")));
        volumeFilterParam.setVolumeWindow(Integer.parseInt(request.getString("volumeWindow")));
        volumeFilterParam.setVolumePercent(Double.parseDouble(request.getString("volumePercent")));
        return volumeFilterParam;
    }

    private PriceFilterParam buildPriceFilter(JSONObject request) {
        if (StringUtils.isEmpty(request.getString("priceType"))
                || StringUtils.isEmpty(request.getString("priceSymbol"))
                || StringUtils.isEmpty(request.getString("priceWindow"))
                || StringUtils.isEmpty(request.getString("priceWindowType"))
                || StringUtils.isEmpty(request.getString("pricePercent"))) {
            return null;
        }
        PriceFilterParam priceFilterParam = new PriceFilterParam();
        priceFilterParam.setPriceType(PriceTypeEnum.getByCode(request.getString("priceType")));
        priceFilterParam.setPriceSymbol(SymbolEnum.getByCode(request.getString("priceSymbol")));
        priceFilterParam.setPriceWindow(Integer.parseInt(request.getString("priceWindow")));
        priceFilterParam.setPriceWindowType(PriceTypeEnum.getByCode(request
                .getString("priceWindowType")));
        priceFilterParam.setPricePercent(Double.parseDouble(request.getString("pricePercent")));
        return priceFilterParam;
    }
}
