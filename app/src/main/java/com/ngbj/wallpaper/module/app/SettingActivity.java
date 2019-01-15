package com.ngbj.wallpaper.module.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.BottomListAlertDialog;
import com.ngbj.wallpaper.dialog.IosAlertDialog;
import com.ngbj.wallpaper.eventbus.TagListPositionEvent;
import com.ngbj.wallpaper.eventbus.activity.LoginSuccessEvent;
import com.ngbj.wallpaper.eventbus.fragment.ExitRefreshEvent;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.utils.common.SPHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class  SettingActivity extends BaseActivity{



    @BindView(R.id.logout)
    TextView logout;

    @BindView(R.id.cache_text)
    TextView cacheText;

    @BindView(R.id.root_layout)
    ConstraintLayout root_layout;//底部布局

    List<UploadTagBean> temps = new ArrayList<>();


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
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
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

        temps.add(new UploadTagBean("不超过100M",false));
        temps.add(new UploadTagBean("不超过300M",false));
        temps.add(new UploadTagBean("不超过500M",false));
        temps.add(new UploadTagBean("不超过1G",false));


        LoginBean bean = MyApplication.getDbManager().queryLoginBean();
        if(bean != null){
            logout.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.logout)
    public void Logout(){
        final IosAlertDialog iosAlertDialog = new IosAlertDialog(this).builder();
        iosAlertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //删除用户信息
                MyApplication.getDbManager().deleteAllLoginBean();

                //退出发送的事件
                LoginBean bean = new LoginBean();

                EventBus.getDefault().post(new ExitRefreshEvent());
                EventBus.getDefault().post(new LoginSuccessEvent(bean));

                finish();

            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        }).setTitle("退出登录").setMsg("是否退出登录").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();

    }


    @OnClick(R.id.part2)
    public void Part2(){
        int position = (int) SPHelper.get(this,"clearPosition",0);

        new BottomListAlertDialog(this,temps,position)
                .builder()
                .show();
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
        }).setTitle("清除缓存").setMsg("是否清除缓存").setCanceledOnTouchOutside(false);
        iosAlertDialog.show();
    }


    /** --------------  底部标签 EventBus 开始-----------------*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /** 记录选中的类型 */
    @Subscribe
    public void onTagListPositionEvent(TagListPositionEvent event){
        Map<Integer,UploadTagBean> hashMap = event.getMap();

        temps.clear();

        for (UploadTagBean value : hashMap.values()) {
            temps.add(value);//更新数据源
        }

    }


    /** --------------   底部标签 EventBus 结束 -----------------*/

}
