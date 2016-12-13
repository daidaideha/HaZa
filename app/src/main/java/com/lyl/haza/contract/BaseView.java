package com.lyl.haza.contract;

import android.content.Context;

import rx.Subscription;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public interface BaseView {

    Context getContext();

    /***
     * 添加网络请求
     * @param subscription 网络请求
     */
    void addSubscription(Subscription subscription);
}
