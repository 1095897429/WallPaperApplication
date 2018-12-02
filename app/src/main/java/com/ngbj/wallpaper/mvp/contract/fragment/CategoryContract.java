package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.AdBean;
import com.ngbj.wallpaper.bean.MulAdBean;

import java.util.List;

public interface CategoryContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getData();
        void getMoreRecommendData();
    }

    interface View extends BaseContract.BaseView{
        void showData(List<AdBean> topList, List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
    }
}
