package com.ngbj.wallpaper.base;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

import com.liulishuo.filedownloader.FileDownloader;
import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

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

    //各个平台的配置 -- 这里就相当于在友盟后台配置了对应相应的第三方平台了！！
    {
        PlatformConfig.setWeixin("wxfee3736d701ee038", "2aafaf14129abaa6fa481a5be18cb23a");
        PlatformConfig.setSinaWeibo("278771476", "65573b2d945fa3d4a3bf847e99c7fb58","http://www.birdbrowser.info");
        PlatformConfig.setQQZone("1107983871", "tRzRpAdsQLr3jqUl");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        MultiDex.install(this);


        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);


        //友盟初始化
        String channelName = StringUtils.getChannelFromApk(this,"channel");
        UMConfigure.init(this,"5bea861cb465f54c850001be",channelName ,UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(false);

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
