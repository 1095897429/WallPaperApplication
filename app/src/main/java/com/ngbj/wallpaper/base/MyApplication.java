package com.ngbj.wallpaper.base;

import android.app.Activity;
import android.app.Application;

import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/***
 * 单例
 */
public class MyApplication extends Application {

    private static DBManager dbManager;
    private static MyApplication myApplication;

    public static MyApplication getInstance(){
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);


        //友盟初始化
        UMConfigure.init(this,"5bea861cb465f54c850001be","umeng" ,UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);

        //DBManager初始化
        dbManager = DBManager.getInstance(this);
    }

    public static DBManager getDbManager() {
        return dbManager;
    }


    /** activity集合 开始 */
    public Set<Activity> mActivities = new HashSet<>();

    //新增
    public void addActivity(Activity activity){
        mActivities.add(activity);
    }

    //移除
    public void removeActivity(Activity activity){
        mActivities.remove(activity);
    }

    //退出
    public void exitApp(){
        for (Activity activity: mActivities) {
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());//杀死该进程
    }

    /** activity集合 结束 */



}
