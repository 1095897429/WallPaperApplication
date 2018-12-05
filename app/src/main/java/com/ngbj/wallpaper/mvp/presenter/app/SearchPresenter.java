package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;

import java.util.ArrayList;
import java.util.List;


public class SearchPresenter extends RxPresenter<SearchContract.View>
                implements SearchContract.Presenter<SearchContract.View> {

    @SuppressLint("CheckResult")
    @Override
    public void getHotWordsAndAd() {
        List<AdBean> hotSearchList = new ArrayList<>();
        hotSearchList.add(new AdBean("奇葩说",""));
        hotSearchList.add(new AdBean("猫妖传",""));
        hotSearchList.add(new AdBean("吐槽大会",""));
        hotSearchList.add(new AdBean("开心麻花",""));
        hotSearchList.add(new AdBean("齐天大圣",""));

        List<AdBean> adList = new ArrayList<>();
        adList.add(new AdBean("",""));

       mView.showHotWordsAndAd(hotSearchList,adList);
    }

    @Override
    public void getHistoryData() {
        List<HistoryBean> historyBeanList = MyApplication.getDbManager().queryHistoryList();
        if(historyBeanList.isEmpty()){
            historyBeanList.add(new HistoryBean("奇葩说"));
            historyBeanList.add(new HistoryBean("猫妖传"));
            historyBeanList.add(new HistoryBean("吐槽大会"));
            historyBeanList.add(new HistoryBean("开心麻花"));
            MyApplication.getDbManager().insertHistoryList(historyBeanList);//插入
        }

        mView.showHistoryData(historyBeanList);
    }

}
