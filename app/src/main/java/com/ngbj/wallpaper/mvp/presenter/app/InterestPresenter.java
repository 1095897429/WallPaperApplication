package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.BaseListSubscriber;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.mvp.contract.app.InterestContract;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class InterestPresenter extends RxPresenter<InterestContract.View>
                implements InterestContract.Presenter<InterestContract.View> {


    @Override
    public void getInterestData() {
        addSubscribe(RetrofitHelper.getApiService()
                .categoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<InterestBean>(mView) {
                    @Override
                    public void onSuccess(List<InterestBean> interestBeanList) {
                        mView.showInterestData(interestBeanList);
                    }
                }));
//        test();
    }

    @Override
    public void writeInterestData(String jsonListString) {
        addSubscribe(RetrofitHelper.getApiService()
                .interestCategory(jsonListString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        mView.showWriteInterestData(string);
                    }
                }));
    }


    public void test(){
        List<InterestBean> interestBeanList = new ArrayList<>();
        interestBeanList.add(new InterestBean("明星写真","1",false));
        interestBeanList.add(new InterestBean("体育竞技","2",false));
        interestBeanList.add(new InterestBean("浪漫爱情","3",false));
        interestBeanList.add(new InterestBean("清新可爱","4",false));
        interestBeanList.add(new InterestBean("明星写真","5",false));
        interestBeanList.add(new InterestBean("体育竞技","6",false));
        interestBeanList.add(new InterestBean("浪漫爱情","7",false));
        interestBeanList.add(new InterestBean("清新可爱","8",false));
        interestBeanList.add(new InterestBean("明星写真","9",false));
        interestBeanList.add(new InterestBean("体育竞技","10",false));
        interestBeanList.add(new InterestBean("浪漫爱情","11",false));
        interestBeanList.add(new InterestBean("清新可爱","12",false));
        interestBeanList.add(new InterestBean("明星写真","12",false));
        interestBeanList.add(new InterestBean("体育竞技","13",false));
        interestBeanList.add(new InterestBean("浪漫爱情","14",false));
        interestBeanList.add(new InterestBean("清新可爱","15",false));
        interestBeanList.add(new InterestBean("清新可爱","16",false));
        interestBeanList.add(new InterestBean("明星写真","17",false));
        interestBeanList.add(new InterestBean("体育竞技","18",false));
        interestBeanList.add(new InterestBean("浪漫爱情","19",false));
        interestBeanList.add(new InterestBean("清新可爱","20",false));
        interestBeanList.add(new InterestBean("明星写真","21",false));
        interestBeanList.add(new InterestBean("体育竞技","22",false));
        interestBeanList.add(new InterestBean("浪漫爱情","23",false));
        interestBeanList.add(new InterestBean("清新可爱","24",false));

        mView.showInterestData(interestBeanList);
    }
}
