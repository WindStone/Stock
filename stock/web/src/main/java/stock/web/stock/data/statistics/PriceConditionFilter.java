package stock.web.stock.data.statistics;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.LoggerUtil;
import stock.core.model.enums.PriceTypeEnum;
import stock.core.model.enums.SymbolEnum;
import stock.core.model.models.PriceFilterParam;

import java.util.Collections;
import java.util.List;

/**
 * Created by songyuanren on 2016/8/30.
 */
public class PriceConditionFilter implements ConditionFilter<PriceFilterParam> {

    public boolean filter(List<DailyTradeData> dtds, int index, final PriceFilterParam param) {
        if (index - param.getPriceWindow() - 1 < 0 || index >= dtds.size()) {
            return true;
        }
        final DailyTradeData dtd = dtds.get(index);
        dtds = dtds.subList(index - param.getPriceWindow() - 1, index - 1);
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

    public PriceFilterParam buildParam(JSONObject request, int index) {
        if (StringUtils.isEmpty(request.getString("priceType_" + index))
                || StringUtils.isEmpty(request.getString("priceSymbol_" + index))
                || StringUtils.isEmpty(request.getString("priceWindow_" + index))
                || StringUtils.isEmpty(request.getString("priceWindowType_" + index))
                || StringUtils.isEmpty(request.getString("pricePercent_" + index))) {
            return null;
        }
        PriceFilterParam priceFilterParam = new PriceFilterParam();
        priceFilterParam.setPriceType(PriceTypeEnum.getByCode(request.getString("priceType_" + index)));
        priceFilterParam.setPriceSymbol(SymbolEnum.getByCode(request.getString("priceSymbol_" + index)));
        priceFilterParam.setPriceWindow(Integer.parseInt(request.getString("priceWindow_" + index)));
        priceFilterParam.setPriceWindowType(PriceTypeEnum.getByCode(request
                .getString("priceWindowType_" + index)));
        priceFilterParam.setPricePercent(Double.parseDouble(request.getString("pricePercent_" + index)));
        return priceFilterParam;
    }
}
