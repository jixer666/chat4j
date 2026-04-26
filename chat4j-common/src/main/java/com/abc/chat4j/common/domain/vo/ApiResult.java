package com.abc.chat4j.common.domain.vo;

import com.abc.chat4j.common.constant.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(HttpStatus.SUCCESS, null, data);
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(HttpStatus.SUCCESS, null, null);
    }

    public static <T> ApiResult<T> fail(String msg) {
        return new ApiResult<>(HttpStatus.ERROR, msg, null);
    }

    public static <T> ApiResult<T> fail(Integer code, String msg) {
        return new ApiResult<>(code, msg, null);
    }


}
