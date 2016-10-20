/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.core.model.models;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author yuanren.syr
 * @version $Id: SerializableModel.java, v 0.1 2016/1/31 21:45 yuanren.syr Exp $
 */
public abstract class SerializableModel {

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
