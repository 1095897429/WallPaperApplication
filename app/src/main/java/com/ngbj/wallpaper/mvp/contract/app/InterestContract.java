package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;

import java.util.List;


public interface InterestContract {

    interface View extends BaseContract.BaseView{
        void showInterestData(List<InterestBean> interestBeanList);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getInterestData();
    }
}
