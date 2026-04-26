package com.abc.chat4j.common.exception;

import com.abc.chat4j.common.domain.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class SystemExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public ApiResult<Void> handleGlobalException(GlobalException e) {
        log.error("自定义出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public ApiResult<Void> handleBindException(BindException e) {
        log.error("校验出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        log.error("系统出现异常: {}, ", e.getMessage(), e);
        return ApiResult.fail("系统出现异常");
    }

}
