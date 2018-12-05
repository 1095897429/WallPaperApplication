package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.mvp.contract.app.InterestContract;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;

import java.util.ArrayList;
import java.util.List;


public class InterestPresenter extends RxPresenter<InterestContract.View>
                implements InterestContract.Presenter<InterestContract.View> {



    @Override
    public void getInterestData() {
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
        interestBeanList.add(new InterestBean("清新可爱","8",false));
        interestBeanList.add(new InterestBean("明星写真","9",false));
        interestBeanList.add(new InterestBean("体育竞技","10",false));
        interestBeanList.add(new InterestBean("浪漫爱情","11",false));
        interestBeanList.add(new InterestBean("清新可爱","12",false));
        interestBeanList.add(new InterestBean("明星写真","12",false));
        interestBeanList.add(new InterestBean("体育竞技","13",false));
        interestBeanList.add(new InterestBean("浪漫爱情","14",false));
        interestBeanList.add(new InterestBean("清新可爱","15",false));

        mView.showInterestData(interestBeanList);
    }
}
