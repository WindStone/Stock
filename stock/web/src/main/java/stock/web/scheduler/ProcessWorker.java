/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

/**
 * @author yuanren.syr
 * @version $Id: ProcessWorker.java, v 0.1 2016/2/16 17:08 yuanren.syr Exp $
 */
public interface ProcessWorker {
    void work();

    boolean isFinished();

    double getProcess();
}
