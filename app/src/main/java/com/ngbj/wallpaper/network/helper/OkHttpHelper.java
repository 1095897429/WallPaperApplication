package com.ngbj.wallpaper.network.helper;



import com.google.gson.Gson;
import com.ngbj.wallpaper.BuildConfig;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.encry.AesUtils;
import com.socks.library.KLog;
import com.umeng.analytics.AnalyticsConfig;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.http.Body;

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


        mOkHttpClient = builder
                .readTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true) // 失败重发
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

    }


    /** 统一添加公共参数在请求体中  将公共 + 必须参数 aes加密 ，得到sign值 ---> 将公共 + 必须参数 + sign aes加密，得到数据 ---> hashmap.put(data,得到数据)*/
    public static RequestBody getRequestBody(Map<String,Object> paramMap){
        Gson gson = new Gson();
        HashMap<String,Object> hashMap = new HashMap<>();
        //具体的参数
        if(paramMap != null && !paramMap.isEmpty()){
            for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
                hashMap.put(entry.getKey(),entry.getValue());
            }
        }
        hashMap.put("fromPlat", AnalyticsConfig.getChannel(MyApplication.getInstance()));
        hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
        hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
        hashMap.put("deviceType", "android");
        hashMap.put("timestamp", System.currentTimeMillis() + "");

        String enctry = gson.toJson(hashMap);
        String result = AesUtils.encrypt(enctry,AesUtils.SECRETKEY,AesUtils.IV);
//        KLog.d("加密数据：" + result);
        hashMap.put("sign", result);

        String dectry =  AesUtils.decrypt(result,AesUtils.SECRETKEY,AesUtils.IV);
//        KLog.d("解密 =  " +  dectry);

        String strEntity = gson.toJson(hashMap);
//        KLog.d("加过密之后的sign数据：" + strEntity);


        String lastResult = AesUtils.encrypt(strEntity,AesUtils.SECRETKEY,AesUtils.IV);
//        KLog.d("加过密之后的发送数据：" + lastResult);

        hashMap.clear();
        hashMap.put("data",lastResult);

        String lastStrEntity = gson.toJson(hashMap);

        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), lastStrEntity);
        return requestBody;
    }


}
