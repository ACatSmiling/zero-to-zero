package cn.zero.cloud.platform.kafka.producer.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix="webex.kafka.producer.cache.caffeine")
public class KafkaCaffeineCacheSpecs {
    private static final String DEFAULT_CAFFEINE_CACHE_SPEC = "maximumSize=10000,expireAfterWrite=180s";

    private List<CaffeineSpecs> specsList = new ArrayList<>();

    public String get(String name){
        if(specsList == null || specsList.isEmpty()){
            return DEFAULT_CAFFEINE_CACHE_SPEC;
        }

        CaffeineSpecs caffeineSpecs = specsList.stream().filter(specs -> specs.getCacheName().equals(name)).findFirst().orElse(null);
        if(caffeineSpecs == null){
            return DEFAULT_CAFFEINE_CACHE_SPEC;
        }

        return caffeineSpecs.getSpec();
    }

    public List<CaffeineSpecs> getSpecsList() {
        return specsList;
    }

    public void setSpecsList(List<CaffeineSpecs> specsList) {
        this.specsList = specsList;
    }
}
