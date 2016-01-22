/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.common.util;

/**
 *
 * @author yuanren.syr
 * @version $Id: ${FILE}.java, v 0.1 2015/11/18 21:54 yuanren.syr Exp $
 */
public class PathUtil {

    private static final String RESOURCE = "/resource";

    private static final String DATA     = "/data";

    private static final String APP      = "/stock";

    private static final String DAL      = "/common/dal";

    public static String getDalPath() {
        return System.getProperty("user.dir") + APP + DAL;
    }

    public static String getDataPath() {
        return System.getProperty("user.dir") + RESOURCE + DATA;
    }
}
