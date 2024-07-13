package com.xxl.job.admin.taskExecutor.config;

import com.xxl.job.executor.jobhandler.SampleXxlJob;
import com.xxl.job.core.executor.impl.XxlJobSimpleExecutor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author xuxueli 2018-10-31 19:05:43
 */
public class FrameLessXxlJobConfig {
    private static Logger logger = LoggerFactory.getLogger(FrameLessXxlJobConfig.class);


    private static FrameLessXxlJobConfig instance = new FrameLessXxlJobConfig();

    public static FrameLessXxlJobConfig getInstance() {
        return instance;
    }


    private XxlJobSimpleExecutor xxlJobExecutor = null;

    /**
     * init
     */
    public void initXxlJobExecutor(JSONObject runConfig) {
        xxlJobExecutor = new XxlJobSimpleExecutor();
        xxlJobExecutor.setAdminAddresses(runConfig.getString("executorAddresses"));
        xxlJobExecutor.setAccessToken("default_token");
        xxlJobExecutor.setAppname("xxl-job-executor");
        xxlJobExecutor.setAddress("");
        xxlJobExecutor.setIp("");
        xxlJobExecutor.setPort(Integer.parseInt(runConfig.getString("executorPort")));
        xxlJobExecutor.setLogPath(runConfig.getString("executorLogPath"));
        xxlJobExecutor.setLogRetentionDays(30);

        // registry job bean
        xxlJobExecutor.setXxlJobBeanList(Arrays.asList(new SampleXxlJob()));

        // start executor
        try {
            xxlJobExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * destroy
     */
    public void destroyXxlJobExecutor() {
        if (xxlJobExecutor != null) {
            xxlJobExecutor.destroy();
        }
    }
}
