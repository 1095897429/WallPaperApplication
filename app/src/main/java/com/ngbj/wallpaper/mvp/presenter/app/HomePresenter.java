package com.ngbj.wallpaper.mvp.presenter.app;

import android.widget.Toast;

import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.mvp.contract.app.HomeContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class HomePresenter extends RxPresenter<HomeContract.View>
                implements HomeContract.Presenter<HomeContract.View> {

    @Override
    public void getSearchHistory() {

        // test -- 获取关键字集合
//        List<String> stringList = new ArrayList<>();
//        stringList.add("二次元");
//        stringList.add("一次元");
//        stringList.add("三次元");
//        stringList.add("四次元");
//        stringList.add("五次元");

        List<String> stringList = new ArrayList<>();
        List<HistoryBean> historyBeanList =  MyApplication.getDbManager().queryHistoryLimitList();
        for (HistoryBean bean : historyBeanList) {
            stringList.add(bean.getHistoryName());
        }

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("searchWord",stringList);

        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .searchHistory(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){

                        }else{
//                            mView.showError(response.getMessage());
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .searchHistory(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String result) {
//
//                    }
//                }));
    }
}
