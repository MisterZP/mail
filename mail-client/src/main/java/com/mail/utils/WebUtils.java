package com.mail.utils;

import com.mail.common.ResponseResult;
import com.mail.common.ValidBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Created by zengping on 2017/1/9.
 */
public class WebUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    public static <T> void validateCollection(Collection<T> collection, Validator validator) throws BindException {
        for(T entity : collection) {
            BindingResult result = new BeanPropertyBindingResult(entity, "objectTMP");
            validator.validate(entity, result);
            if(result.hasErrors()) {
                throw new BindException(result);
            }
        }
    }

    public static <T> void validateBean(T bean, Validator validator) throws BindException {
        BindingResult result = new BeanPropertyBindingResult(bean, "objectTMP");
        validator.validate(bean, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
    }

    public static ResponseResult<List<ValidBean<String>>> makeValidErrorResponse(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<ValidBean<String>> result = new ArrayList<>();
        for(ObjectError objectError : allErrors) {
            if(objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                result.add(new ValidBean(fieldError.getField(),fieldError.getDefaultMessage()));
            }else {
                result.add(new ValidBean(objectError.getObjectName(), objectError.getDefaultMessage()));
            }
        }
        ResponseResult<List<ValidBean<String>>> entity = ResponseResult.STATUS(BAD_REQUEST);
        entity.setEntity(result);
        return entity;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                //根据网卡取本机配置的IP
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (Exception e) {
                    LOGGER.error("get ip addr error : ", e);
                }
            }

        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (StringUtils.isNotBlank(ipAddress) && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
