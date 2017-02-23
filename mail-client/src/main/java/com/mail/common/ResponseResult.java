package com.mail.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@SuppressWarnings("unused")
@Data
public class ResponseResult<T> {

    private boolean success = false;

    private String message;

    private int code = -1;

    private T entity;

    public static <T> ResponseResult<T> ERR(int code, String message) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> ResponseResult<T> STATUS(HttpStatus status) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = status.value();
        response.message = status.getReasonPhrase();
        return response;
    }

    public static <T> ResponseResult<T> OK() {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        return response;
    }

    public static <T> ResponseResult<T> OK(T entity) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        response.entity = entity;
        return response;
    }

    public static <T> ResponseResult<T> OK(int code, T entity) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = code;
        response.success = true;
        response.entity = entity;
        return response;
    }

    public static <T> ResponseResult<T> OK(String message) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = HttpStatus.OK.value();
        response.success = true;
        response.message = message;
        return response;
    }

    public static <T> ResponseResult<T> OK(int code) {
        ResponseResult<T> response = new ResponseResult<>();
        response.code = code;
        response.success = true;
        return response;
    }

}
