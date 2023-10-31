package cn.xisun.springboot3.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiSun
 * @since 2023/10/4 15:10
 */
@Configuration
// @EnableWebMvc
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 保留默认的规则
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // 自定义的规则
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/c/", "classpath:/d/")
                .setCacheControl(CacheControl.maxAge(1180, TimeUnit.SECONDS));
    }*/

    // 写法一：配置一个能把对象转为yaml的messageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(new MyYamlHttpMessageConverter());
    }

    /*@Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override // 写法二：配置一个能把对象转为yaml的messageConverter
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(new MyYamlHttpMessageConverter());
            }
        };
    }*/

    /*@Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/a/", "classpath:/b/")
                        .setCacheControl(CacheControl.maxAge(1180, TimeUnit.SECONDS));
            }
        };
    }*/
}
