package com.rkhd.platform.metarepo.core.model;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结构
 */
@Data
public class Result<T> implements Serializable {
    /** 状态码，0=成功，非0=失败 */
    private int code;
    /** 提示信息 */
    private String message;
    /** 业务数据 */
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(0);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public boolean isSuccess() {
        return code == 0;
    }
}
