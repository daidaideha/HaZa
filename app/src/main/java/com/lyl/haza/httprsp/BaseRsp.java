package com.lyl.haza.httprsp;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class BaseRsp {

    private String reason;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public boolean isSuccess() {
        return error_code == 0;
    }
}
