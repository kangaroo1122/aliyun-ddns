package com.kangaroohy.aliyun.ddns.result;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @author kangaroo hy
 * @version 0.0.1
 * @since 2020/3/13
 */
@Data
public class RestResult<T> implements Serializable {

    private static final long serialVersionUID = 7711799662216684129L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应实体
     */
    private T data;

    public RestResult() {
    }

    public RestResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RestResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseEntity<RestResult<T>> ok() {
        return ok(RestCode.RC200.getMsg(), null);
    }

    public static <T> ResponseEntity<RestResult<T>> ok(T data) {
        return ok(RestCode.RC200.getMsg(), data);
    }

    public static <T> ResponseEntity<RestResult<T>> ok(String msg, T data) {
        return ok(RestCode.RC200, msg, data);
    }

    public static <T> ResponseEntity<RestResult<T>> ok(RestCode restCode, String msg, T data) {
        return ok(restCode, msg, data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<RestResult<T>> ok(RestCode restCode, String msg, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new RestResult<>(restCode.getCode(), msg, data), httpStatus);
    }

    public static <T> ResponseEntity<RestResult<T>> error(String msg) {
        return error(RestCode.RC_1, msg);
    }

    public static <T> ResponseEntity<RestResult<T>> error(RestCode restCode) {
        return error(restCode, restCode.getMsg());
    }

    public static <T> ResponseEntity<RestResult<T>> error(RestCode restCode, String msg) {
        return error(restCode, msg, null);
    }

    public static <T> ResponseEntity<RestResult<T>> error(RestCode restCode, String msg, T data) {
        return error(restCode, HttpStatus.BAD_REQUEST, msg, data);
    }

    public static <T> ResponseEntity<RestResult<T>> error(HttpStatus httpStatus, String msg) {
        return error(RestCode.RC_1, httpStatus, msg);
    }

    public static <T> ResponseEntity<RestResult<T>> error(RestCode restCode, HttpStatus httpStatus, String msg) {
        return error(restCode, httpStatus, msg, null);
    }

    public static <T> ResponseEntity<RestResult<T>> error(RestCode restCode, HttpStatus httpStatus, String msg, T data) {
        return new ResponseEntity<>(new RestResult<>(restCode.getCode(), msg, data), httpStatus);
    }
}
