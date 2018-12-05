package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;

import java.util.ArrayList;
import java.util.List;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class DetailPresenter extends RxPresenter<DetailContract.View>
                implements DetailContract.Presenter<DetailContract.View> {


    @SuppressLint("CheckResult")
    @Override
    public void getVerCodeData() {

        mView.showVerCodeData();
    }

    @Override
    public void getDynamicData() {
        List<AdBean> list = new ArrayList<>();
        list.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        list.add(new AdBean("猫妖传","http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg"));
        list.add(new AdBean("吐槽大会","http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg"));
        list.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        list.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        list.add(new AdBean("猫妖传","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        list.add(new AdBean("吐槽大会","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        list.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        mView.showDynamicData(list);
    }

}
