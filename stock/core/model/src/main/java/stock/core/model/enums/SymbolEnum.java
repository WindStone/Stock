package stock.core.model.enums;

import org.apache.commons.lang3.StringUtils;

import stock.core.model.symbol.CompareSymbol;
import stock.core.model.symbol.GeSymbol;
import stock.core.model.symbol.GtSymbol;
import stock.core.model.symbol.LeSymbol;
import stock.core.model.symbol.LtSymbol;

/**
 * Created by songyuanren on 2016/8/8.
 */
public enum SymbolEnum {
    /** 大于 */
    GT("gt", "大于", new GtSymbol()),
    /** 大于等于 */
    GE("ge", "大于等于", new GeSymbol()),
    /** 小于 */
    LT("lt", "小于", new LtSymbol()),
    /** 小于等于 */
    LE("le", "小于等于", new LeSymbol());

    private String        code;
    private String        desc;
    private CompareSymbol symbol;

    SymbolEnum(String code, String desc, CompareSymbol symbol) {
        this.code = code;
        this.desc = desc;
        this.symbol = symbol;
    }

    public static SymbolEnum getByCode(String code) {
        for (SymbolEnum value : SymbolEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public CompareSymbol getSymbol() {
        return symbol;
    }
}
