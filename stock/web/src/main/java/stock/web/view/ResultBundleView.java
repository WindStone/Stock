/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.view;

/**
 * @author yuanren.syr
 * @version $Id: ResultBundleView.java, v 0.1 2016/1/11 2:05 yuanren.syr Exp $
 */
public class ResultBundleView {

    public String           date;

    public FileBundleView[] fileBundleViews;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FileBundleView[] getFileBundleViews() {
        return fileBundleViews;
    }

    public void setFileBundleViews(FileBundleView[] fileBundleViews) {
        this.fileBundleViews = fileBundleViews;
    }
}
