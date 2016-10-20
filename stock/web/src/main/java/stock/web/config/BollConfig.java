/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.config;

/**
 * @author yuanren.syr
 * @version $Id: BollConfig.java, v 0.1 2016/2/2 17:34 yuanren.syr Exp $
 */
public class BollConfig extends AlgorithmConfig {

    private int n = 15;

    private int k = 2;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
}
