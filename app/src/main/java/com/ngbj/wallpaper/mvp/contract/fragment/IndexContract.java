package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface IndexContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getAdData(int page);
        void getMoreRecommendData(int page);
        void getRecommendData();
    }

    interface View extends BaseContract.BaseView{
        void showAdData(List<IndexBean.HotSearch> list, List<IndexBean.Banner> bannerList,
                        List<IndexBean.Navigation> coolList);
        void showRecommendData(List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
    }
}
