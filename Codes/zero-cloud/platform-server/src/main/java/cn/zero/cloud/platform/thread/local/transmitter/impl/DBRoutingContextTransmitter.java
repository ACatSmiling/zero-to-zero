package cn.zero.cloud.platform.thread.local.transmitter.impl;

import cn.zero.cloud.platform.thread.local.context.DBRoutingContext;
import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;

public class DBRoutingContextTransmitter implements ThreadPoolThreadLocalTransmitter<String> {
    @Override
    public String get() {
        return DBRoutingContext.getDataSourceJNDIName();
    }

    @Override
    public void set(String context) {
        if(context == null){
            DBRoutingContext.selectDefaultDataSource();
        }else{
            DBRoutingContext.setDataSourceJNDIName(jndi2WebDomain(context));
        }
    }

    @Override
    public void clear() {
        DBRoutingContext.selectDefaultDataSource();
    }

    private String jndi2WebDomain(String jndiName) {
        return jndiName.replaceFirst("JNDI_webdb_", "");
    }

}
