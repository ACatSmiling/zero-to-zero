package cn.zero.cloud.platform.kafka.producer.cache;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum KafkaSenderCacheName {

    MEETING_UPDATE_CACHENAME("meeting_update","meeting_cache_update"),
    MEETING_CREATE_CACHENAME("meeting_create","meeting_cache_create"),
    MEETING_DELETE_CACHENAME("meeting_delete","meeting_cache_delete");

    private static final Map<String, String> kafkaSenderCacheNameBaseOnType = new HashMap<>();

    static {
        for(KafkaSenderCacheName kafkaSenderCacheName : EnumSet.allOf(KafkaSenderCacheName.class)){
            kafkaSenderCacheNameBaseOnType.put(kafkaSenderCacheName.getType(), kafkaSenderCacheName.value);
        }
    }

    public static String findCacheNameBaseOnTypes(String source, String postfix){
        StringBuilder type = new StringBuilder(source.toLowerCase()).append("_").append(postfix.toLowerCase());
        return kafkaSenderCacheNameBaseOnType.get(type.toString());
    }

    KafkaSenderCacheName(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String type;
    private String value;
}
