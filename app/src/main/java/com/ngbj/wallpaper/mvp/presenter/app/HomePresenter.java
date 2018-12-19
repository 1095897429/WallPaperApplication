package com.ngbj.wallpaper.mvp.presenter.app;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.mvp.contract.app.HomeContract;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class HomePresenter extends RxPresenter<HomeContract.View>
                implements HomeContract.Presenter<HomeContract.View> {

    @Override
    public void getSearchHistory() {

        //获取关键字集合
        List<String> stringList = new ArrayList<>();
        stringList.add("二次元");
        stringList.add("一次元");
        stringList.add("三次元");
        stringList.add("四次元");
//        stringList.add("五次元");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("searchWord",stringList);

        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);
        addSubscribe(RetrofitHelper.getApiService()
                .searchHistory(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String result) {
                        mView.showSearchHistory();
                    }
                }));
    }
}
