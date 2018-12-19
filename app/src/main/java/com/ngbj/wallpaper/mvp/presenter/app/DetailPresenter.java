package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.utils.common.AppHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class DetailPresenter extends RxPresenter<DetailContract.View>
                implements DetailContract.Presenter<DetailContract.View> {


    @SuppressLint("CheckResult")
    @Override
    public void getData(final String wallpaperId) {

        Gson gson = new Gson();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("fromPlat", "default");
        hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
        hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
        hashMap.put("deviceType", "android");
        hashMap.put("timestamp", System.currentTimeMillis() + "");
        hashMap.put("sign", "");
        String strEntity = gson.toJson(hashMap);
        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity);

        addSubscribe(RetrofitHelper.getApiService()
                .detail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(AdBean item) {
                        //TODO 通过id在数据库中拿到对应的Bean
                        WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpagerBean(wallpaperId);
                        wallpagerBean.setImg_url(item.getImg_url());
                        MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
                        mView.showData(item);
                    }
                }));
    }

    @Override
    public void getDynamicData() {
        //TODO 从数据库中获取 2018.12.14

       List<WallpagerBean> list =  MyApplication.getDbManager().queryWallpagerBeanList();
       if(!list.isEmpty()){
           mView.showDynamicData(list);
       }
    }


    public void test(){
        List<AdBean> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(new AdBean("1","奇葩说","http://pjb68wj3e.bkt.clouddn.com/icon_20181214145027"));
        }
//        list.add(new AdBean("2","奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        list.add(new AdBean("1","猫妖传","http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg"));
//        list.add(new AdBean("吐槽大会","http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg"));
//        list.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        list.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        list.add(new AdBean("猫妖传","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        list.add(new AdBean("吐槽大会","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        list.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        mView.showDynamicData(list);
    }

}
