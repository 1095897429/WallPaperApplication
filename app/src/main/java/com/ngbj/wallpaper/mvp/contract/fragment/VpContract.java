package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface VpContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getReportData(String wallpaperId,String type);
        void getRecordData(String wallpaperId,String type);
    }

    interface View extends BaseContract.BaseView{
        void showReportData();
        void showRecordData();
    }
}
