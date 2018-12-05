package com.ngbj.wallpaper.base;

import android.app.Application;

import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {

    private static DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //友盟初始化
        UMConfigure.init(this,"5bea861cb465f54c850001be","umeng" ,UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);

        //DBManager初始化
        dbManager = DBManager.getInstance(this);
    }

    public static DBManager getDbManager() {
        return dbManager;
    }
}
