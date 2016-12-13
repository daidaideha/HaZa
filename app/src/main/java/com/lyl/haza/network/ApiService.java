package com.lyl.haza.network;

import com.lyl.haza.httprsp.NewsRsp;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public interface ApiService {

    @POST("toutiao/index")
    Observable<NewsRsp> newsGet(@Query("type") String type, @Query("key") String key);
}
