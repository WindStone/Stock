/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.utils;

import java.io.File;

/**
 * @author yuanren.syr
 * @version $Id: RenameUtils.java, v 0.1 2016/1/22 1:13 yuanren.syr Exp $
 */
public class RenameUtils {

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.home") + "/resources/");
        for (File dateFile : file.listFiles()) {
            for (File eachFile : dateFile.listFiles()) {
                String fileName = eachFile.getName();
                int firstIndex = fileName.indexOf('_');
                int lastIndex = fileName.lastIndexOf('_');
                int split = fileName.lastIndexOf(".");
                if (firstIndex != lastIndex) {
                    String newFileName = fileName.substring(0, firstIndex)
                                         + fileName.substring(lastIndex + 1, split) + "_"
                                         + fileName.substring(firstIndex + 1, lastIndex)
                                         + fileName.substring(split);
                    File newFile = new File(dateFile.getPath() + "/" + newFileName);
                    eachFile.renameTo(newFile);
                }
            }
        }
    }
}
