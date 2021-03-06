package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface MyContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{


        /** -------------- */
        void getUploadHistory(String accessToken);
        void getUploadHeadData(String accessToken,String base64Img);
        void getRecord(String type);

        void getRecordData(String wallpaperId, String type);
        void getDeleteCollection(String wallpaperId);


    }

    interface View extends BaseContract.BaseView{

        /** -------------- */
        void showUploadHistory(List<MulAdBean> list);
        void showUploadHeadData(LoginBean loginBean);
        void showRecord(List<MulAdBean> list);

        void showRecordData();
        void showDeleteCollection();

    }
}
