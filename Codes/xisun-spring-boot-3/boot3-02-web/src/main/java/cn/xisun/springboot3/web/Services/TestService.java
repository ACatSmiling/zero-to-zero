package cn.xisun.springboot3.web.Services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author XiSun
 * @since 2023/10/10 22:38
 */
@Service
public class TestService {

    public void test() {
        // 在任意位置，随时可以通过RequestContextHolder获取到当前请求和响应的信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        assert attributes != null;

        // 当前请求
        HttpServletRequest request = attributes.getRequest();
        // 当前响应
        HttpServletResponse response = attributes.getResponse();

        // 当前请求的路径
        String requestURI = request.getRequestURI();
    }
}
