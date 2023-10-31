package cn.xisun.springboot3.web.controllers;

import cn.xisun.springboot3.web.Services.TestService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author XiSun
 * @since 2023/10/4 21:15
 */
@RestController
public class DemoController {

    @Resource
    private TestService testService;

    @GetMapping("/a*/b?/**/{p1:[a-f]+}")
    public String hello(HttpServletRequest request, @PathVariable("p1") String path) {
        // 获取请求路径
        return request.getRequestURI();
    }

    @GetMapping("/test_exception")
    public String testException() {
        int i = 1 / 0;
        return "无异常";
    }

    /**
     * @ExceptionHandler 标识一个方法处理错误，默认只能处理这个类发生的指定错误
     * @ControllerAdvice 统一处理所有错误
     */
    /*@ResponseBody
    @ExceptionHandler
    public String handleException(Exception e) {
        return "异常原因：" + e.getMessage();
    }*/

    @GetMapping("/test")
    public String test() {
        testService.test();
        return "...";
    }

}
