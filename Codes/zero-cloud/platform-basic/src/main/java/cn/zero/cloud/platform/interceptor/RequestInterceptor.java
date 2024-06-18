package cn.zero.cloud.platform.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xisun Wang
 * @since 6/18/2024 15:42
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION = "Authorization";

    private static final String VALID_HEADER_TOKEN = "ValidHeaderToken";

    private static final List<String> VALID_HEADER_IDENTIFIES = List.of("PLATFORM_BASIC_SERVER");

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", request.getMethod());
            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            response.setStatus(HttpStatus.OK.value());
            return true;
        } else if (!request.getRequestURI().startsWith("/basic") || !needLogin(handler)) {
            return true;
        }

        return check(handler, request, response);
    }

    private boolean needLogin(final Object handler) {
        return true;
    }

    private boolean check(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String identifies = request.getHeader(VALID_HEADER_TOKEN);
        if (StringUtils.isNotBlank(identifies) && VALID_HEADER_IDENTIFIES.contains(identifies)) {
            // 内部系统之间互相调用合法header，直接放行
            return true;
        }

        String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            // authorization鉴权失败
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 自定义鉴权
        return true;
    }
}
