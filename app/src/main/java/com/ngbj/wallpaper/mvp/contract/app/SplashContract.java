package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;

/***
 * 欢迎页契约类
 */
public interface SplashContract {

    interface View extends BaseContract.BaseView{
        void showInitUserInfo(InitUserBean initUserBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void initUserInfo();
    }
}
