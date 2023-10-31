package cn.xisun.springboot3.web.config;

import cn.xisun.springboot3.web.business.PersonBizHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * @author XiSun
 * @since 2023/10/11 10:30
 */
@Configuration
public class WebFunctionConfig {

    /**
     * 函数式Web：
     * 1 给容器中诸如一个Bean，类型是RouterFunction<ServerResponse>
     * 2 每个业务准备一个自己的Handler
     *
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> personRouter(PersonBizHandler personBizHandler) {// @Bean注解的方法参数，会被自动注入进来
        return RouterFunctions.route()
                .GET("/user/{id}", RequestPredicates.accept(MediaType.ALL).and(RequestPredicates.param("a", "b")), personBizHandler::getPerson)// 请求中必须要有一个值为b的a参数
                .GET("/users", personBizHandler::getPersons)
                .POST("/user", RequestPredicates.accept(MediaType.APPLICATION_JSON), personBizHandler::savePerson)
                .PUT("/user/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), personBizHandler::updatePerson)
                .DELETE("/user/{id}", personBizHandler::deletePerson)
                .build();//开始定义路由信息
    }

}
