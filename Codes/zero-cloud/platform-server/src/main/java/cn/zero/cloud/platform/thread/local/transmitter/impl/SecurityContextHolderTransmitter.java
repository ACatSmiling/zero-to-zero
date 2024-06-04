package cn.zero.cloud.platform.thread.local.transmitter.impl;

import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextHolderTransmitter implements ThreadPoolThreadLocalTransmitter<SecurityContext> {
    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }

    @Override
    public void set(SecurityContext context) {
        SecurityContextHolder.setContext(context);
    }

    @Override
    public void clear() {
        SecurityContextHolder.clearContext();
    }
}
