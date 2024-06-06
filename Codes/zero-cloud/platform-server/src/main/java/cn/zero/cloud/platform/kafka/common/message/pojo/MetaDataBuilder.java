package cn.zero.cloud.platform.kafka.common.message.pojo;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:45
 */
public class MetaDataBuilder {
    static private String hostName = null;

    static private String serverType = null;

    static {
        try {
            hostName = getHostName();
            serverType = getServerType();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MetaData build() {
        MetaData metaData = new MetaData();

        metaData.setServerType(serverType);
        metaData.setHostName(hostName);

        return metaData;
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            return "Unknown";
        }
    }

    private static String getServerType() {
        String serverType = getServerTypeFromVM();
        if (StringUtils.isBlank(serverType)) {
            serverType = getServerTypeFromDocker();
        }

        if (StringUtils.isNotBlank(serverType)) {
            return serverType;
        }

        return "Unknown";
    }

    private static String getServerTypeFromVM() {
        File file = new File("/.svrtype");
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = reader.readLine();
            if (StringUtils.isNotBlank(content)) {
                return content.trim();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static String getServerTypeFromDocker() {
        // docker should have env SERVICE_NAME = mbs
        String serviceName = System.getenv().get("SERVICE_NAME");
        if (StringUtils.isNotBlank(serviceName)) {
            return serviceName.trim();
        }

        // docker hostName = ats1-mbs-65b5bbd5c5-5txmf
        String hostName = getHostName();
        if (StringUtils.isNotBlank(hostName)) {
            String[] words = hostName.split("-");
            if (words.length > 1 && StringUtils.isNotBlank(words[1])) {
                return words[1].trim();
            }
        }

        return null;
    }
}
