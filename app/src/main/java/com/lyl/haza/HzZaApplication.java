package com.lyl.haza;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.lyl.haza.network.component.ApiComponent;
import com.lyl.haza.network.component.DaggerApiComponent;
import com.lyl.haza.network.component.DaggerNetComponent;
import com.lyl.haza.network.component.NetComponent;
import com.lyl.haza.network.module.AppModule;
import com.lyl.haza.network.module.NetModule;
import com.lyl.haza.utils.log.Log;
import com.squareup.picasso.Picasso;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class HzZaApplication extends Application {

    private boolean mIsDebugMode = BuildConfig.DEBUG;

    private boolean mIsNight;

    private NetComponent mNetComponent;
    private ApiComponent mApiComponent;

    public static HzZaApplication mHzZaApplication;

    public static HzZaApplication getInstance() {
        return mHzZaApplication;
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public void setNetComponent(NetComponent netComponent) {
        mNetComponent = netComponent;
    }

    public ApiComponent getApiComponent() {
        return mApiComponent;
    }

    public void setApiComponent(ApiComponent apiComponent) {
        mApiComponent = apiComponent;
    }

    public boolean isNight() {
        return mIsNight;
    }

    public void setNight(boolean night) {
        mIsNight = night;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHzZaApplication = this;

        // 初始化 picasso with OkHttp3
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this))
                .build();
        Picasso.setSingletonInstance(picasso);

        // 初始化 日志 图片缓存
        Log.getInstanceLog().init(mHzZaApplication);
        Log.getInstanceLog().setIsLog(mIsDebugMode);
        Log.getInstanceLog().setIsLogToSdcard(mIsDebugMode);

        mNetComponent = DaggerNetComponent.builder()
                .netModule(new NetModule(BuildConfig.BASE_URL))
                .appModule(new AppModule(this))
                .build();
        mApiComponent = DaggerApiComponent.builder()
                .netComponent(mNetComponent)
                .build();
    }
}
