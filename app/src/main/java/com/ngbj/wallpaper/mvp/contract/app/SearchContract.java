package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;

import java.util.List;


public interface SearchContract {

    interface View extends BaseContract.BaseView{
        void showHotWordsAndAd(List<AdBean> hotWords,List<AdBean> ads);
        void showHistoryData(List<HistoryBean> historys);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getHotWordsAndAd();
        void getHistoryData();
    }
}
