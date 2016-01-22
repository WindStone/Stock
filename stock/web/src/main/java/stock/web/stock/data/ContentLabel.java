/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data;

/**
 * @author yuanren.syr
 * @version $Id: ContentLabel.java, v 0.1 2016/1/18 22:22 yuanren.syr Exp $
 */
public class ContentLabel {

    int    column;
    String content;

    public ContentLabel(String content) {
        this(1, content);
    }

    public ContentLabel(int column, String content) {
        this.column = column;
        this.content = content;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
