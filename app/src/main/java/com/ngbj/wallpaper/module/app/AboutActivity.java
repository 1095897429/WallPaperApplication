package com.ngbj.wallpaper.module.app;


import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.umeng.analytics.AnalyticsConfig;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity{


    @BindView(R.id.version)
    TextView mVersion;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }


    @Override
    protected void initData() {
        mVersion.setText("v" + AppHelper.getPackageName(this));
    }

    @OnClick(R.id.back)
    public void Back(){
        finish();
    }


    @OnClick(R.id.name)
    public void Name(){
      String chnnelName = AnalyticsConfig.getChannel(this);
        ToastHelper.customToastView(getApplicationContext(),chnnelName);
    }


}
