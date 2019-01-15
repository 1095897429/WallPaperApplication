package com.ngbj.wallpaper.network.api;

import com.ngbj.wallpaper.bean.entityBean.IpBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;


import io.reactivex.Flowable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/***
 * 1.发送给后台的是json字符串
 * @Body  -- 将数据添加到requestBody中
 */

public interface ApiService {

    @POST("user/initUserInfo")
    Flowable<ResponseBody> initUserInfo(@Body RequestBody requestBody);//用户信息初始化

//    @POST("user/initUserInfo")
//    Flowable<HttpResponse<InitUserBean>> initUserInfo(@Body RequestBody requestBody);//用户信息初始化


    @POST("wallpaper/index")
    Flowable<ResponseBody> index(@Query("page") int page,@Body RequestBody requestBody);//首页数据


//    @POST("wallpaper/index")
//    Flowable<HttpResponse<IndexBean>> index(@Query("page") int page,@Body RequestBody requestBody);//首页数据


    @POST("wallpaper/search")
    Flowable<ResponseBody> search(@Query("page") int  page, @Body RequestBody requestBody);//搜索壁纸



//    @POST("wallpaper/search")
//    Flowable<HttpResponse<List<AdBean>>> search(@Query("page") int  page,@Body RequestBody requestBody);//搜索壁纸



    @POST("ads/searchPage")
    Flowable<ResponseBody> searchPage(@Body RequestBody requestBody);//搜索页内容

//
//    @POST("ads/searchPage")
//    Flowable<HttpResponse<SearchBean>> searchPage(@Body RequestBody requestBody);//搜索页内容



    @POST("ads/navigation")
    Flowable<ResponseBody> navigationList(@Query("page") int  page,@Body RequestBody requestBody);//导航专题列表页


//    @POST("ads/navigation")
//    Flowable<HttpResponse<List<AdBean>>> navigationList(@Query("page") int  page,@Body RequestBody requestBody);//导航专题列表页


    @POST("ads/hotSearch")
    Flowable<ResponseBody> hotSearchList(@Query("page") int  page,@Body RequestBody requestBody);//热搜列表页


//    @POST("ads/hotSearch")
//    Flowable<HttpResponse<List<AdBean>>> hotSearchList(@Query("page") int  page,@Body RequestBody requestBody);//热搜列表页



    @POST("ads/banner")
    Flowable<ResponseBody> banner(@Body RequestBody requestBody);//banner专题详情页数据


//    @POST("ads/banner")
//    Flowable<HttpResponse<BannerListBean>> banner(@Body RequestBody requestBody);//banner专题详情页数据


    @POST("wallpaper/list")
    Flowable<ResponseBody> wallpagerList(@Query("page") int page,@Body RequestBody requestBody);//壁纸列表页数据


//    @POST("wallpaper/list")
//    Flowable<HttpResponse<List<AdBean>>> wallpagerList(@Query("page") int page,@Body RequestBody requestBody);//壁纸列表页数据


    @POST("wallpaper/categories")
    Flowable<ResponseBody> categories(@Body RequestBody requestBody);//分类中兴趣的壁纸分类集合


//    @POST("wallpaper/categories")
//    Flowable<HttpResponse<List<InterestBean>>> categories(@Body RequestBody requestBody);//分类中兴趣的壁纸分类集合



    @POST("user/thirdPlatLogin")
    Flowable<ResponseBody> thirdPlatLogin(@Body RequestBody requestBody);//第三方登录


//    @POST("user/thirdPlatLogin")
//    Flowable<HttpResponse<LoginBean>> thirdPlatLogin(@Body RequestBody requestBody);//第三方登录


    @POST("wallpaper/uploadHistory")
    Flowable<ResponseBody> uploadHistory(@Query("access-token") String accessToken,
                                                       @Body RequestBody requestBody);//用户上传壁纸历史

//    @POST("wallpaper/uploadHistory")
//    Flowable<HttpResponse<List<AdBean>>> uploadHistory(@Query("access-token") String accessToken,
//                                                       @Body RequestBody requestBody);//用户上传壁纸历史

    @POST("history/getRecord")
    Flowable<ResponseBody> getRecord(@Body RequestBody requestBody);//用户下载、分享、收藏壁纸信息


//    @POST("history/getRecord")
//    Flowable<HttpResponse<List<AdBean>>> getRecord(@Body RequestBody requestBody);//用户下载、分享、收藏壁纸信息


