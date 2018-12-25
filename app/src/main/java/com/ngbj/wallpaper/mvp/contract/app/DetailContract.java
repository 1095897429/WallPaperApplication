package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;

import java.util.List;

/***
 * 壁纸详情页契约类
 */
public interface DetailContract {

    interface View extends BaseContract.BaseView{
        void showData(AdBean adBean);
        void showDynamicData(List<WallpagerBean> list);

        void showReportData();
        void showRecordData();

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getData(String wallpaperId);
        void getDynamicData();

        void getReportData(String wallpaperId,String type);
        void getRecordData(String wallpaperId,String type);
    }
}
