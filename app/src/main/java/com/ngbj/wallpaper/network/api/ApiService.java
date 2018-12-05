package com.ngbj.wallpaper.network.api;

import com.ngbj.wallpaper.bean.entityBean.IpBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.network.response.HttpResponse;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/***
 * 1.发送给后台的是json字符串
 * @Body  -- 将数据添加到requestBody中
 */

public interface ApiService {

    @GET("getIpInfo.php")
    Flowable<HttpResponse<IpBean>> getIpMsg(@Query("ip") String ip);//淘宝IP地址

    /** 获取验证码 */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("mobile-code/send-mobile-code")
    Flowable<HttpResponse<VerCodeBean>> getVerCode(@Body RequestBody requestBody);

}