    @POST("user/updateUserHeadImage")
    Flowable<ResponseBody> uploadHeadImage(@Query("access-token") String accessToken,@Body RequestBody requestBody);//修改用户头像

//    @POST("user/updateUserHeadImage")
//    Flowable<HttpResponse<LoginBean>> uploadHeadImage(@Query("access-token") String accessToken,@Body RequestBody requestBody);//修改用户头像


    @POST("user/updateUserInfo")
    Flowable<ResponseBody> updateUserInfo(@Query("access-token") String accessToken,@Body RequestBody requestBody);//修改用户信息



//    @POST("user/updateUserInfo")
//    Flowable<HttpResponse<LoginBean>> updateUserInfo(@Query("access-token") String accessToken,@Body RequestBody requestBody);//修改用户信息


    @POST("wallpaper/categoryList")
    Flowable<ResponseBody> categoryList(@Body RequestBody requestBody);//开始感兴趣的壁纸分类集合




//    @POST("wallpaper/categoryList")
//    Flowable<HttpResponse<List<InterestBean>>> categoryList(@Body RequestBody requestBody);//开始感兴趣的壁纸分类集合


    @POST("wallpaper/getUploadToken")
    Flowable<ResponseBody> getUploadToken(@Body RequestBody requestBody);//获取图片上传的token


//    @POST("wallpaper/getUploadToken")
//    Flowable<HttpResponse<UploadTokenBean>> getUploadToken(@Body RequestBody requestBody);//获取图片上传的token



    @POST("wallpaper/upload")
    Flowable<ResponseBody> uploadWallpaper(@Query("access-token") String accessToken,
                                                   @Body RequestBody requestBody);//用户上传壁纸


//    @POST("wallpaper/upload")
//    Flowable<HttpResponse<String>> uploadWallpaper(@Query("access-token") String accessToken,

    @POST("wallpaper/detail")
    Flowable<ResponseBody> detail(@Body RequestBody requestBody);//壁纸详情页


//    @POST("wallpaper/detail")
//    Flowable<HttpResponse<AdBean>> detail(@Body RequestBody requestBody);//壁纸详情页


    @POST("history/record")
    Flowable<ResponseBody> record(@Body RequestBody requestBody);// 记录用户下载，收藏，分享壁纸


//    @POST("history/record")
//    Flowable<HttpResponse<String>> record(@Body RequestBody requestBody);// 记录用户下载，收藏，分享壁纸



    @POST("history/deleteCollection")
    Flowable<ResponseBody> deleteCollection(@Body RequestBody requestBody);//取消收藏


//    @POST("history/deleteCollection")
//    Flowable<HttpResponse<String>> deleteCollection(@Body RequestBody requestBody);//取消收藏


    @POST("ads/alertAd")
    Flowable<ResponseBody> alertAd(@Body RequestBody requestBody);//下载弹出广告


//    @POST("ads/alertAd")
//    Flowable<HttpResponse<List<AdBean>>> alertAd(@Body RequestBody requestBody);//下载弹出广告



    @POST("history/report")
    Flowable<ResponseBody> report(@Body RequestBody requestBody);//记录用户举报


//    @POST("history/report")
//    Flowable<HttpResponse<String>> report(@Body RequestBody requestBody);//记录用户举报

    @POST("user/interestCategory")
    Flowable<ResponseBody> interestCategory(@Body RequestBody requestBody);//用户选择感兴趣的壁纸分类



//    @POST("user/interestCategory")
//    Flowable<HttpResponse<String>> interestCategory(@Body RequestBody requestBody);//用户选择感兴趣的壁纸分类


    @POST("user/login")
    Flowable<ResponseBody> loginPhone(@Body RequestBody requestBody);//手机登录


    @POST("mobile-code/sendMobileCode")
    Flowable<ResponseBody> sendMobileCode(@Body RequestBody requestBody);//验证码


    @POST("history/searchHistory")
    Flowable<ResponseBody> searchHistory(@Body RequestBody requestBody);//记录用户最近5次关键字搜索历史



//    @POST("history/searchHistory")
//    Flowable<HttpResponse<String>> searchHistory(@Body RequestBody requestBody);//记录用户最近5次关键字搜索历史



    /** ------------------------------------------------------------------------------------------------------ */


    @GET("getIpInfo.php")
    Flowable<HttpResponse<IpBean>> getIpMsg(@Query("ip") String ip);//淘宝IP地址



    /** 获取验证码 */
    @POST("mobile-code/send-mobile-code")
    Flowable<HttpResponse<VerCodeBean>> getVerCode();















}
