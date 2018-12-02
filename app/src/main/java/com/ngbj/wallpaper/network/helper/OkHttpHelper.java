package com.ngbj.wallpaper.network.helper;



import com.ngbj.wallpaper.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zl on 2018/5/22.
 * okHttp 帮助类
 */

public class OkHttpHelper {
    private static final long DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000;  //读取时间
    private static final long DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; //写入时间
    private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 20 * 1000; //超时时间

    private static OkHttpHelper sInstance;//单例
    private OkHttpClient mOkHttpClient;//依赖OkHttpClient

    public static OkHttpHelper getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (sInstance == null) {
                    sInstance = new OkHttpHelper();
                }
            }
        }
        return sInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private OkHttpHelper(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//包含header、body数据
            builder.addInterceptor(loggingInterceptor);
        }

        mOkHttpClient = builder
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new Interceptor() {//自定义拦截器
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()//关键部分，设置requestBody的编码格式为json
                                .addHeader("Content-Type","application/json;charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }


}
