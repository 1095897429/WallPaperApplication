package com.ngbj.wallpaper.module.app;


import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;

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


    @OnClick(R.id.back)
    public void Back(){
        finish();
    }



}
