/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.config;

/**
 * @author yuanren.syr
 * @version $Id: DMAConfig.java, v 0.1 2016/2/2 17:21 yuanren.syr Exp $
 */
public class DMAConfig extends AlgorithmConfig {

    private int shortAvg = 5;

    private int longAvg  = 89;

    private int M        = 36;

    public int getShortAvg() {
        return shortAvg;
    }

    public void setShortAvg(int shortAvg) {
        this.shortAvg = shortAvg;
    }

    public int getLongAvg() {
        return longAvg;
    }

    public void setLongAvg(int longAvg) {
        this.longAvg = longAvg;
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }
}
