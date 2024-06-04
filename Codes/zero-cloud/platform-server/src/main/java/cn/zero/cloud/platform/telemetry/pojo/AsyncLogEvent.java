package cn.zero.cloud.platform.telemetry.pojo;

import org.slf4j.Logger;

/**
 * @author Xisun Wang
 * @since 2024/3/21 14:41
 */
public class AsyncLogEvent {
    private String log;

    private Logger logger;

    public AsyncLogEvent(String log, Logger logger) {
        this.log = log;
        this.logger = logger;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
