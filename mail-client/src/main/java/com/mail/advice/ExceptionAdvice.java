package com.mail.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.common.ResponseResult;
import com.mail.common.ValidBean;
import com.mail.utils.Constants;
import com.mail.utils.WebUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.MissingResourceException;

import static org.springframework.http.HttpStatus.*;


@SuppressWarnings("unused")
@ControllerAdvice(annotations = {Controller.class, RestController.class})
class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({TypeMismatchException.class })
    @ResponseStatus(BAD_REQUEST)
    public String handleTypeMismatchException(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.info("TypeMismatchException error, path: {}, exception: {}", path, ex.getMessage());
        return wrapperResponse(request, response, BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMissingRequestParams(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        String query = request.getQueryString();
        LOGGER.error(
                "MissingServletRequestParameterException error, query: {}, message: {}", query, ex.getMessage());
        return wrapperResponse(request, response, BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleHttpMethodNotSupport(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("http method not error, path : {}, method : {}", request.getServletPath(), request.getMethod());
        return wrapperResponse(request, response, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.error("HttpMessageNotReadableException error, path: {}, exception: {}", path, ex);
        return wrapperResponse(request, response, BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseResult<List<ValidBean<String>>>> handleMethodArgsException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("Post RequestBody Param Valid Error, [{}]", ex.getMessage());
        return new ResponseEntity<>(WebUtils.makeValidErrorResponse(ex.getBindingResult()), BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseResult<List<ValidBean<String>>>> handleMethodArgsException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("Post Form Param Valid Error, [{}]", ex.getMessage());
        return new ResponseEntity<>(WebUtils.makeValidErrorResponse(ex.getBindingResult()), BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCommentNotFound(NotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.error("Common Not Found [{}]", ex.getMessage());
        return wrapperResponse(request, response, NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(BAD_REQUEST)
    public String handleMissingResourceException(MethodArgumentTypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.error("MethodArgumentTypeMismatchException : {}", path, ex);
        return wrapperResponse(request, response, BAD_REQUEST);
    }

    @ExceptionHandler({MissingResourceException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleMissingResourceException(MissingResourceException ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.error("Could Nod Find value by Key in Resource : {}", path, ex);
        return wrapperResponse(request, response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({javax.validation.ValidationException.class})
    public String handleValidationException(javax.validation.ValidationException ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.error("Validation Exception : {}", path, ex);
        if(null != ex.getCause() && ex.getCause() instanceof ValidationException) {
            return handleValidationException((ValidationException) ex.getCause(), request, response);
        }else {
            return wrapperResponse(request, response, INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public String handleCommonException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();
        LOGGER.error("Server Has Error , Path : {}", path, ex);
        return wrapperResponse(request, response, INTERNAL_SERVER_ERROR);
    }



    private String wrapperResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus httpStatus) {
        writeJSONEntityToResponse(response, httpStatus);
        return null;
    }

    private void writeJSONEntityToResponse(HttpServletResponse resp, HttpStatus httpStatus) {
        writeJSONEntityToResponse(resp, httpStatus, null);
    }

    private <T> void writeJSONEntityToResponse(HttpServletResponse resp, HttpStatus httpStatus, T entity) {
        resp.setCharacterEncoding(Constants.COMMON_CHARSET);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setContentType("application/json;charset=" + Constants.COMMON_CHARSET);
        resp.setStatus(httpStatus.value());
        ResponseResult<T> response = ResponseResult.ERR(httpStatus.value(), httpStatus.getReasonPhrase());
        if(null != entity) {
            response.setEntity(entity);
        }
        try {
            ServletOutputStream out = resp.getOutputStream();
            out.write(new ObjectMapper().writeValueAsString(response).getBytes(Constants.COMMON_CHARSET));
            out.flush();
        } catch (IOException e) {
            LOGGER.error("write response error", e);
        }
    }

}
