package stock.web.stock.data.statistics;

import com.alibaba.fastjson.JSONObject;
import stock.common.dal.dataobject.DailyTradeData;
import stock.core.model.models.FilterParam;

import java.util.List;

/**
 * Created by songyuanren on 2016/8/30.
 */
public interface ConditionFilter<T extends FilterParam> {

    /**
     * 传入一组时序数据，判断是否需要过滤掉
     *
     * @param dtds  时序数据
     * @param index 带处理的序号
     * @param param 过滤参数
     * @return
     */
    boolean filter(List<DailyTradeData> dtds, int index, T param);

    /**
     * 基于请求数据构造第index个过滤条件参数
     *
     * @param request 请求数据
     * @param index   序号
     * @return
     */
    T buildParam(JSONObject request, int index);
}
