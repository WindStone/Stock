/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import stock.common.util.LoggerUtil;

/**
 * @author yuanren.syr
 * @version $Id: ProcessQueue.java, v 0.1 2016/1/18 23:21 yuanren.syr Exp $
 */
public class ProcessQueue extends AbstractQueue<ProcessWorker> {

    private static ConcurrentLinkedQueue<ProcessWorker> workers       = new ConcurrentLinkedQueue<ProcessWorker>();

    private static ProcessWorker                        currentWorker = null;

    public Iterator<ProcessWorker> iterator() {
        return workers.iterator();
    }

    @Override
    public int size() {
        if (currentWorker == null) {
            return workers.size();
        } else {
            return 1 + workers.size();
        }
    }

    public boolean offer(ProcessWorker processWorker) {
        boolean ret = workers.offer(processWorker);
        if (ret) {
            pollToWork();
        }
        return ret;
    }

    public ProcessWorker poll() {
        if (currentWorker == null) {
            pollToWork();
        }
        return currentWorker;
    }

    public ProcessWorker peek() {
        if (currentWorker == null) {
            return null;
        } else {
            return currentWorker;
        }
    }

    public static void pollToWork() {
        synchronized (workers) {
            if (currentWorker != null) {
                if (!currentWorker.isFinished()) {
                    return;
                }
            }
            currentWorker = workers.poll();
            if (currentWorker == null) {
                return;
            }
            new Thread() {
                public void run() {
                    try {
                        currentWorker.work();
                    } catch (Throwable t) {
                        LoggerUtil.error("Process Queue System Error", t);
                    }
                }
            }.start();
        }
    }
}
