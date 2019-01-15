package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface UserContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getUploadUserData(String accessToken,String nickName,int gender);


    }

    interface View extends BaseContract.BaseView{

      void showUploadUserData(LoginBean loginBean);

    }
}
