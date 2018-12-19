package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

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
    public void getVerCodeData(String phone) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .sendMobileCode(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String result) {
                        KLog.d("result: " + result);
                        mView.showVerCodeData();
                    }
                }));
    }

    @Override
    public void getLoginData(String phone,String code) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        hashMap.put("code",code);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .loginPhone(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<LoginBean>(mView) {
                    @Override
                    public void onSuccess(LoginBean loginBean) {

                        mView.showLoginData(loginBean);
                    }
                }));
    }

}
