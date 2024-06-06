package cn.zero.cloud.platform.kafka.common.message.pojo;

/**
 * @author Xisun Wang
 * @since 2024/3/8 14:38
 */
public class MetaData {
    private String hostName;

    private String serverType;

    private Component component;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public enum Component {
        MBS, SBS, SiteAdmin, SuperAdmin, URLAPI, XMLAPI
    }
}
