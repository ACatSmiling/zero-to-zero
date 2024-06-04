package cn.zero.cloud.platform.thread.local.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Xisun Wang
 * @since 2024/4/15 12:25
 */
public class DBRoutingContext {
    private static final Logger logger = LoggerFactory.getLogger(DBRoutingContext.class);
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void selectDefaultDataSource() {
        contextHolder.remove();
    }

    public static void setDataSourceJNDIName(String webDomainName) {
        String jndiName = "JNDI_webdb_" + webDomainName.toLowerCase();
        contextHolder.set(jndiName);
        logger.debug("Set dataSourceJNDIName to " + jndiName);
    }

    public static String getDataSourceJNDIName() {
        return (String) contextHolder.get();
    }

    public static void clearDataSourceJNDIName() {
        contextHolder.remove();
        logger.debug("DataSourceJNDIName for current thread has been cleaned.");
    }
}
