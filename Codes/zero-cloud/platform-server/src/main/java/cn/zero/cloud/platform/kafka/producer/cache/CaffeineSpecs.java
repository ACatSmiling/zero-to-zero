package cn.zero.cloud.platform.kafka.producer.cache;

public class CaffeineSpecs {
    private String cacheName;
    private String spec;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
}