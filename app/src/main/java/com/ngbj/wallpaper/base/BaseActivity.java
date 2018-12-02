package com.ngbj.wallpaper.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/***
 * 基础的Activity
 * 1.通过泛型传递P
 * 2.加载通用的布局
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter>
        extends RxAppCompatActivity implements BaseContract.BaseView{

    protected T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initPresenter();
        initInject();
        initData();
        initEvent();
    }




    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);  //APP状态统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);  //APP状态统计
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.detachView();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    /** 数据初始化方法 */
    protected void initData() {

    }

    protected void initEvent() {}

    /** P 绑定 V  */
    protected void initPresenter() {

    }

    /** Presenter 初始化方法 */
    protected void initInject() {
        if(null != mPresenter){
            mPresenter.attachView(this);
        }
    }



    /** ---------------- 抽象的构造方法  ------------------  */
    protected abstract int getLayoutId();
}
