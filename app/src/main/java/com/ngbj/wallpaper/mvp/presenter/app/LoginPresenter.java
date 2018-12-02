package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class LoginPresenter extends RxPresenter<LoginContract.View>
                implements LoginContract.Presenter<LoginContract.View> {
    @SuppressLint("CheckResult")
    @Override
    public void getVerCodeData() {
        Gson gson = new Gson();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("mobile","15240128165");
        String strEntity = gson.toJson(hashMap);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"),strEntity);
        addSubscribe(RetrofitHelper.getApiService()
                .getVerCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<VerCodeBean>(mView) {
                    @Override
                    public void onSuccess(VerCodeBean verCodeBean) {
                        mView.showVerCodeData();
                    }
                }));
    }

}
