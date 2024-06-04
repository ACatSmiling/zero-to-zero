package cn.zero.cloud.platform.exception;

import cn.zero.cloud.platform.exception.factory.ExceptionResolverFactory;
import cn.zero.cloud.platform.exception.type.impl.RestResponseException;
import cn.zero.cloud.platform.exception.type.impl.PlatFormJsonException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author Xisun Wang
 * @since 2024/3/26 15:57
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 通用的异常处理
     *
     * @param e 待处理的异常
     * @return 响应体
     */
    /*@ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e) {
        LOGGER.error("globalExceptionResolver", e);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", e.getMessage());
        body.put("timestamp", DateUtil.getCurrentTimeDefaultTimeZone());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    /**
     * 特定类型的异常处理
     *
     * @param e 待处理的异常
     * @return 响应体
     */
    @ExceptionHandler(value = {RestResponseException.class, FeignException.class, IllegalArgumentException.class, IllegalStateException.class, Exception.class})
    protected ResponseEntity<Object> handleSpecificException(Exception e) {
        return ExceptionResolverFactory.getExceptionResolverInstance(e).handleException(e);
    }

    /**
     * 特定类型的异常处理，返回字符串做响应体
     * 注意：需添加@ResponseBody注解，否则Spring会尝试解析这个字符串为视图名称，并跳转到相应的视图
     * 如果没有对应的视图解析成功，可能会出现将字符串作为下一次请求路径的情况
     *
     * @param e 待处理的异常
     * @return 响应体
     */
    @ResponseBody
    @ExceptionHandler(value = PlatFormJsonException.class)
    protected String handle(Exception e) {
        return ExceptionResolverFactory.getExceptionResolverInstance(e).handleJsonException(e);
    }
}
