package com.ngbj.wallpaper.module.app;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.socks.library.KLog;

import butterknife.OnClick;

/***
 * 免责条款
 */
public class SafeActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_safe;
    }

    @Override
    protected void initPresenter() {

    }


}
