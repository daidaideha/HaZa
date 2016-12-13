package com.lyl.haza.network;

import com.lyl.haza.BuildConfig;
import com.lyl.haza.HzZaApplication;
import com.lyl.haza.R;
import com.lyl.haza.common.ToastHelper;
import com.lyl.haza.utils.log.Log;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HttpLoggingInterceptor implements Interceptor {

    public HttpLoggingInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request()
                .newBuilder()
//                .addHeader("clientVersion", "4.0.1")
                .build();
        if (!BuildConfig.DEBUG) {
            return chain.proceed(request);
        }

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
            Log.d("HTTP RESPONSE", requestStartMessage);
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Log.json("HTTP RESPONSE", buffer.readUtf8());
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception error) {
            Log.e("HTTP FAILED", "<-- HTTP FAILED: ");
            if (error instanceof SocketTimeoutException) {
                // 超时
                ToastHelper.showShort(HzZaApplication.getInstance().getString(R.string.time_out));
            } else if (error instanceof SocketException) {
                // 非取消操作引起的异常
                if (!error.getMessage().contains("Socket closed")) {
                    ToastHelper.showShort(HzZaApplication.getInstance().getString(R.string.network_is_not_available));
                }
            } else if (error instanceof IOException) {
                // 非取消操作引起的异常
                if (!error.getMessage().contains("Canceled")) {
                    ToastHelper.showShort(HzZaApplication.getInstance().getString(R.string.network_is_not_available));
                }
            }
            throw error;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        Log.d("HTTP RESPONSE", "<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + ", "
                + bodySize + " body" + ')');

        if (response.code() < 200 || response.code() > 299) {
            ToastHelper.showShort("服务器端故障 code:" + response.code());
            throw new IOException(String.format(Locale.getDefault(), "Unexpected response code %d for %s", response.code(), response.request().url()));
        }
        return response;
    }

}
