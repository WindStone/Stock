package stock.core.model.enums;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import stock.common.dal.dataobject.DailyTradeData;
import stock.common.util.LoggerUtil;

/**
 * Created by songyuanren on 2016/8/8.
 */
public enum PriceTypeEnum {
    /** 收盘价 */
    CLOSING_PRICE("getClosingPrice"),
    /** 开盘价 */
    OPENING_PRICE("getOpeningPrice"),
    /** 最高价 */
    HIGHEST_PRICE("getHighestPrice"),
    /** 最低价 */
    LOWEST_PRICE("getLowestPrice");

    private Method priceMethod;

    PriceTypeEnum(String methodName) {
        try {
            this.priceMethod = DailyTradeData.class.getDeclaredMethod(methodName,
                DailyTradeData.class);
        } catch (Exception e) {
            LoggerUtil.error("Can't initialize Method of PriceTypeEnum", e);
        }
    }

    public static PriceTypeEnum getByCode(String code) {
        code = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(code), '_');
        for (PriceTypeEnum value : PriceTypeEnum.values()) {
            if (StringUtils.equalsIgnoreCase(code, value.toString())) {
                return value;
            }
        }
        return null;
    }

    public Method getPriceMethod() {
        return priceMethod;
    }
}
