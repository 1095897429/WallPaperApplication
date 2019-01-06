package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
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

        addSubscribe(RetrofitHelper.getApiService()
                .thirdPlatLogin(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<LoginBean>(mView) {
                    @Override
                    public void onSuccess(LoginBean loginBean) {
                        mView.showThridData(loginBean);
                    }
                }));
    }



    //{"success":true,"code":200,"data":"发送成功","message":"OK"}
    @SuppressLint("CheckResult")
    @Override
    public void getVerCodeData(String phone) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .sendMobileCode(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody body) {

                        String jsongString ;
                        try {
                            jsongString = body.string();
                            JSONObject jsonObject =  new JSONObject(jsongString);
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("message");
                            if(200 == code){
                                mView.showVerCodeData();
                            }else
                                Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }


    //{"success":false,"code":422,"data":"","message":"验证码过期"} -- 解析错误

    @Override
    public void getLoginData(String phone,String code) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        hashMap.put("code",code);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .loginPhone(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody body) {
                        String jsongString ;
                        try {
                            jsongString = body.string();
                            JSONObject jsonObject =  new JSONObject(jsongString);
                            int code = jsonObject.optInt("code");
                            String msg = jsonObject.optString("message");
                            if(200 == code){
                                JSONObject data = jsonObject.optJSONObject("data");
                                Gson gson = new Gson();
                                LoginBean loginBean = gson.fromJson(data.toString(),LoginBean.class);
                                mView.showLoginData(loginBean);
                            }else
                                Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }));
    }



}
