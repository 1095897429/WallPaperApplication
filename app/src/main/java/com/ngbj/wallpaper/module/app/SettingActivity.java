package com.ngbj.wallpaper.module.app;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.IosAlertDialog;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.socks.library.KLog;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class  SettingActivity extends BaseActivity{

    @BindView(R.id.cache_text)
    TextView cacheText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter();
    }

    @OnClick(R.id.part3)
    public void Part3(){
      startActivity(new Intent(this,SafeActivity.class));
    }

    @OnClick(R.id.part4)
    public void Part4(){
       AboutActivity.openActivity(this);
    }



    @OnClick(R.id.back)
    public void Back(){
        finish();
    }


    @Override
    protected void initData() {

        /** 目前获取随机数  */
        Random rand = new Random();
        int i = rand.nextInt(10) + 1;//0 - 9 --> 1 - 10
        cacheText.setText(i + "M");
    }

    @OnClick(R.id.logout)
    public void Logout(){
        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getDbManager().deleteAllLoginBean();
                MyApplication.getInstance().exitApp();
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        }).setTitle("标题").setMsg("是否退出登录").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();



    }


    @OnClick(R.id.cache_part)
    public void cachePart(){
        showIosDialog();
    }

    private void showIosDialog() {
        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cacheText.setText("0M");
                SPHelper.put(SettingActivity.this,AppConstant.ISCLEARCACHE,true);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).setTitle("标题").setMsg("是否清除缓存").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();
    }

}
