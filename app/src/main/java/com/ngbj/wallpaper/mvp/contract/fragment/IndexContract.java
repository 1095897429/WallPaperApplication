package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.AdBean;
import com.ngbj.wallpaper.bean.MulAdBean;

import java.util.List;

public interface IndexContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getAdData();
        void getRecommendData();
        void getMoreRecommendData();
    }

    interface View extends BaseContract.BaseView{
        void showAdData(List<AdBean> list,List<AdBean> bannerList,List<AdBean> coolList);
        void showRecommendData(List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
    }
}
