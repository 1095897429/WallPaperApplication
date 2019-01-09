package com.ngbj.wallpaper.module.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.mvp.presenter.app.SplashPresenter;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;


/***
 * 闪屏界面
 * 1.新增权限访问 -- 如果拒绝，传递默认值
 */
public class SplashActivity extends BaseActivity<SplashPresenter>
                implements SplashContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else {
            initLocationOption();
        }


        //解决下载完成后点击 安装还是完成的bug
        if(!isTaskRoot()){
            finish();
            return;
        }

    }


    //权限请求的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initLocationOption();
                }else {
//                    ToastHelper.customToastView(this,"地理位置权限拒绝，请先开启权限");
                    mPresenter.initUserInfo( mProvince, mCity);
                }

                break;
            default:
                break;
        }
    }


    @Override
    protected void initData() {



    }




    @Override
    public void showInitUserInfo(InitUserBean initUserBean) {
        KLog.d("实体:" + initUserBean.getDownload_url());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFristCome = (boolean) SPHelper.get(SplashActivity.this,AppConstant.ISFRISTCOME,true);

//                InterestActivity.openActivity(SplashActivity.this);

               if(isFristCome){
                   //不能用静态方法，导致内存泄漏
                   Intent intent = new Intent(mContext, InterestActivity.class);
                   mContext.startActivity(intent);

                }else{
                   Intent intent = new Intent(mContext, HomeActivity.class);
                   mContext.startActivity(intent);
               }

               finish();
            }
        },1000);

    }

    /** ------------- 定位 -----------------*/
    LocationClient locationClient;
    String mProvince = "安徽省";
    String mCity = "阜阳市";

    /** 初始化定位参数配置 */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
                locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
                LocationClientOption locationOption = new LocationClientOption();
                MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
                locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
                locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
                locationOption.setCoorType("gcj02");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
                locationOption.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
                locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
                locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
                locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
                locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
                locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
                locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
                locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
                locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
                locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
                locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
                locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
                locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //关联
                locationClient.setLocOption(locationOption);
        //开始定位
                locationClient.start();
    }

    /** 实现定位回调 */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();
            String city = location.getCity();//市
            String province = location.getProvince();//省

            if(!TextUtils.isEmpty(province)){
                mProvince = province;
                mCity = city;
                Log.d("tag",mProvince + mCity);
            }

            mPresenter.initUserInfo( mProvince, mCity);


            locationClient.stop();

        }
    }


}
