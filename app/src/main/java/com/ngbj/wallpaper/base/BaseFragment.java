package com.ngbj.wallpaper.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.StatisticsBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.SplashActivity;
import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 备注：基类的Fragment
 *       1.懒加载 -- 在状态可见 + 控件初始化完成（看下面的setUserVisibleHint方法）
 */
public abstract class BaseFragment<T extends BaseContract.BasePresenter>
        extends RxFragment implements BaseContract.BaseView {

    protected T mPresenter;
    protected View mRootView;//加载的View
    protected Context mContext;//上下文
    private Unbinder unbinder;

    protected boolean isVisible;//fragment是否可见
    protected boolean isPrepared;//控件初始化完成



    /** 简介1：当fragment 结合 viewpager使用时候 这个方法会调用*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){ // 相当于onResume()方法
            isVisible = true;
            onVisible();
        }else{    // 相当于onpause()方法
            isVisible = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            Intent intent= new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView){
          mRootView = LayoutInflater.from(mContext).inflate(getLayoutId(),container,false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    /** P 绑定 V  */
    protected void initInject() {
        if(null != mPresenter){
            mPresenter.attachView(this);
        }
    }

    /** onDestroyView中进行解绑操作 */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(null != mPresenter)mPresenter.detachView();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    /** 初始化控件方法 */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVidget();
        initPresenter();
        initInject();
        initData();
        finishCreateView();
        initEvent();
    }



    /** Presenter 初始化方法 */
    protected void initPresenter() {}

    protected  void initData(){}

    protected void initEvent() {}


    /** 当状态和控件都可见时才加载数据 */
    protected void finishCreateView() {
        isPrepared = true;
        lazyLoad();
    }

    /** 对各种控件进行设置、适配、填充数据 */
    protected void initVidget() {}

    protected abstract int getLayoutId();

    private void onVisible() {
        lazyLoad();
    }

    /**  懒加载方法 需要状态可见 + 控件初始化完成 */
    private void lazyLoad() {
        if(!isVisible || !isPrepared ) return;
        lazyLoadData();
        isPrepared = false;
    }

    /**  加载数据 */
    protected void lazyLoadData() {}

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }


    protected List<WallpagerBean> transformDataToWallpaper(String fromWhere,List<MulAdBean> recommendList) {
        WallpagerBean wallpagerBean;
        AdBean adBean ;
        ApiAdBean apiAdBean;
        List<WallpagerBean> tempList = new ArrayList<>();
        for (MulAdBean bean: recommendList) {
            if(bean.getItemType() == MulAdBean.TYPE_ONE){
                adBean = bean.adBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(adBean.getType());
                wallpagerBean.setFromWhere(fromWhere);
                wallpagerBean.setIs_collected(adBean.getIs_collected());
                wallpagerBean.setMovie_url(adBean.getMovie_url());
                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                wallpagerBean.setImg_url(adBean.getImg_url());

                if(adBean.getType().equals(AppConstant.COMMON_WP)){
                    wallpagerBean.setWallpager_id(adBean.getId());
                }else{
                    wallpagerBean.setWallpager_id(adBean.getAd_id());
                    wallpagerBean.setLink(adBean.getLink());//TODO 新增的
                }

                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setFromWhere(fromWhere);
                wallpagerBean.setWallpager_id(apiAdBean.getAd_id());
                wallpagerBean.setType(apiAdBean.getType());
                wallpagerBean.setImg_url(apiAdBean.getImgUrl());
                wallpagerBean.setLink(apiAdBean.getLink());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }


    /** ---------------- 公共方法  ------------------  */


    protected List<WallpagerBean> transformDataToWallpaper(List<MulAdBean> recommendList) {
        WallpagerBean wallpagerBean;
        AdBean adBean ;
        ApiAdBean apiAdBean;
        List<WallpagerBean> tempList = new ArrayList<>();
        for (MulAdBean bean: recommendList) {
            if(bean.getItemType() == MulAdBean.TYPE_ONE){
                adBean = bean.adBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(adBean.getType());
                wallpagerBean.setIs_collected(adBean.getIs_collected());
                wallpagerBean.setMovie_url(adBean.getMovie_url());
                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                wallpagerBean.setImg_url(adBean.getImg_url());

                if(adBean.getType().equals(AppConstant.COMMON_WP)){
                    wallpagerBean.setWallpager_id(adBean.getId());
                }else{
                    wallpagerBean.setWallpager_id(adBean.getAd_id());
                    wallpagerBean.setLink(adBean.getLink());//TODO 新增的
                }

                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setWallpager_id(apiAdBean.getAd_id());
                wallpagerBean.setType(apiAdBean.getType());
                wallpagerBean.setImg_url(apiAdBean.getImgUrl());
                wallpagerBean.setLink(apiAdBean.getLink());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }




    /** ---------------- 查询 用户点击数逻辑  ------------------  */
    StatisticsBean mStatisticsBean;
    SimpleDateFormat mSimpleDateFormat;
    Date mDate;
    String mCurrentYear_Month_Day;
    String mHistoryYear_Month_Day;
    boolean isClick ;

    //获取是否下载
    protected boolean isAdUserClick(String adId){
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mDate = new Date(System.currentTimeMillis());
        mCurrentYear_Month_Day = mSimpleDateFormat.format(mDate);
//        KLog.d("当前的年月日：" ,mCurrentYear_Month_Day);

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
//            KLog.d("历史的年月日：",mHistoryYear_Month_Day);

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



}
