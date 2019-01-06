package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseListSubscriber;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.LoadingDialog;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class DetailPresenter extends RxPresenter<DetailContract.View>
                implements DetailContract.Presenter<DetailContract.View> {


    /** 主界面 */
    @Override
    public void getIndexRecommendData(int page) {
        addSubscribe(RetrofitHelper.getApiService()
                .index(page,OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<IndexBean>(mView) {
                    @Override
                    public void onSuccess(IndexBean indexBean) {

                        List<AdBean> recommendList = indexBean.getRecommend();
                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(recommendList);
                         mView.showIndexRecommendData(list);

                    }
                }));
    }


    /** 分类界面 */
    @Override
    public void getMoreRecommendData(final int page, String category, String order) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("categoryId",category);
        hashMap.put("order",order);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .wallpagerList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        mView.showMoreRecommendData(list);

                    }
                }));
    }



    /*************************** 搜索 *******************************/

    @Override
    public void getMoreKeySearchData(int page, String keyWord) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("keyWord",keyWord);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .search(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        mView.showMoreKeySearchData(list);

                    }
                }));
    }

    @Override
    public void getMoreNavigationData(int page, String navigation) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("navigationId",navigation);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .navigationList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        mView.showMoreNavigationData(list);

                    }
                }));
    }

    @Override
    public void getMoreHotSearchData(int page, String hotSearchTag) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("hotSearchTag",hotSearchTag);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .hotSearchList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        mView.showMoreHotSearchData(list);
                    }
                }));
    }

    /**************************** 搜索 ******************************/

    /** 取消收藏 */
    @Override
    public void getDeleteCollection(String wallpaperId) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .deleteCollection(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        mView.showDeleteCollection();
                    }
                }));
    }


    /** 记录用户举报 1色情低俗 2侵犯版权 3其他*/
    @Override
    public void getReportData(String wallpaperId, String type) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("type",type);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .report(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        mView.showReportData();
                    }
                }));
    }


    /** 记录用户下载 1下载 2收藏 3分享 */
    @Override
    public void getRecordData(String wallpaperId, String type) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("type",type);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .record(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        mView.showRecordData();
                    }
                }));
    }


    /** 壁纸明细 */
    @SuppressLint("CheckResult")
    @Override
    public void getDetailData(final String wallpaperId) {

        Gson gson = new Gson();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("fromPlat", "default");
        hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
        hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
        hashMap.put("deviceType", "android");
        hashMap.put("timestamp", System.currentTimeMillis() + "");
        hashMap.put("sign", "");
        String strEntity = gson.toJson(hashMap);
        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);

        addSubscribe(RetrofitHelper.getApiService()
                .detail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(AdBean item) {

                        mView.showDetailData(item);
                    }
                }));
    }

}
