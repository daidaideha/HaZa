package com.lyl.haza.network;

import java.io.IOException;

/**
 * Create by Mr.Pro.Lin on 2016/11/16
 * </p>
 * 错误异常
 */
public class ApiException extends IOException {

    private int error_code = -1;
    private String reason = null;

    public ApiException(int error_code, String reason) {
        this.error_code = error_code;
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
