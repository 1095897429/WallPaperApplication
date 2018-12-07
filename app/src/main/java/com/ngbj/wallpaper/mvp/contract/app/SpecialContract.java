package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface SpecialContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getRecommendData();
        void getMoreRecommendData();
    }

    interface View extends BaseContract.BaseView{
        void showRecommendData(List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
    }
}
