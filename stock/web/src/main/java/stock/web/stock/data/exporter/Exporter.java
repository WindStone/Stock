/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.stock.data.exporter;

import java.util.Date;
import java.util.List;

import stock.web.stock.data.ContentLabel;
import stock.web.stock.data.TitleLabel;

/**
 * @author yuanren.syr
 * @version $Id: Exporter.java, v 0.1 2016/1/11 1:10 yuanren.syr Exp $
 */
public interface Exporter {
    /**
     * 将结果写入到excel中
     * @param results
     * @param titleRow
     * @param fileName
     * @param currentDate
     */
    void writeResult(List<List<String>> results, List<TitleLabel> titleRow, String fileName,
                     Date currentDate);

    /**
     * 将结果写入到excel中
     *
     * @param titleRow
     * @param results
     * @param fileName
     * @param currentDate
     */
    void writeContentResult(List<TitleLabel> titleRow, List<List<ContentLabel>> results,
                            String fileName, Date currentDate);
}
