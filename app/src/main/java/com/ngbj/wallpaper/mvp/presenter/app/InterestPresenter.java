package com.ngbj.wallpaper.mvp.presenter.app;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.mvp.contract.app.InterestContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class InterestPresenter extends RxPresenter<InterestContract.View>
                implements InterestContract.Presenter<InterestContract.View> {


    @Override
    public void getInterestData() {

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .categoryList(OkHttpHelper.getRequestBody(new HashMap<String, Object>()))
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
//                .categoryList(OkHttpHelper.getRequestBody(new HashMap<String, Object>()))
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
    public void writeInterestData(List<String> jsonListString) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("interest",jsonListString);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .interestCategory(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            mView.showWriteInterestData();
                        }else{
//                            mView.showError(response.getMessage());
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .interestCategory(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String string) {
//                        mView.showWriteInterestData(string);
//                    }
//                }));
    }


    public void test(){
        List<InterestBean> interestBeanList = new ArrayList<>();
        interestBeanList.add(new InterestBean("明星写真","1",false));
        interestBeanList.add(new InterestBean("体育竞技","2",false));
        interestBeanList.add(new InterestBean("浪漫爱情","3",false));
        interestBeanList.add(new InterestBean("清新可爱","4",false));
        interestBeanList.add(new InterestBean("明星写真","5",false));
        interestBeanList.add(new InterestBean("体育竞技","6",false));
        interestBeanList.add(new InterestBean("浪漫爱情","7",false));
        interestBeanList.add(new InterestBean("清新可爱","8",false));
        interestBeanList.add(new InterestBean("明星写真","9",false));
        interestBeanList.add(new InterestBean("体育竞技","10",false));
        interestBeanList.add(new InterestBean("浪漫爱情","11",false));
        interestBeanList.add(new InterestBean("清新可爱","12",false));
        interestBeanList.add(new InterestBean("明星写真","12",false));
        interestBeanList.add(new InterestBean("体育竞技","13",false));
        interestBeanList.add(new InterestBean("浪漫爱情","14",false));
        interestBeanList.add(new InterestBean("清新可爱","15",false));
        interestBeanList.add(new InterestBean("清新可爱","16",false));
        interestBeanList.add(new InterestBean("明星写真","17",false));
        interestBeanList.add(new InterestBean("体育竞技","18",false));
        interestBeanList.add(new InterestBean("浪漫爱情","19",false));
        interestBeanList.add(new InterestBean("清新可爱","20",false));
        interestBeanList.add(new InterestBean("明星写真","21",false));
        interestBeanList.add(new InterestBean("体育竞技","22",false));
        interestBeanList.add(new InterestBean("浪漫爱情","23",false));
        interestBeanList.add(new InterestBean("清新可爱","24",false));

        mView.showInterestData(interestBeanList);
    }
}
