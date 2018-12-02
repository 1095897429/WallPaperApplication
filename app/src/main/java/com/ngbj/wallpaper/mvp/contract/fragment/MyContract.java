package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.AdBean;
import com.ngbj.wallpaper.bean.MulAdBean;

import java.util.List;

public interface MyContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getAdData(String type);
        void getMoreData(String type);
    }

    interface View extends BaseContract.BaseView{
        void showAdData(List<AdBean> list);
        void showMoreData(List<AdBean> list);
    }
}
