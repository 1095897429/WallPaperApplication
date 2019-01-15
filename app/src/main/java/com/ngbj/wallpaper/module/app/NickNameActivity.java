package com.ngbj.wallpaper.module.app;


import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.eventbus.activity.LoginSuccessEvent;
import com.ngbj.wallpaper.eventbus.activity.NickNameSuccessEvent;
import com.ngbj.wallpaper.mvp.contract.app.UserContract;
import com.ngbj.wallpaper.mvp.presenter.app.UserPresenter;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.widget.GlideCircleTransform;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


/***
 * 修改昵称
 */
public class NickNameActivity extends BaseActivity<UserPresenter>
            implements UserContract.View {


    @BindView(R.id.nickname_txt)
    EditText nickname_txt;

    LoginBean bean;


    @Override
    protected void initPresenter() {
        mPresenter = new UserPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nickname;
    }


    @Override
    protected void initData() {
        bean = MyApplication.getDbManager().queryLoginBean();
        nickname_txt.setText(bean.getNickname());
    }

    @OnClick(R.id.back)
    public void Back(){
        finish();
    }


    @OnClick(R.id.makesave)
    public void MakeSave(){

        String nickName  = nickname_txt.getText().toString().trim();
        if(TextUtils.isEmpty(nickName)){
            ToastHelper.customToastView(getApplicationContext(),"昵称不能为空");
            return;
        }

        try {
            KLog.d("昵称的长度：" + nickName.getBytes("GBK").length);
            if(nickName.getBytes("GBK").length > 10){
                Toast.makeText(this,"昵称的长度不能超过5个汉字",Toast.LENGTH_SHORT).show();
                return ;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        String accessToken = bean.getAccess_token();
        int gender = bean.getGender();
        mPresenter.getUploadUserData(accessToken,nickName,gender);
    }


    @Override
    public void showUploadUserData(LoginBean loginBean) {

        //更新实体
        LoginBean bean = MyApplication.getDbManager().queryLoginBean();
        bean.setNickname(loginBean.getNickname());
        MyApplication.getDbManager().updateLoginBean(bean);

        EventBus.getDefault().post(new NickNameSuccessEvent(loginBean));
        finish();
    }
}
