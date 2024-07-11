package com.xxl.job.executor;

import com.xxl.job.executor.config.FrameLessXxlJobConfig;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli 2018-10-31 19:05:43
 */
public class ExecutorApplication {
    private static Logger logger = LoggerFactory.getLogger(ExecutorApplication.class);

    private static JSONObject ReadConfig() {
        String currentDirectory = System.getProperty("user.dir");
        String filePath = currentDirectory + File.separator + "config.json";
        JSONObject cfg = null;
        try (FileReader reader = new FileReader(filePath)) {
            int ch;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            String jsonString = sb.toString();
            cfg = new JSONObject(jsonString);
        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
        return cfg;
    }
    public static void main(JSONObject runConfig) {
//        JSONObject runConfig = ReadConfig();
        try {
            // start
            FrameLessXxlJobConfig.getInstance().initXxlJobExecutor(runConfig);
            // Blocks until interrupted
            while (true) {
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            // destroy
            FrameLessXxlJobConfig.getInstance().destroyXxlJobExecutor();
        }

    }

}
