package com.ngbj.wallpaper.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 备注：基类的Fragment
 *       1.懒加载 -- 在状态可见 + 控件初始化完成（看下面的setUserVisibleHint方法）
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter>
        extends RxFragment implements BaseContract.BaseView {

    protected T mPresenter;
    protected View mRootView;//加载的View
    protected Context mContext;//上下文
    private Unbinder unbinder;

    protected boolean isVisible;//fragment是否可见
    protected boolean isPrepared;//控件初始化完成



    /** 简介1：当fragment 结合 viewpager使用时候 这个方法会调用*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){ // 相当于onResume()方法
            isVisible = true;
            onVisible();
        }else{    // 相当于onpause()方法
            isVisible = false;
        }
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView){
          mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(),container,false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    /** P 绑定 V  */
    protected void initInject() {
        if(null != mPresenter){
            mPresenter.attachView(this);
        }
    }

    /** onDestroyView中进行解绑操作 */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(null != mPresenter)mPresenter.detachView();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    /** 初始化控件方法 */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVidget();
        initPresenter();
        initInject();
        initData();
        finishCreateView();
        initEvent();
    }



    /** Presenter 初始化方法 */
    protected void initPresenter() {}

    protected  void initData(){}

    protected void initEvent() {}


    /** 当状态和控件都可见时才加载数据 */
    protected void finishCreateView() {
        isPrepared = true;
        lazyLoad();
    }

    /** 对各种控件进行设置、适配、填充数据 */
    protected void initVidget() {}

    protected abstract int getLayoutId();

    private void onVisible() {
        lazyLoad();
    }

    /**  懒加载方法 需要状态可见 + 控件初始化完成 */
    private void lazyLoad() {
        if(!isVisible || !isPrepared ) return;
        lazyLoadData();
        isPrepared = false;
    }

    /**  加载数据 */
    protected void lazyLoadData() {}

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }


    /** ---------------- 公共方法  ------------------  */

    protected void insertToSql(int page ,final List<MulAdBean> recommendList, final String fromWhere){

        if(1 == page){
            MyApplication.getDbManager().deleteWallpagerBeanList(fromWhere);

        }

        //TODO 线程加入到数据库中 -- 先删除，后添加
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpagerBean wallpagerBean;
                AdBean adBean ;
                ApiAdBean apiAdBean;
                KLog.d(" --- ");
                for (MulAdBean bean: recommendList) {
                    if(bean.getItemType() == MulAdBean.TYPE_ONE){
                        adBean = bean.adBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(adBean.getType());
                        wallpagerBean.setIs_collected(adBean.getIs_collected());
                        wallpagerBean.setMovie_url(adBean.getMovie_url());
                        wallpagerBean.setWallpager_id(adBean.getId());
                        wallpagerBean.setNickname(adBean.getNickname());
                        wallpagerBean.setTitle(adBean.getTitle());
                        wallpagerBean.setHead_img(adBean.getHead_img());
                        wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                        apiAdBean = bean.apiAdBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(apiAdBean.getType());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }
                }
            }
        }).start();
    }




    protected List<WallpagerBean> transformDataToWallpaper(List<MulAdBean> recommendList) {
        WallpagerBean wallpagerBean;
        AdBean adBean ;
        ApiAdBean apiAdBean;
        List<WallpagerBean> tempList = new ArrayList<>();
        for (MulAdBean bean: recommendList) {
            if(bean.getItemType() == MulAdBean.TYPE_ONE){//把广告当成壁纸，也使用id去构建wallpaperId
                adBean = bean.adBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(adBean.getType());
                wallpagerBean.setIs_collected(adBean.getIs_collected());
                wallpagerBean.setMovie_url(adBean.getMovie_url());
                wallpagerBean.setWallpager_id(adBean.getId());
                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(apiAdBean.getType());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }



}
