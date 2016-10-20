package stock.web.stock.data.statistics;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.DecimalUtil;
import stock.core.model.enums.SymbolEnum;
import stock.core.model.models.VolumeFilterParam;

import java.util.List;

/**
 * Created by songyuanren on 2016/8/31.
 */
public class VolumeConditionFilter implements ConditionFilter<VolumeFilterParam> {

    public boolean filter(List<DailyTradeData> dtds, int index, VolumeFilterParam param) {
        if (index - param.getVolumeWindow() - 1 < 0) {
            return true;
        }
        final DailyTradeData dtd = dtds.get(index);
        dtds = dtds.subList(index - param.getVolumeWindow() - 1, index - 1);

        List<Double> volumes = Lists.transform(dtds, new Function<DailyTradeData, Double>() {
            public Double apply(DailyTradeData dailyTradeData) {
                return dailyTradeData.getTradingVolume();
            }
        });
        double avgVolume = DecimalUtil.average(volumes);

        return !param.getVolumeSymbol().getSymbol()
                .compare(dtd.getTradingVolume(), avgVolume, param.getVolumePercent());
    }

    public VolumeFilterParam buildParam(JSONObject request, int index) {
        VolumeFilterParam volumeFilterParam = new VolumeFilterParam();
        if (StringUtils.isEmpty(request.getString("volumeSymbol_" + index))
                || StringUtils.isEmpty(request.getString("volumeWindow_" + index))
                || StringUtils.isEmpty(request.getString("volumePercent_" + index))) {
            return null;
        }
        volumeFilterParam.setVolumeSymbol(SymbolEnum.getByCode(request.getString("volumeSymbol_" + index)));
        volumeFilterParam.setVolumeWindow(Integer.parseInt(request.getString("volumeWindow_" + index)));
        volumeFilterParam.setVolumePercent(Double.parseDouble(request.getString("volumePercent_" + index)));
        return volumeFilterParam;
    }
}
