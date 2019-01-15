package com.ngbj.wallpaper.mvp.presenter.app;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.mvp.contract.app.ReleaseContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class ReleasePresenter extends RxPresenter<ReleaseContract.View>
                implements ReleaseContract.Presenter<ReleaseContract.View> {



    @Override
    public void getInterestData() {

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .categoryList(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());

                            Type type = new TypeToken<List<InterestBean>>() {}.getType();
                            List<InterestBean> interestBeanList = gson.fromJson(result, type);
                            mView.showInterestData(interestBeanList);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .categoryList(OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<InterestBean>(mView) {
//                    @Override
//                    public void onSuccess(List<InterestBean> interestBeanList) {
//                        mView.showInterestData(interestBeanList);
//                    }
//                }));
    }

    @Override
    public void getUploadToken() {

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .getUploadToken(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            UploadTokenBean uploadTokenBean = gson.fromJson(result,UploadTokenBean.class);
                            mView.shwoUploadToken(uploadTokenBean);
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .getUploadToken(OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<UploadTokenBean>(mView) {
//                    @Override
//                    public void onSuccess(UploadTokenBean uploadTokenBean) {
//                        mView.shwoUploadToken(uploadTokenBean);
//                    }
//                }));

    }

    @Override
    public void uploadWallpaper(String accessToken, Map<String, Object> map) {

        RequestBody requestBody = OkHttpHelper.getRequestBody(map);

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .uploadWallpaper(accessToken,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        if(response.getCode() == 200){
                            mView.showUploadWallpaper();
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .uploadWallpaper(accessToken,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String string) {
//                        KLog.d("result: " + string);
//                        mView.showUploadWallpaper();
//                    }
//                }));
    }
}
