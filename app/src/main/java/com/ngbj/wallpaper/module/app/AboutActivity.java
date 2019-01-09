package com.ngbj.wallpaper.module.app;


import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.utils.common.AppHelper;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity{


    @BindView(R.id.version)
    TextView mVersion;

//    public static void openActivity(Context context){
//        Intent intent = new Intent(context,AboutActivity.class);
//        context.startActivity(intent);
//    }

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


}
