package cn.zero.cloud.platform.thread.local.transmitter.impl;

import cn.zero.cloud.platform.thread.local.transmitter.ThreadPoolThreadLocalTransmitter;
import cn.zero.cloud.platform.thread.local.context.DelegatingHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextTransmitter implements ThreadPoolThreadLocalTransmitter<RequestAttributes> {
    @Override
    public RequestAttributes get() {
        //for Request/Session/Global level bean, it cannot be reused since request will be ended, but it can cloned as a new one
        return cloneCurrentRequestAttributes();
    }

    @Override
    public void set(RequestAttributes context) {
        RequestContextHolder.setRequestAttributes(context);
    }

    @Override
    public void clear() {
        RequestContextHolder.resetRequestAttributes();
    }

    private RequestAttributes cloneCurrentRequestAttributes() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (!(requestAttributes instanceof ServletRequestAttributes)) {
                return null;
            }
            DelegatingHttpServletRequest request = new DelegatingHttpServletRequest(((ServletRequestAttributes) requestAttributes).getRequest());
            //only support request scope bean delagating
            return new ServletRequestAttributes(request, ((ServletRequestAttributes) requestAttributes).getResponse());
        } catch (Exception e) {
            return null;
        }
    }
}
