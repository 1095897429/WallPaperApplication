package com.ngbj.wallpaper.network.helper;



import com.google.gson.Gson;
import com.ngbj.wallpaper.BuildConfig;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.socks.library.KLog;

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
//                .addInterceptor(new HttpBaseParamsInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

    }

    //公共参数拦截器
    class HttpBaseParamsInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Gson gson = new Gson();
            HashMap<String,Object> hashMap = new HashMap<>();
            //获取到请求链接
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder();


            KLog.d("content-Type:" + request.body().contentType());


            //对请求参数获取 -- 只限于表单 post
            if(request.body() instanceof FormBody){
                FormBody oldFormBody = (FormBody) request.body();
                for (int i = 0; i < oldFormBody.size(); i++) {
                    hashMap.put(oldFormBody.encodedName(i),oldFormBody.encodedValue(i));
                }
            }

            hashMap.put("fromPlat", "default");
            hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
            hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
            hashMap.put("deviceType", "android");
            hashMap.put("timestamp", System.currentTimeMillis() + "");
            hashMap.put("sign", "");
            String strEntity = gson.toJson(hashMap);
            strEntity = URLDecoder.decode(strEntity,"UTF-8");
            request = requestBuilder
                    .post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                            strEntity))
                    .build();


            return chain.proceed(request);
        }
    }



    /** 统一添加公共参数在请求体中 */
    public static RequestBody getRequestBody(Map<String,Object> paramMap){
        Gson gson = new Gson();
        HashMap<String,Object> hashMap = new HashMap<>();
        //具体的参数
        if(paramMap != null && !paramMap.isEmpty()){
            for(Map.Entry<String, Object> entry: paramMap.entrySet()) {
                hashMap.put(entry.getKey(),entry.getValue());
            }
        }
        hashMap.put("fromPlat", "default");
        hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
        hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
        hashMap.put("deviceType", "android");
        hashMap.put("timestamp", System.currentTimeMillis() + "");
        hashMap.put("sign", "");
        String strEntity = gson.toJson(hashMap);
        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);
        return requestBody;
    }


}
