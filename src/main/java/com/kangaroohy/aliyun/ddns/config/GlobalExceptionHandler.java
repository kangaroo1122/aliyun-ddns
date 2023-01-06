package com.kangaroohy.aliyun.ddns.config;

import com.kangaroohy.aliyun.ddns.result.RestCode;
import com.kangaroohy.aliyun.ddns.result.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类 GlobalExceptionHandler 功能描述：<br/>
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2023/1/2 20:27
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestResult<String>> bindExceptionHandler(BindException e) {
        log.error("BindException：{}", e.getMessage(), e);
        return getStringResult(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestResult<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException：{}", e.getMessage(), e);
        return getStringResult(e.getBindingResult());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestResult<String>> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException：" + e.getClass().getName(), e);
        return RestResult.error(RestCode.RC400, "参数传递格式不对");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestResult<String>> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException：" + e.getClass().getName(), e);
        return RestResult.error(RestCode.RC400, "请求方法错误");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestResult<String>> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException：" + e.getClass().getName(), e);
        if (e instanceof MethodArgumentTypeMismatchException || e instanceof IllegalArgumentException) {
            return RestResult.error(RestCode.RC400, "请求参数格式不对，请检查");
        } else {
            return RestResult.error(RestCode.RC_1, e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestResult<String>> exceptionHandler(Exception e) {
        log.error("Exception：" + e.getClass().getName(), e);
        return RestResult.error(RestCode.RC_1, "未知异常，请联系管理员");
    }

    private ResponseEntity<RestResult<String>> getStringResult(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> collect = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return RestResult.error(RestCode.RC400, String.join(",", collect));
    }
}
