package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;

import java.util.List;


public interface ReleaseContract {

    interface View extends BaseContract.BaseView{
        void shwoUploadToken(UploadTokenBean uploadTokenBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getUploadToken();
    }
}
