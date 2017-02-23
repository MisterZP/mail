package com.mail.common;

import lombok.Data;

@Data
public class ValidBean<T> {
    private String key;
    private T value;
    public ValidBean() {
    }
    public ValidBean(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
