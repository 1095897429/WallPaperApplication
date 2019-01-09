package com.ngbj.wallpaper.eventbus.activity;


import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

/***
 * 登录成功后，发送事件更新头像
 */
public class LoginSuccessEvent {

    private LoginBean mLoginBean;

    public LoginSuccessEvent(LoginBean loginBean) {
        mLoginBean = loginBean;
    }

    public LoginBean getLoginBean() {
        return mLoginBean;
    }
}
