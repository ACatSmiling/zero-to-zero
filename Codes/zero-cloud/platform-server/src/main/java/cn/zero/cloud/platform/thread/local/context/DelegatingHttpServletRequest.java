package cn.zero.cloud.platform.thread.local.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;
import java.util.function.Consumer;

/**
 * MockHttpServletRequest
 */
public class DelegatingHttpServletRequest extends HttpServletRequestWrapper {
    private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

    public DelegatingHttpServletRequest(HttpServletRequest request) {
        super(request);
        if(request != null){
            forEachRemaining(request.getAttributeNames(), n -> this.setAttribute(n, request.getAttribute(n)));
        }
    }

    private <T> void forEachRemaining(Enumeration<T> e, Consumer<? super T> c) {
        if(e == null){
            return;
        }

        while(e.hasMoreElements()){
            c.accept(e.nextElement());
        }
    }

   	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(new LinkedHashSet<String>(this.attributes.keySet()));
	}

	@Override
	public void setAttribute(String name, Object value) {
        if(name != null){
            if (value != null) {
			    this.attributes.put(name, value);
		    } else {
			    this.attributes.remove(name);
		    }
        }
	}

	@Override
	public void removeAttribute(String name) {
		if(name != null){
		    this.attributes.remove(name);
        }
	}

}
