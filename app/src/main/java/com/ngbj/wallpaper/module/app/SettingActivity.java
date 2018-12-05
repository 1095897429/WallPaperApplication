package com.ngbj.wallpaper.module.app;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.socks.library.KLog;

import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class SettingActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter();
    }

    @OnClick(R.id.logout)
    public void Logout(){
        KLog.d("logout");
    }

}
