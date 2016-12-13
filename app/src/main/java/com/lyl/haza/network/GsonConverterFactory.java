package com.lyl.haza.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.lyl.haza.BuildConfig;
import com.lyl.haza.httprsp.BaseRsp;
import com.lyl.haza.utils.log.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Create by Mr.Pro.Lin on 2016/11/17
 * </p>
 *
 */
public class GsonConverterFactory extends Converter.Factory {

    private static final String TAG = GsonConverterFactory.class.getSimpleName();

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    private final Gson gson;

    private GsonConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new GsonRequestBodyConverter<>();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(adapter);
    }

    private static final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;

        GsonResponseBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                String json = value.string();
                T t = adapter.fromJson(json);
                if (BuildConfig.DEBUG) {
                    Log.json("HTTP RESPONSE", json);
                }
                if (t instanceof BaseRsp) {
                    BaseRsp baseBean = (BaseRsp) t;
                    if (!baseBean.isSuccess()) {
                        throw new ApiException(baseBean.getError_code(), baseBean.getReason());
                    }
                }
                return t;
            } catch (JsonSyntaxException | UnsupportedEncodingException | MalformedJsonException e) {
                // 解析失败
//                ToastHelper.showShort("json 解析失败");
                throw e;
            } finally {
                value.close();
            }
        }
    }

    private static class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

        GsonRequestBodyConverter() {
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            return RequestBody.create(MEDIA_TYPE, value.toString());
        }
    }
}


