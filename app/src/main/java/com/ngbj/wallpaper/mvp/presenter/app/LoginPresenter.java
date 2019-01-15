package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class LoginPresenter extends RxPresenter<LoginContract.View>
                implements LoginContract.Presenter<LoginContract.View> {

    /** 第三方登录 */
    @Override
    public void getThridData(Map<String, Object> map) {

        RequestBody requestBody = OkHttpHelper.getRequestBody(map);



        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .thirdPlatLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            LoginBean loginBean = gson.fromJson(result,LoginBean.class);
                            mView.showThridData(loginBean);
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .thirdPlatLogin(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<LoginBean>(mView) {
//                    @Override
//                    public void onSuccess(LoginBean loginBean) {
//                        mView.showThridData(loginBean);
//                    }
//                }));
    }



    //{"success":true,"code":200,"data":"发送成功","message":"OK"}
    @SuppressLint("CheckResult")
    @Override
    public void getVerCodeData(String phone) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .sendMobileCode(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            mView.showVerCodeData();
                        }else{
//                            mView.showError(response.getMessage());
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .sendMobileCode(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new ResponseSubscriber<ResponseBody>() {
//                    @Override
//                    public void onSuccess(ResponseBody body) {
//
//                        String jsongString ;
//                        try {
//                            jsongString = body.string();
//                            JSONObject jsonObject =  new JSONObject(jsongString);
//                            int code = jsonObject.optInt("code");
//                            String msg = jsonObject.optString("message");
//                            if(200 == code){
//                                mView.showVerCodeData();
//                            }else
//                                Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }));
    }


    //{"success":false,"code":422,"data":"","message":"验证码过期"} -- 解析错误

    @Override
    public void getLoginData(String phone,String code) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        hashMap.put("code",code);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .loginPhone(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            LoginBean loginBean = gson.fromJson(result,LoginBean.class);
                            mView.showLoginData(loginBean);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .loginPhone(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new ResponseSubscriber<ResponseBody>() {
//                    @Override
//                    public void onSuccess(ResponseBody body) {
//                        String jsongString ;
//                        try {
//                            jsongString = body.string();
//                            JSONObject jsonObject =  new JSONObject(jsongString);
//                            int code = jsonObject.optInt("code");
//                            String msg = jsonObject.optString("message");
//                            if(200 == code){
//                                JSONObject data = jsonObject.optJSONObject("data");
//                                Gson gson = new Gson();
//                                LoginBean loginBean = gson.fromJson(data.toString(),LoginBean.class);
//                                mView.showLoginData(loginBean);
//                            }else
//                                Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }));



    }



}
