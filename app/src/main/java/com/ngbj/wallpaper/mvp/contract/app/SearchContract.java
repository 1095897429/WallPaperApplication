package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;


public interface SearchContract {

    interface View extends BaseContract.BaseView{
        void showHotWordsAndAd(List<AdBean> hotWords,List<AdBean> ads);
        void showHistoryData(List<HistoryBean> historys);
        void showRecommendData(List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getHotWordsAndAd();
        void getHistoryData();
        void getRecommendData();
        void getMoreRecommendData();

    }
}
