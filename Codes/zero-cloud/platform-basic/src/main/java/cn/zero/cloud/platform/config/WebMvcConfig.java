package cn.zero.cloud.platform.config;

import cn.zero.cloud.platform.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Xisun Wang
 * @since 6/18/2024 16:08
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，并指定匹配的URL路径
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    }
}
