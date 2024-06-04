package cn.zero.cloud.platform.thread.local.transmitter.impl;

import cn.zero.cloud.platform.thread.local.context.WbxServerContext;
import cn.zero.cloud.platform.thread.local.context.WbxServerContextToken;
import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;

public class WbxServerContextTransmitter implements ThreadPoolThreadLocalTransmitter<WbxServerContextToken> {
    @Override
    public WbxServerContextToken get() {
        return WbxServerContext.getContext();
    }

    @Override
    public void set(WbxServerContextToken context) {
        WbxServerContext.setContext(context);
    }

    @Override
    public void clear() {
        WbxServerContext.clearContext();
    }
}
