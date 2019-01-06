package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.BannerDetailBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface SpecialContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getRecommendData(String bannerId);
        void getMoreRecommendData();

        void getRecordData(String wallpaperId, String type);
        void getDeleteCollection(String wallpaperId);
    }

    interface View extends BaseContract.BaseView{
        void showRecommendData(BannerDetailBean bannerDetailBean,List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);

        void showRecordData();
        void showDeleteCollection();
    }
}
