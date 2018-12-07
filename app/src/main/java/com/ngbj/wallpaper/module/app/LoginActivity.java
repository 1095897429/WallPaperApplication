package com.ngbj.wallpaper.module.app;

import android.widget.EditText;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.VerCodeBean;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
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
        mPresenter.getVerCodeData();
    }

    @OnClick(R.id.login)
    public void Login(){
        mPresenter.getLoginData();
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




    @Override
    public void showVerCodeData(VerCodeBean verCodeBean) {
        code.setText(verCodeBean.getCode() + "");
    }

    @Override
    public void showLoginData(LoginBean loginBean) {
        KLog.d("showLoginData");
    }
}
