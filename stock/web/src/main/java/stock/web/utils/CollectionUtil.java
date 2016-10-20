/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * @author yuanren.syr
 * @version $Id: CollectionUtil.java, v 0.1 2016/1/17 16:40 yuanren.syr Exp $
 */
public class CollectionUtil {

    public static <T> T fetchLastElement(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        Iterator<T> iter = collection.iterator();
        T ret = null;
        while (iter.hasNext()) {
            ret = iter.next();
        }
        return ret;
    }

    public static <T> T fetchDefault(T[] list, int index, T defaultValue) {
        if (list == null) {
            return defaultValue;
        }
        if (index < 0 || index >= list.length) {
            return defaultValue;
        }
        return list[index];
    }

    public static <T> T fetchDefault(List<T> list, int index, T defaultValue) {
        if (list == null) {
            return defaultValue;
        }
        if (index < 0 || index >= list.size()) {
            return defaultValue;
        }
        return list.get(index);
    }
}
