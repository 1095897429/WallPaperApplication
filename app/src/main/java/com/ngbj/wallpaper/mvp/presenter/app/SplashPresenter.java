package com.ngbj.wallpaper.mvp.presenter.app;

import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.mvp.contract.app.SplashContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class SplashPresenter extends RxPresenter<SplashContract.View>
                implements SplashContract.Presenter<SplashContract.View> {


    @Override
    public void initUserInfo(String province,String city) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("province",province);
        hashMap.put("city",city);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .initUserInfo(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            InitUserBean initUserBean = gson.fromJson(result,InitUserBean.class);
                            mView.showInitUserInfo(initUserBean);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//
//        addSubscribe(RetrofitHelper.getApiService()
//                .initUserInfo(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<InitUserBean>(mView) {
//                    @Override
//                    public void onSuccess(InitUserBean initUserBean1) {
//                        mView.showInitUserInfo(initUserBean1);
//                    }
//                }));

    }
}
