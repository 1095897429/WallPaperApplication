package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;

import java.util.List;


public interface HomeContract {

    interface View extends BaseContract.BaseView{
        void showSearchHistory();

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        void getSearchHistory();
    }
}
