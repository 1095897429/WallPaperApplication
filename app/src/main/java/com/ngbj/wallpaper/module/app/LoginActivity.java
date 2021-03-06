package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.activity.LoginSuccessEvent;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.utils.common.RegexUtils;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class LoginActivity extends BaseActivity<LoginPresenter>
            implements LoginContract.View{

    @BindView(R.id.code)
    EditText code;

    @BindView(R.id.phone)
    EditText phone;

    String phoneNum;
    String codeNum;


    @BindView(R.id.get_done)
    TextView getDone;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initData() {
        mCountDownTimer = new CountDownTimer(60 * 1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getDone.setText(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                isTimeOver = true;
                getDone.setText("重新获取验证码");
            }
        };
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter();
    }




    @OnClick(R.id.safe_txt)
    public void SafeTxt(){
        startActivity(new Intent(this,SafeActivity.class));
    }




    @OnClick(R.id.login_delete)
    public void LoginDelete(){
       finish();
    }



    @OnClick(R.id.get_done)
    public void GetDone(){
        phoneNum = phone.getText().toString().trim();
        if(checkPhoneNum() && isTimeOver){
            mCountDownTimer.start();
            isTimeOver = false;
            mPresenter.getVerCodeData(phoneNum);
        }

    }

    @OnClick(R.id.login)
    public void Login(){
        phoneNum = phone.getText().toString().trim();
        codeNum = code.getText().toString().trim();
        if(checkPhoneNum() && checkVertyNum())
          mPresenter.getLoginData(phoneNum,codeNum);
    }




    private boolean checkPhoneNum(){
        if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            KLog.d("请输入手机号码");
            return false;
        }
        if(!RegexUtils.isMobileExact(phoneNum)){
            Toast.makeText(this,"输入的手机号码格式不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean checkVertyNum(){
        if(TextUtils.isEmpty(codeNum)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(codeNum.length() != 4){
            Toast.makeText(this,"输入的验证码位数不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void showVerCodeData() {
        Toast.makeText(this,"发送成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginData(LoginBean loginBean) {
        KLog.d("access_token: " + loginBean.getAccess_token());
        MyApplication.getDbManager().insertLoginBean(loginBean);
//        HomeActivity.openActivity(this);

        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
        //登录成功后发送的事件
        EventBus.getDefault().post(new LoginSuccessEvent(loginBean));
    }




    /** -------- 第三方登录 ------- */

    private String plat;//平台

    @OnClick(R.id.tab1)
    public void LoginQQ(){
        KLog.d("qq");
        if (isQQInstall(this)) {
            authorization(SHARE_MEDIA.QQ);
            plat = "QQ";
        } else {
            ToastHelper.customToastView(this, "请先安装QQ客户端");
            return;
        }
    }

    @OnClick(R.id.tab2)
    public void LoginWeiXin(){
        KLog.d("weixin");
        if (isWxInstall(this)) {
            authorization(SHARE_MEDIA.WEIXIN);
            plat = "WEIXIN";
        } else {
            ToastHelper.customToastView(this, "请先安装微信客户端");
            return;
        }

    }


    @OnClick(R.id.tab3)
    public void LoginWeiBo(){
        KLog.d("weibo");
        if (isWBInstall(this)) {
            authorization(SHARE_MEDIA.SINA);
            plat = "SINA";
        } else {
            ToastHelper.customToastView(this, "请先安装微博客户端");
            return;
        }
    }

    public static boolean isQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isWBInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.sina.weibo")) {
                    return true;
                }
            }
        }
        return false;
    }

    //授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);

        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                KLog.d("tag", "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                KLog.d("tag", "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");
                String city = map.get("city");
                String province = map.get("province");

                Map<String,Object> paraMap = new HashMap<>();

                paraMap.put("plat",plat);
                paraMap.put("openid",openid);
                paraMap.put("nickname",name);
                paraMap.put("sex",gender);
                paraMap.put("city",city);
                paraMap.put("province",province);
                paraMap.put("headimgurl",iconurl);
                paraMap.put("unionid",unionid);

                mPresenter.getThridData(paraMap);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                KLog.d("tag",  "授权失败 " + throwable.getMessage());
                ToastHelper.customToastView(LoginActivity.this,throwable.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastHelper.customToastView(LoginActivity.this,"授权取消");
                KLog.d("tag",  "onCancel " + "授权取消");
            }
        });
    }


    @Override
    public void showThridData(LoginBean thirdBean) {
        //跳转到普通登录，保存一些变量
        showLoginData(thirdBean);
        KLog.d("登录成功后的昵称： " + thirdBean.getNickname());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /** 倒计时  */
    private CountDownTimer mCountDownTimer;
    private boolean isTimeOver = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();//防止内存泄漏

        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
