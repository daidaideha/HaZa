package com.lyl.haza.wxapi;

import com.lyl.haza.BuildConfig;

public class Constants {

    /**
     * debug
     **/
    // appid
    public static final String APP_ID;

    /**
     * open qq tencent qq  1104617282相关
     */
    public static final String TENCENT_APP_ID = "1105760751";


    static {
        /** debug **/
        if (BuildConfig.DEBUG) {
            APP_ID = "wx41c5b148f0388b58";
        } else {
            APP_ID = "wxf346d7a6113bbbf9";
        }
    }

}
