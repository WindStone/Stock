package stock.core.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by songyuanren on 2016/8/8.
 */
public enum FilterGroupEnum {

    /** 上证A股 */
    SH_SHARE("SH60"),
    /** 深圳A股 */
    SZ_SHARE("SZ00"),
    /** 深圳创业板 */
    SZ_GEM("SZ300");

    private String prefix;

    FilterGroupEnum(String prefix) {
        this.prefix = prefix;
    }

    public static FilterGroupEnum getByCode(String code) {
        for (FilterGroupEnum value : FilterGroupEnum.values()) {
            if (StringUtils.equalsIgnoreCase(code, value.toString())) {
                return value;
            }
        }
        return null;
    }

    public String getPrefix() {
        return prefix;
    }
}
