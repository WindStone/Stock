package stock.core.model.models;

import stock.core.model.enums.PriceTypeEnum;
import stock.core.model.enums.SymbolEnum;

/**
 * Created by songyuanren on 2016/8/8.
 */
public class PriceFilterParam extends FilterParam {

    /**
     * 比较主体价格类型
     */
    private PriceTypeEnum priceType;

    /**
     * 比较符号
     */
    private SymbolEnum priceSymbol;

    /**
     * 最高价格类型
     */
    private PriceTypeEnum priceWindowType;

    /**
     * 最高价格窗口
     */
    private int priceWindow;

    /**
     * 比较比率
     */
    private double pricePercent;

    public PriceTypeEnum getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceTypeEnum priceType) {
        this.priceType = priceType;
    }

    public SymbolEnum getPriceSymbol() {
        return priceSymbol;
    }

    public void setPriceSymbol(SymbolEnum priceSymbol) {
        this.priceSymbol = priceSymbol;
    }

    public PriceTypeEnum getPriceWindowType() {
        return priceWindowType;
    }

    public void setPriceWindowType(PriceTypeEnum priceWindowType) {
        this.priceWindowType = priceWindowType;
    }

    public int getPriceWindow() {
        return priceWindow;
    }

    public void setPriceWindow(int priceWindow) {
        this.priceWindow = priceWindow;
    }

    public double getPricePercent() {
        return pricePercent;
    }

    public void setPricePercent(double pricePercent) {
        this.pricePercent = pricePercent;
    }

    public int getMaxWindow() {
        return priceWindow;
    }
}
