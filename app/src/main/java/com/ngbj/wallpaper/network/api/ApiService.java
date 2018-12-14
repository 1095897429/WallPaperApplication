package com.ngbj.wallpaper.network.api;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.IpBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @POST("user/initUserInfo")
    Flowable<HttpResponse<InitUserBean>> initUserInfo();//用户信息初始化


    /** 获取验证码 */
    @POST("mobile-code/send-mobile-code")
    Flowable<HttpResponse<VerCodeBean>> getVerCode(@Body RequestBody requestBody);


    @POST("wallpaper/index")
    Flowable<HttpResponse<IndexBean>> index(@Query("page") int page);//首页数据

    @FormUrlEncoded
    @POST("wallpaper/list")
    Flowable<HttpResponse<List<AdBean>>> wallpagerList(@Query("page") int page,
                                                       @Field("categoryId")String categoryId,
                                                       @Field("order")int order );//壁纸列表页数据

    @POST("wallpaper/categoryList")
    Flowable<HttpResponse<List<InterestBean>>> categoryList();//感兴趣的壁纸分类集合



//    @Headers({"Content-type:application/x-www-form-urlencoded;charset=UTF-8"})
    @FormUrlEncoded
    @POST("user/interestCategory")
    Flowable<HttpResponse<String>> interestCategory(@Field("interest") String jsonListString);//用户选择感兴趣的壁纸分类


    @FormUrlEncoded
    @POST("wallpaper/detail")
    Flowable<HttpResponse<AdBean>> detail(@Field("wallpaperId") String wallpaperId);//壁纸详情页


    @POST("wallpaper/getUploadToken")
    Flowable<HttpResponse<UploadTokenBean>> getUploadToken();//获取图片上传的token























}
