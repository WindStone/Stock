/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.dal.enums;

/**
 * @author yuanren.syr
 * @version $Id: SequenceEnum.java, v 0.1 2015/12/6 16:48 yuanren.syr Exp $
 */
public enum SequenceEnum {
    STOCK_DAILY_SEQ("stock_daily_id", "", 16);

    private String name;

    private String head;

    private int    length;

    SequenceEnum(String name, String head, int length) {
        this.name = name;
        this.head = head;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getHead() {
        return head;
    }

    public int getLength() {
        return length;
    }
}
