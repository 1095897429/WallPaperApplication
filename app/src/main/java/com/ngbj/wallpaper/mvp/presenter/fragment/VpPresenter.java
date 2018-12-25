package com.ngbj.wallpaper.mvp.presenter.fragment;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.mvp.contract.fragment.VpContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.utils.common.AppHelper;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class VpPresenter extends RxPresenter<VpContract.View>
            implements VpContract.Presenter<VpContract.View>{


    /** 壁纸明细 */
    @SuppressLint("CheckResult")
    @Override
    public void getData(final String wallpaperId) {

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

                        mView.showData(item);
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


}
