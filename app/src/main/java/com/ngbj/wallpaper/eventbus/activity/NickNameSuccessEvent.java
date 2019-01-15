package com.ngbj.wallpaper.eventbus.activity;


import com.ngbj.wallpaper.bean.entityBean.LoginBean;

/***
 * 登录成功后，发送事件更新昵称
 */
public class NickNameSuccessEvent {

    private LoginBean mLoginBean;

    public NickNameSuccessEvent(LoginBean loginBean) {
        mLoginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return mLoginBean;
    }
}
