package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;

import java.util.List;
import java.util.Map;


public interface ReleaseContract {

    interface View extends BaseContract.BaseView{
        void shwoUploadToken(UploadTokenBean uploadTokenBean);
        void showUploadWallpaper();
        void showInterestData(List<InterestBean> interestBeanList);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getInterestData();
        void getUploadToken();
        void uploadWallpaper(String accessToken, Map<String,Object> map);
    }
}
