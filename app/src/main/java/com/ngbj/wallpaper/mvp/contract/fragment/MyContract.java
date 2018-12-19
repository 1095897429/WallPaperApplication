package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;

import java.util.List;

public interface MyContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{


        /** -------------- */
        void getUploadHistory(String accessToken);
        void getUploadHeadData(String accessToken,String base64Img);
        void getRecord(String type);


    }

    interface View extends BaseContract.BaseView{

        /** -------------- */
        void showUploadHistory(List<AdBean> list);
        void showUploadHeadData(LoginBean loginBean);
        void showRecord(List<AdBean> list);

    }
}
