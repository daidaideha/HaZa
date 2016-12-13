package com.lyl.haza.network.component;

import com.lyl.haza.network.ApiService;
import com.lyl.haza.network.module.AppModule;
import com.lyl.haza.network.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
@Singleton
@Component(modules = {NetModule.class, AppModule.class})
public interface NetComponent {

    ApiService apiService();

}
