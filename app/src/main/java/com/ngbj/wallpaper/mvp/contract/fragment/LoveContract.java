package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;

public interface LoveContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getRecordData(String wallpaperId, String type);
    }

    interface View extends BaseContract.BaseView{
        void showRecordData();
    }
}
