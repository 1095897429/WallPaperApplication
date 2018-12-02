package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;

/***
 * 登录契约类
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView{
        void showVerCodeData();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getVerCodeData();
    }
}
