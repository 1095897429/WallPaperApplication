package com.ngbj.wallpaper.mvp.presenter.app;

import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.mvp.contract.app.ReleaseContract;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class ReleasePresenter extends RxPresenter<ReleaseContract.View>
                implements ReleaseContract.Presenter<ReleaseContract.View> {


    @Override
    public void getUploadToken() {

        addSubscribe(RetrofitHelper.getApiService()
                .getUploadToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<UploadTokenBean>(mView) {
                    @Override
                    public void onSuccess(UploadTokenBean uploadTokenBean) {
                        mView.shwoUploadToken(uploadTokenBean);
                    }
                }));

    }
}
