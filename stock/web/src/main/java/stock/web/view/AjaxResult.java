/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

/**
 * @author yuanren.syr
 * @version $Id: AjaxResult.java, v 0.1 2016/1/24 0:09 yuanren.syr Exp $
 */
public class AjaxResult {

    public Object  value;

    public boolean success;

    public AjaxResult() {
    }

    public AjaxResult(Object value) {
        this.success = true;
        this.value = value;
    }

    public static AjaxResult valueOfFail() {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setSuccess(false);
        return ajaxResult;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
