package com.ngbj.wallpaper;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //友盟初始化
        UMConfigure.init(this,"5bea861cb465f54c850001be","umeng" ,UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);
    }
}
