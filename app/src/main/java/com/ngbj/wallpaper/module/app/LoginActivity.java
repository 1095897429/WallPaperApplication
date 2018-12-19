package com.ngbj.wallpaper.module.app;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.utils.common.RegexUtils;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.socks.library.KLog;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter();
    }



    @OnClick(R.id.login_delete)
    public void LoginDelete(){
       finish();
    }

    @OnClick(R.id.get_done)
    public void GetDone(){
        phoneNum = phone.getText().toString().trim();
        if(checkPhoneNum())
            mPresenter.getVerCodeData(phoneNum);
    }

    @OnClick(R.id.login)
    public void Login(){
        codeNum = code.getText().toString().trim();
        if(checkVertyNum())
         mPresenter.getLoginData(phoneNum,codeNum);
    }


    @OnClick(R.id.tab1)
    public void LoginQQ(){
        KLog.d("qq");
    }

    @OnClick(R.id.tab2)
    public void LoginWeiXin(){
        KLog.d("weixin");
    }


    @OnClick(R.id.tab3)
    public void LoginWeiBo(){
        KLog.d("weibo");
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
            Toast.makeText(this,"输入的验证码数位不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void showVerCodeData() {

    }

    @Override
    public void showLoginData(LoginBean loginBean) {
        KLog.d("access_token: " + loginBean.getAccess_token());
        SPHelper.put(this,AppConstant.ACCESSTOKEN,loginBean.getAccess_token());//保存token
        startActivity(new Intent(this,HomeActivity.class));
    }
}
