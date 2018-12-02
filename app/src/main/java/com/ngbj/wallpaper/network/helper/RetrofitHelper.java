package com.ngbj.wallpaper.network.helper;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ngbj.wallpaper.network.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private Retrofit mRetrofit;
    private static RetrofitHelper instance;
    private static ApiService mApiService;
    private static String BASEURL = "http://www.birdbrowser.info/";


    private RetrofitHelper(){}

    public static RetrofitHelper getInstance(){
        if(instance == null){
            synchronized(RetrofitHelper .class){
                if(instance == null){
                    instance = new RetrofitHelper ();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit(){
        if(null == mRetrofit){
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .client(OkHttpHelper.getInstance().getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }

    /** 获取Service代理对象 */
    public static ApiService getApiService(){
        if(null == mApiService){
            mApiService = getInstance().getRetrofit().create(ApiService.class);
        }
        return mApiService;
    }

    /** -------------------------------------------------- */




}
