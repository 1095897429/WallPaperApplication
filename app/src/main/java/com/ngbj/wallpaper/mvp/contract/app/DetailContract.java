package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;

import java.util.List;

/***
 * 壁纸详情页契约类
 */
public interface DetailContract {

    interface View extends BaseContract.BaseView{
        void showVerCodeData();
        void showDynamicData(List<AdBean> list);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getVerCodeData();
        void getDynamicData();
    }
}
