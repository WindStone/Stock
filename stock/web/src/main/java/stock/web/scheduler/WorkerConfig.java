/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.util.Date;
import java.util.List;

import stock.web.stock.data.processor.DataProcessor;
import stock.web.stock.data.processor.FollowedDataProcessor;

/**
 * @author yuanren.syr
 * @version $Id: WorkerConfig.java, v 0.1 2016/1/19 0:55 yuanren.syr Exp $
 */
public class WorkerConfig {
    private String                      fileName;

    private Date                        currentDate;

    private DataProcessor               mainProcessor;

    private List<FollowedDataProcessor> followedDataProcessors;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public DataProcessor getMainProcessor() {
        return mainProcessor;
    }

    public void setMainProcessor(DataProcessor mainProcessor) {
        this.mainProcessor = mainProcessor;
    }

    public List<FollowedDataProcessor> getFollowedDataProcessors() {
        return followedDataProcessors;
    }

    public void setFollowedDataProcessors(List<FollowedDataProcessor> followedDataProcessors) {
        this.followedDataProcessors = followedDataProcessors;
    }
}
