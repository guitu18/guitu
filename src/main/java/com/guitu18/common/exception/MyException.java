package com.guitu18.common.exception;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author zhangkuan
 * @date 2019/8/20
 */
@Data
public class MyException extends RuntimeException {

    private String msg;
    private int code = 500;

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public MyException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public MyException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}