package com.ngbj.wallpaper.base;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.DownBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.StatisticsBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.SplashActivity;
import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/***
 * 基础的Activity
 * 1.通过泛型传递P
 * 2.加载通用的布局
 * 3.成为观察者
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter>
        extends RxAppCompatActivity implements BaseContract.BaseView,NetBroadcastReceiver.NetChangeListener{

    protected T mPresenter;
    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//全部禁止横屏
        super.onCreate(savedInstanceState);

        /** 2019.1.10 当在app中打开了某个权限，设置中设置否定，返回app时 saveInstanceState不为空，让其重启 */
        /** app中没有设置，去设置中打开 返回app 不强退 */
        if(savedInstanceState!=null){
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mContext = this;
        initPresenter();
        initInject();
        initData();
        initEvent();
        MyApplication.getInstance().addActivity(this);
        initBroadcastRecevier();
    }




    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);  //APP状态统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);  //APP状态统计
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPresenter)mPresenter.detachView();
        if(null != mNetBroadcastReceiver)
            unregisterReceiver(mNetBroadcastReceiver);//解绑
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    /** 数据初始化方法 */
    protected void initData() {

    }

    protected void initEvent() {}

    /** P 绑定 V  */
    protected void initPresenter() {

    }

    /** Presenter 初始化方法 */
    protected void initInject() {
        if(null != mPresenter){
            mPresenter.attachView(this);
        }
    }



    /** ---------------- 抽象的构造方法  ------------------  */
    protected abstract int getLayoutId();


    /** ---------------- 实现的方法 开始 ------------------  */

    private int netType;//网络类型
    private NetBroadcastReceiver mNetBroadcastReceiver;

    /** 7.0之后静态方式取消 注册网咯状态监听广播 */
    private void initBroadcastRecevier() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mNetBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(mNetBroadcastReceiver, filter);
            //注册观察者
            mNetBroadcastReceiver.setNetChangeListener(this);
        }
    }


    /** 初始化时判断有没有网络 */
    public boolean checkNet() {
        this.netType = NetBroadcastReceiver.getNetWorkState(BaseActivity.this);
        if (!isNetConnect()) {
            //网络异常，请检查网络
            KLog.d("网络异常，请检查网络");
        }
        return isNetConnect();
    }

    /** 网络变化之后的类型 */
    @Override
    public void onChangeListener(int netType) {
        // TODO Auto-generated method stub
        this.netType = netType;
//        KLog.d("netType", "netType:" + netType);
        if (!isNetConnect()) {
            ToastHelper.customToastView(this,"网络异常，请检查网络");
        } else {
//            ToastHelper.customToastView(this,"网络恢复正常");
        }
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }

    /** ---------------- 实现的方法 结束 ------------------  */

    /** ---------------- 查询 下载次数逻辑  ------------------  */
    SimpleDateFormat mSimpleDateFormat;
    Date mDate;
    String mCurrentYear_Month_Day;
    String mHistoryYear_Month_Day;
    DownBean mDownBean;
    int count ;

    //获取下载次数
    protected int getDownCount(){
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mDate = new Date(System.currentTimeMillis());
        mCurrentYear_Month_Day = mSimpleDateFormat.format(mDate);
        KLog.d("当前的年月日：" ,mCurrentYear_Month_Day);

        mDownBean =  MyApplication.getDbManager().queryDownBean();

        if(null == mDownBean){//项目第一次启动
            DownBean downBean = new DownBean();
            downBean.setCount(0);
            downBean.setDate(mCurrentYear_Month_Day);
            MyApplication.getDbManager().insertDownBean(downBean);
            return  0;
        }else{
            mHistoryYear_Month_Day = mDownBean.getDate();
            KLog.d("历史的年月日：",mHistoryYear_Month_Day);

            if(!TextUtils.isEmpty(mHistoryYear_Month_Day)
                    && mCurrentYear_Month_Day.compareTo(mHistoryYear_Month_Day) > 0){//第二天 重置下次次数
                mDownBean.setCount(0);
                mDownBean.setDate(mCurrentYear_Month_Day);
                MyApplication.getDbManager().updateDownBean(mDownBean);
                return 0;

            }else{//当天
                count = mDownBean.getCount();//获取当日的下载免费次数
            }
        }

        return count;

    }

    //查询并修改
    protected void queryAndUpdate(){
        DownBean downBean = MyApplication.getDbManager().queryDownBean();
        downBean.setCount(count + 1);
        MyApplication.getDbManager().updateDownBean(downBean);
    }


    /** ---------------- 查询 用户点击数逻辑  ------------------  */
    StatisticsBean mStatisticsBean;
    boolean isClick ;

    //获取是否下载
    protected boolean isAdUserClick(String adId){
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mDate = new Date(System.currentTimeMillis());
        mCurrentYear_Month_Day = mSimpleDateFormat.format(mDate);
        KLog.d("当前的年月日：" ,mCurrentYear_Month_Day);

        mStatisticsBean =  MyApplication.getDbManager().queryStatisticsBean(adId);

        if(null == mStatisticsBean){//项目第一次启动
            StatisticsBean statisticsBean = new StatisticsBean();
            statisticsBean.setAd_id(adId);
            statisticsBean.setIs_clicked(false);
            statisticsBean.setDate(mCurrentYear_Month_Day);
            MyApplication.getDbManager().insertStatisticsBean(statisticsBean);
            return false;
        }else{
            mHistoryYear_Month_Day = mStatisticsBean.getDate();
            KLog.d("历史的年月日：",mHistoryYear_Month_Day);

            if(!TextUtils.isEmpty(mHistoryYear_Month_Day)
                    && mCurrentYear_Month_Day.compareTo(mHistoryYear_Month_Day) > 0){//第二天 重置下次次数
                mStatisticsBean.setAd_id(adId);
                mStatisticsBean.setIs_clicked(false);
                mStatisticsBean.setDate(mCurrentYear_Month_Day);
                MyApplication.getDbManager().updateStatisticsBean(mStatisticsBean);
                return false;
            }else{//当天
                isClick = mStatisticsBean.isIs_clicked();
            }
        }

        return isClick;

    }


    //查询并修改
    protected void queryAndUpdate_Click(String adId){
        StatisticsBean statisticsBean = MyApplication.getDbManager().queryStatisticsBean(adId);
        statisticsBean.setIs_clicked(true);
        MyApplication.getDbManager().updateStatisticsBean(statisticsBean);
    }



    /** =================== 广告统计  开始 =================== */

    protected void adClickStatistics(String adId) {
        HashMap<String,String> maps = new HashMap<>();
        maps.put("adId",adId);
        MobclickAgent.onEvent(mContext,AppConstant.AdClickEvent,maps);//上传广告点击数
        boolean isAdClick = isAdUserClick(adId);
        if(!isAdClick){
            MobclickAgent.onEvent(mContext,AppConstant.AdUserClickCount,maps);//上传用户数
            queryAndUpdate_Click(adId);//更新
            maps.clear();
            return;
        }
        maps.clear();
    }
    /** =================== 广告统计  结束 =================== */


    /** 检测是否安装微信 */
    public static boolean isWxInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }



}
