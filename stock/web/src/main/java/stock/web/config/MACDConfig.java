/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.config;

/**
 * @author yuanren.syr
 * @version $Id: MACDConfig.java, v 0.1 2016/2/2 17:35 yuanren.syr Exp $
 */
public class MACDConfig extends AlgorithmConfig {

    private int ema1   = 9;

    private int ema2   = 21;

    private int dea    = 6;

    private int factor = 2;

    public int getEma1() {
        return ema1;
    }

    public void setEma1(int ema1) {
        this.ema1 = ema1;
    }

    public int getEma2() {
        return ema2;
    }

    public void setEma2(int ema2) {
        this.ema2 = ema2;
    }

    public int getDea() {
        return dea;
    }

    public void setDea(int dea) {
        this.dea = dea;
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
}
