package com.abc.chat4j.common.exception;

import com.abc.chat4j.common.constant.HttpStatus;

public class GlobalException extends RuntimeException {

    private Integer code;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(String message) {
        super(message);
        this.code = HttpStatus.ERROR;
    }

    public Integer getCode() {
        return code;
    }
}