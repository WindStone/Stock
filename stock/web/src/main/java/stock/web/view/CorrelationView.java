/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

import java.util.List;

/**
 * @author yuanren.syr
 * @version $Id: CorrelationView.java, v 0.1 2016/3/13 14:41 yuanren.syr Exp $
 */
public class CorrelationView {

    private String       stockCodeA;

    private String       stockNameA;

    private String       stockCodeB;

    private String       stockNameB;

    private double       correlation;

    private List<String> codeAValues;

    private List<String> codeBValues;

    private double       posibility;

    public String getStockCodeA() {
        return stockCodeA;
    }

    public void setStockCodeA(String stockCodeA) {
        this.stockCodeA = stockCodeA;
    }

    public String getStockNameA() {
        return stockNameA;
    }

    public void setStockNameA(String stockNameA) {
        this.stockNameA = stockNameA;
    }

    public String getStockCodeB() {
        return stockCodeB;
    }

    public void setStockCodeB(String stockCodeB) {
        this.stockCodeB = stockCodeB;
    }

    public String getStockNameB() {
        return stockNameB;
    }

    public void setStockNameB(String stockNameB) {
        this.stockNameB = stockNameB;
    }

    public List<String> getCodeAValues() {
        return codeAValues;
    }

    public void setCodeAValues(List<String> codeAValues) {
        this.codeAValues = codeAValues;
    }

    public List<String> getCodeBValues() {
        return codeBValues;
    }

    public void setCodeBValues(List<String> codeBValues) {
        this.codeBValues = codeBValues;
    }

    public double getPosibility() {
        return posibility;
    }

    public void setPosibility(double posibility) {
        this.posibility = posibility;
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }
}
