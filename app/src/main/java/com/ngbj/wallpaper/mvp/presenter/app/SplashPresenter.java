package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class SplashPresenter extends RxPresenter<SplashContract.View>
                implements SplashContract.Presenter<SplashContract.View> {


    @Override
    public void initUserInfo() {

        //测试数据
//        InitUserBean initUserBean = new InitUserBean();
//        initUserBean.setDownload_url("");
//        mView.showInitUserInfo(initUserBean);

        addSubscribe(RetrofitHelper.getApiService()
                .initUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<InitUserBean>(mView) {
                    @Override
                    public void onSuccess(InitUserBean initUserBean1) {
                        mView.showInitUserInfo(initUserBean1);
                    }
                }));

    }
}
