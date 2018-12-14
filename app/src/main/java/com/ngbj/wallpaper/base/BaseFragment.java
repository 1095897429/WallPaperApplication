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

import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 备注：基类的Fragment
 *       1.懒加载 -- 在状态可见 + 控件初始化完成
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter>
        extends RxFragment implements BaseContract.BaseView {

    protected T mPresenter;
    protected View mRootView;//加载的View
    protected Context mContext;//上下文
    private Unbinder unbinder;

    protected boolean isVisible;//fragment是否可见
    protected boolean isPrepared;//控件初始化完成



    /** 当fragment 结合 viewpager使用时候 这个方法会调用*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        KLog.d("setUserVisibleHint");
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else{
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




}
