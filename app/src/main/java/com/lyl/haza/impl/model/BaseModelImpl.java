package com.lyl.haza.impl.model;

import com.lyl.haza.HzZaApplication;
import com.lyl.haza.network.ApiService;

import javax.inject.Inject;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class BaseModelImpl {

    @Inject
    ApiService mApiService;

    public BaseModelImpl() {
        HzZaApplication.getInstance().getApiComponent().inject(this);
    }
}
