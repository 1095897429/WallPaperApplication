package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
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
public class DetailPresenter extends RxPresenter<DetailContract.View>
                implements DetailContract.Presenter<DetailContract.View> {


    @SuppressLint("CheckResult")
    @Override
    public void getVerCodeData() {
        //TODO 数据库的模拟
        mView.showVerCodeData();
    }

    @Override
    public void getDynamicData() {
        mView.showDynamicData();
    }

}
