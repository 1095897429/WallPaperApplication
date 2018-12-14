package com.ngbj.wallpaper.module.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.mvp.presenter.app.SearchPresenter;
import com.ngbj.wallpaper.mvp.presenter.app.SplashPresenter;
import com.socks.library.KLog;

/***
 * 闪屏界面
 */
public class SplashActivity extends BaseActivity<SplashPresenter>
                implements SplashContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //解决下载完成后点击 安装还是完成的bug
        if(!isTaskRoot()){
            finish();
            return;
        }
    }

    @Override
    protected void initData() {

        mPresenter.initUserInfo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,InterestActivity.class));
                finish();
            }
        },1000);
    }

    @Override
    public void showInitUserInfo(InitUserBean initUserBean) {
        KLog.d("实体:" + initUserBean.getDownload_url());

    }
}
