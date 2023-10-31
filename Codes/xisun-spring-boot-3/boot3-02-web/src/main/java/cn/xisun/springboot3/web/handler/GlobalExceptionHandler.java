package cn.xisun.springboot3.web.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author XiSun
 * @since 2023/10/9 9:19
 */
// @ControllerAdvice // @ControllerAdvice标识的类，集中处理所有@Controller发生的错误
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler
    public String handleException(Exception e) {
        return "异常原因：" + e.getMessage();
    }
}
