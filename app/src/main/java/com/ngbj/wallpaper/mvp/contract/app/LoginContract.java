package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;

/***
 * 登录契约类
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView{
        void showVerCodeData(VerCodeBean verCodeBean);
        void showLoginData(LoginBean loginBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getVerCodeData();
        void getLoginData();
    }
}
