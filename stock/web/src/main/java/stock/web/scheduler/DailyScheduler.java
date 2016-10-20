/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package stock.web.scheduler;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

import stock.common.util.DateUtil;
import stock.common.util.LoggerUtil;
import stock.web.config.DailyDataWorkerConfig;
import stock.web.config.PlateAlgorithmWorkerConfig;
import stock.web.stock.data.processor.followed.RasingRateProcessor;
import stock.web.utils.HolidayUtils;

/**
 * @author yuanren.syr
 * @version $Id: DailyScheduler.java, v 0.1 2016/1/7 1:16 yuanren.syr Exp $
 */
public class DailyScheduler implements InitializingBean {

    private DailyDataWorkerConfig              collectorConfig;
    private DailyDataWorkerConfig              shExpCollectConfig;
    private DailyDataWorkerConfig              nearestPeakForcastConfig;
    private DailyDataWorkerConfig              nearestPeakEvaluateConfig;
    private DailyDataWorkerConfig              oversoldRallyForcastConfig;
    private DailyDataWorkerConfig              oversoldRallyForcastConfigForFeb;
    private DailyDataWorkerConfig              dynamicOverSoldConfig;
    private DailyDataWorkerConfig              dynamicOverboughtConfig;
    private PlateAlgorithmWorkerConfig         bollConfig;
    private PlateAlgorithmWorkerConfig         bollDiffConfig;
    private PlateAlgorithmWorkerConfig         macdConfig;
    private PlateAlgorithmWorkerConfig         dmaConfig;

    @Autowired
    private ProcessQueue                       processQueue;

    private Map<String, DailyDataWorkerConfig> workerConfigMap = Maps.newHashMap();

    @Autowired
    private HolidayUtils                       holidayUtils;

    public void execute() {
        Date forcastDate = HolidayUtils.getLastAvailableDate();
        if (holidayUtils.isAvailable(forcastDate)) {
            processQueue.offer(new DailyDataProcessWorker(forcastDate, collectorConfig, null));
            LoggerUtil.info("Push Collector into the Queue! forcardDate=[{0}]",
                DateUtil.simpleFormat(forcastDate));
            processQueue.offer(new DailyDataProcessWorker(forcastDate, nearestPeakForcastConfig,
                null));
            processQueue.offer(new DailyDataProcessWorker(forcastDate,
                oversoldRallyForcastConfigForFeb, null));
            processQueue
                .offer(new DailyDataProcessWorker(forcastDate, dynamicOverSoldConfig, null));
            processQueue.offer(new DailyDataProcessWorker(forcastDate, dynamicOverboughtConfig,
                null));

            Date evaluateDate = holidayUtils.getPrevWorkDate(forcastDate);

            Map<String, Object> map = Maps.newHashMap();
            map.put(RasingRateProcessor.CALC_DAY, forcastDate);
            processQueue.offer(new DailyDataProcessWorker(evaluateDate, nearestPeakEvaluateConfig,
                map));
            processQueue.offer(new DailyDataProcessWorker(forcastDate, shExpCollectConfig, null));

            processQueue.offer(new PlateAlgorithmProcessWorker(forcastDate, bollConfig));
            processQueue.offer(new PlateAlgorithmProcessWorker(forcastDate, bollDiffConfig));
            processQueue.offer(new PlateAlgorithmProcessWorker(forcastDate, macdConfig));
            processQueue.offer(new PlateAlgorithmProcessWorker(forcastDate, dmaConfig));
        }
    }

    public void afterPropertiesSet() throws Exception {
        workerConfigMap.put(nearestPeakForcastConfig.getFileName(), nearestPeakForcastConfig);
        workerConfigMap.put(nearestPeakEvaluateConfig.getFileName(), nearestPeakEvaluateConfig);
        workerConfigMap.put(oversoldRallyForcastConfig.getFileName(), oversoldRallyForcastConfig);
        workerConfigMap.put(oversoldRallyForcastConfigForFeb.getFileName(),
            oversoldRallyForcastConfigForFeb);
        workerConfigMap.put(dynamicOverSoldConfig.getFileName(), dynamicOverSoldConfig);
        workerConfigMap.put(dynamicOverboughtConfig.getFileName(), dynamicOverboughtConfig);
    }

    public Map<String, DailyDataWorkerConfig> getWorkerConfigMap() {
        return workerConfigMap;
    }

    public void setCollectorConfig(DailyDataWorkerConfig collectorConfig) {
        this.collectorConfig = collectorConfig;
    }

    public void setShExpCollectConfig(DailyDataWorkerConfig shExpCollectConfig) {
        this.shExpCollectConfig = shExpCollectConfig;
    }

    public void setNearestPeakForcastConfig(DailyDataWorkerConfig nearestPeakForcastConfig) {
        this.nearestPeakForcastConfig = nearestPeakForcastConfig;
    }

    public void setNearestPeakEvaluateConfig(DailyDataWorkerConfig nearestPeakEvaluateConfig) {
        this.nearestPeakEvaluateConfig = nearestPeakEvaluateConfig;
    }

    public void setOversoldRallyForcastConfig(DailyDataWorkerConfig oversoldRallyForcastConfig) {
        this.oversoldRallyForcastConfig = oversoldRallyForcastConfig;
    }

    public void setOversoldRallyForcastConfigForFeb(DailyDataWorkerConfig oversoldRallyForcastConfigForFeb) {
        this.oversoldRallyForcastConfigForFeb = oversoldRallyForcastConfigForFeb;
    }

    public void setDynamicOverSoldConfig(DailyDataWorkerConfig dynamicOverSoldConfig) {
        this.dynamicOverSoldConfig = dynamicOverSoldConfig;
    }

    public void setDynamicOverboughtConfig(DailyDataWorkerConfig dynamicOverboughtConfig) {
        this.dynamicOverboughtConfig = dynamicOverboughtConfig;
    }

    public void setBollConfig(PlateAlgorithmWorkerConfig bollConfig) {
        this.bollConfig = bollConfig;
    }

    public void setBollDiffConfig(PlateAlgorithmWorkerConfig bollDiffConfig) {
        this.bollDiffConfig = bollDiffConfig;
    }

    public void setMacdConfig(PlateAlgorithmWorkerConfig macdConfig) {
        this.macdConfig = macdConfig;
    }

    public void setDmaConfig(PlateAlgorithmWorkerConfig dmaConfig) {
        this.dmaConfig = dmaConfig;
    }

    public void setProcessQueue(ProcessQueue processQueue) {
        this.processQueue = processQueue;
    }
}
