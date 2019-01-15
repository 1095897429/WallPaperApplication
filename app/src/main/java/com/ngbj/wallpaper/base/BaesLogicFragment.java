package com.ngbj.wallpaper.base;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.utils.common.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/***
 * 统一点击逻辑事件的基类
 */
public abstract class BaesLogicFragment<T extends BaseContract.BasePresenter> extends BaseFragment<T>{

    protected RecomendAdapter recomendAdapter;
    protected GridLayoutManager gridLayoutManager;
//    protected List<MulAdBean> recommendList = new ArrayList<>();
//    protected AdBean mAdBean;


    @Override
    protected void initEvent() {

    }


}
