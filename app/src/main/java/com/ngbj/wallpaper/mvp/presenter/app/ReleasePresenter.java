package com.ngbj.wallpaper.mvp.presenter.app;

import com.ngbj.wallpaper.base.BaseListSubscriber;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.mvp.contract.app.ReleaseContract;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class ReleasePresenter extends RxPresenter<ReleaseContract.View>
                implements ReleaseContract.Presenter<ReleaseContract.View> {



    @Override
    public void getInterestData() {

        addSubscribe(RetrofitHelper.getApiService()
                .categoryList(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<InterestBean>(mView) {
                    @Override
                    public void onSuccess(List<InterestBean> interestBeanList) {
                        mView.showInterestData(interestBeanList);
                    }
                }));
    }

    @Override
    public void getUploadToken() {

        addSubscribe(RetrofitHelper.getApiService()
                .getUploadToken(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<UploadTokenBean>(mView) {
                    @Override
                    public void onSuccess(UploadTokenBean uploadTokenBean) {
                        mView.shwoUploadToken(uploadTokenBean);
                    }
                }));

    }

    @Override
    public void uploadWallpaper(String accessToken, Map<String, Object> map) {

        RequestBody requestBody = OkHttpHelper.getRequestBody(map);

        addSubscribe(RetrofitHelper.getApiService()
                .uploadWallpaper(accessToken,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        KLog.d("result: " + string);
                        mView.showUploadWallpaper();
                    }
                }));
    }
}
