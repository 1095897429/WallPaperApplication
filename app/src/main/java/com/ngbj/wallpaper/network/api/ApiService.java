package com.ngbj.wallpaper.network.api;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.BannerListBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.IpBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.SearchBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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

    @POST("user/initUserInfo")
    Flowable<HttpResponse<InitUserBean>> initUserInfo(@Body RequestBody requestBody);//用户信息初始化


    @POST("wallpaper/categoryList")
    Flowable<HttpResponse<List<InterestBean>>> categoryList(@Body RequestBody requestBody);//感兴趣的壁纸分类集合

    @POST("user/interestCategory")
    Flowable<HttpResponse<String>> interestCategory(@Body RequestBody requestBody);//用户选择感兴趣的壁纸分类

    @POST("wallpaper/index")
    Flowable<HttpResponse<IndexBean>> index(@Query("page") int page,@Body RequestBody requestBody);//首页数据


    @POST("wallpaper/list")
    Flowable<HttpResponse<List<AdBean>>> wallpagerList(@Query("page") int page,@Body RequestBody requestBody);//壁纸列表页数据


    @POST("wallpaper/detail")
    Flowable<HttpResponse<AdBean>> detail(@Body RequestBody requestBody);//壁纸详情页


    @POST("wallpaper/getUploadToken")
    Flowable<HttpResponse<UploadTokenBean>> getUploadToken(@Body RequestBody requestBody);//获取图片上传的token

    @POST("user/login")
    Flowable<HttpResponse<LoginBean>> loginPhone(@Body RequestBody requestBody);//手机登录


    @POST("mobile-code/sendMobileCode")
    Flowable<HttpResponse<String>> sendMobileCode(@Body RequestBody requestBody);//验证码

    @POST("wallpaper/search")
    Flowable<HttpResponse<List<AdBean>>> search(@Query("page") int  page,@Body RequestBody requestBody);//搜索壁纸


    @POST("ads/banner")
    Flowable<HttpResponse<BannerListBean>> banner(@Body RequestBody requestBody);//banner专题详情页数据


    @POST("ads/navigation")
    Flowable<HttpResponse<List<AdBean>>> navigationList(@Query("page") int  page,@Body RequestBody requestBody);//导航专题列表页

    @POST("ads/hotSearch")
    Flowable<HttpResponse<List<AdBean>>> hotSearchList(@Query("page") int  page,@Body RequestBody requestBody);//热搜列表页


    @POST("history/report")
    Flowable<HttpResponse<String>> report(@Body RequestBody requestBody);//记录用户举报



    @POST("history/record")
    Flowable<HttpResponse<String>> record(@Body RequestBody requestBody);// 记录用户下载，收藏，分享壁纸

    @POST("ads/searchPage")
    Flowable<HttpResponse<SearchBean>> searchPage(@Body RequestBody requestBody);//搜索页内容




    @POST("history/searchHistory")
    Flowable<HttpResponse<String>> searchHistory(@Body RequestBody requestBody);//记录用户最近5次关键字搜索历史

    @POST("user/updateUserHeadImage")
    Flowable<HttpResponse<LoginBean>> uploadHeadImage(@Query("access-token") String accessToken,@Body RequestBody requestBody);//修改用户头像



    @POST("wallpaper/upload")
    Flowable<HttpResponse<String>> uploadWallpaper(@Query("access-token") String accessToken,
                                                   @Body RequestBody requestBody);//用户上传壁纸


    @POST("history/getRecord")
    Flowable<HttpResponse<List<AdBean>>> getRecord(@Body RequestBody requestBody);//用户下载、分享、收藏壁纸信息


    @POST("wallpaper/uploadHistory")
    Flowable<HttpResponse<List<AdBean>>> uploadHistory(@Query("access-token") String accessToken,
                                                       @Body RequestBody requestBody);//用户上传壁纸历史

    @POST("user/thirdPlatLogin")
    Flowable<HttpResponse<LoginBean>> thirdPlatLogin(@Body RequestBody requestBody);//第三方登录

    @POST("history/deleteCollection")
    Flowable<HttpResponse<String>> deleteCollection(@Body RequestBody requestBody);//取消收藏



    /** ------------------------------------------------------------------------------------------------------ */


    @GET("getIpInfo.php")
    Flowable<HttpResponse<IpBean>> getIpMsg(@Query("ip") String ip);//淘宝IP地址



    /** 获取验证码 */
    @POST("mobile-code/send-mobile-code")
    Flowable<HttpResponse<VerCodeBean>> getVerCode();















}
