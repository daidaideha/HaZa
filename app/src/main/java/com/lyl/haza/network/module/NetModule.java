package com.lyl.haza.network.module;

import android.app.Application;

import com.google.gson.Gson;
import com.lyl.haza.network.ApiService;
import com.lyl.haza.network.GsonConverterFactory;
import com.lyl.haza.network.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
@Module
public class NetModule {

    private static final int TIMEOUT_TIME = 15;// 15s

    private String mBaseUrl;

    public NetModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new HttpLoggingInterceptor())
//                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
//                .addNetworkInterceptor(mTokenInterceptor)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
