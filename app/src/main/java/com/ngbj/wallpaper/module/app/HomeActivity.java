package com.ngbj.wallpaper.module.app;



import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.LoadingDialog;
import com.ngbj.wallpaper.eventbus.LoveEvent;
import com.ngbj.wallpaper.eventbus.fragment.ChangeFragmentEvent;
import com.ngbj.wallpaper.module.fragment.CategoryFragment;
import com.ngbj.wallpaper.module.fragment.IndexFragment;
import com.ngbj.wallpaper.module.fragment.MyFragment;
import com.ngbj.wallpaper.mvp.contract.app.HomeContract;
import com.ngbj.wallpaper.mvp.presenter.app.HomePresenter;
import com.ngbj.wallpaper.service.MyJobService;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.sigmob.windad.WindAdOptions;
import com.sigmob.windad.WindAds;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeActivity extends BaseActivity<HomePresenter>
            implements HomeContract.View {

    @BindView(R.id.index_icon)
    ImageView indexIcon;

    @BindView(R.id.category_icon)
    ImageView categoryIcon;

    @BindView(R.id.my_icon)
    ImageView myIcon;


    IndexFragment indexFragment;
    CategoryFragment categoryFragment;
    MyFragment myFragment;
    Fragment currentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

        initIndexFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            openJobService();
        }
    }


    private void openJobService() {
        JobScheduler jobScheduler;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, MyJobService.class));  //指定哪个JobService执行操作
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING);  //非漫游网络状态
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
            builder.setRequiresCharging(false); // 未充电状态
            jobScheduler.schedule(builder.build());
        }
    }


    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
    }

    /** 默认的Fragment */
    private void initIndexFragment(){
        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        if(currentFragment == null)
            indexFragment = IndexFragment.getInstance();
        currentFragment = indexFragment;
        transaction.add(R.id.frameLayout,currentFragment);
        transaction.commit();
    }


    /** 切换Fragment */
    private void switchFragment(Fragment fragment){
        if(currentFragment != fragment){
            FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if(!fragment.isAdded()){
                transaction.add(R.id.frameLayout,fragment).commit();
            }else{
                transaction.show(fragment).commit();
            }
        }
    }

    /** 隐藏状态 */
    private void hideImageStatus(){
        indexIcon.setImageResource(R.mipmap.index_uncheck);
        categoryIcon.setImageResource(R.mipmap.category_uncheck);
        myIcon.setImageResource(R.mipmap.my_uncheck);
    }

    @OnClick({R.id.index,R.id.category,R.id.my})
    public void bottomClick(View layout){

        hideImageStatus();
        switch (layout.getId()){
            case R.id.index:
                if(null == indexFragment) indexFragment = IndexFragment.getInstance();
                indexIcon.setImageResource(R.mipmap.index_check);
                switchFragment(indexFragment);
                break;
            case R.id.category:
                if(null == categoryFragment) categoryFragment = CategoryFragment.getInstance();
                categoryIcon.setImageResource(R.mipmap.category_check);
                switchFragment(categoryFragment);
                break;
            case R.id.my:
                if(null == myFragment) myFragment = MyFragment.getInstance();
                myIcon.setImageResource(R.mipmap.my_check);
                switchFragment(myFragment);
                break;
        }
    }


    @Override
    public void showSearchHistory() {

    }


    /** --------- 退出逻辑  在2秒内快速点击2次，则退出app --------- */
    long currentTime;
    long mExitTime; //退出时的时间
    public void exit() {
        currentTime = System.currentTimeMillis();
        if ((currentTime - mExitTime) > 2000) {
            ToastHelper.customToastView(MyApplication.getInstance(),"再按一次退出");
            mExitTime = currentTime;
        } else {
            mPresenter.getSearchHistory();
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /** --------- 权限  --------- */

    //申请两个权限，录音和文件读写
    //1、首先声明一个数组permissions，将需要的权限都放在里面
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码


    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }else{
            //说明权限都已经通过，可以做你想做的事情去
        }
    }


    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            }else{
                //全部权限通过，可以进行下一步操作。。。

            }
        }

    }


    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "com.ngbj.wallpaper";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();

                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }




    /** =================== EventBus  开始 =================== */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
         }
        EventBus.getDefault().register(this);
    }


    /** 切换到分类界面 */
    @Subscribe
    public void onChangeFragmentEvent(ChangeFragmentEvent event){
        bottomClick(findViewById(R.id.category));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }





    /** =================== EventBus  结束 =================== */



    /** =================== 推送参数  =================== */

    private String type;
    private String param;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null ){
            type = intent.getExtras().getString("type");
            param = intent.getExtras().getString("param");

            if(AppConstant.APPHOME.equals(type)){//啥都不做
                KLog.d("啥都不做");
            }else if(AppConstant.APPBANNER.equals(type)){//banner -- id = 4

                Intent toIntent = new Intent(mContext, SpecialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bannerId",param);
                toIntent.putExtras(bundle);
                mContext.startActivity(toIntent);

            }else if(AppConstant.APPNAVICATION.equals(type)){//导航 -- id = 16

                Intent toIntent = new Intent(mContext,SearchActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt(AppConstant.FROMWHERE,AppConstant.FROMINDEX_NAVICATION);
                bundle.putString(AppConstant.NAVICATIONID,param);
                bundle.putString(AppConstant.HOTSEARCHTAG,"");
                toIntent.putExtras(bundle);
                mContext.startActivity(toIntent);

            }else if(AppConstant.APPKEYWORD.equals(type)){//搜索关键字

                Intent toIntent = new Intent(mContext,SearchActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt(AppConstant.FROMWHERE,AppConstant.FROMINDEX_SEACHER);
                bundle.putString(AppConstant.NAVICATIONID,"");
                bundle.putString(AppConstant.HOTSEARCHTAG,"");
                bundle.putString("keyword",param);
                toIntent.putExtras(bundle);
                mContext.startActivity(toIntent);

            }
        }

    }
}
