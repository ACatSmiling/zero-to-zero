package cn.zero.cloud.platform.kafka.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaClientState {
	private ConcurrentHashMap<String, Map<String, Object>> propertiesMap = new ConcurrentHashMap<>();
	private String ENABLED = "enabled";
	private String SERVERS = "servers";
	private String RATELITERMAP = "ratelimitermap";


	// lazy, thread-safe initialization
	private static class KafkaClientStateHolder {
		static final KafkaClientState INSTANCE = new KafkaClientState();
	}

	public static KafkaClientState getInstance() {
		return KafkaClientStateHolder.INSTANCE;
	}

	// register servers & enabled propertiesMap & duration for KafkaClientState
	public void registerState(String senderKey, Map<String, Object> state) {
		this.propertiesMap.put(senderKey, state);
	}

	private Map<String, Object> getProperties(String senderKey) {
		return propertiesMap.get(senderKey);
	}

	public boolean isEnabled(String senderKey) {
		Map<String, Object> properties = getProperties(senderKey);
		return properties != null && (Boolean) properties.get(ENABLED);
	}

	public String getServers(String senderKey) {
		Map<String, Object> properties = getProperties(senderKey);
		return properties == null || String.valueOf(properties.get(SERVERS)).equals("null")
				? ""
				: String.valueOf(properties.get(SERVERS));
	}

	public String getRatelimit(String senderKey, String cacheName) {
	    Map<String, String> rateLimiterMap= getRateLimiterMap(senderKey);
	    if(rateLimiterMap!=null){
	    	if(rateLimiterMap.get(cacheName)!=null){
	    		return rateLimiterMap.get(cacheName);
			}
		}
		return "0";
	}



	private Map<String, String> getRateLimiterMap(String senderKey){
		Map<String, Object> properties = getProperties(senderKey);
		if (properties != null) {
			return (HashMap) properties.get(RATELITERMAP);
		}
		return null;

	}

}