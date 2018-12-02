package com.ngbj.wallpaper;

import android.content.Context;
import android.os.Bundle;

import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.utils.encry.AesUtils;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    String result ;
    String  secretKey= AesUtils.SECRETKEY;
    String   iv = AesUtils.IV;
    String string;
    String dectry;

    @OnClick(R.id.enctry)
    public void enctry(){
        string = "{\"did\":\"123456\",\"device_type\":\"周亮\",\"from_plat\":\"dgfgfghgfhdgfhgh\"}";
        result = AesUtils.encrypt(string,secretKey,iv);
        KLog.d("result =  " +  result);
    }


    @OnClick(R.id.dectry)
    public void dectry(){
        result = "00ROK9UJm2iXMZMBa7gbJblZOqJcC3mEdK7yRGao5VvpLIS+TG+JjV6PDU5WQxzsLkuWswVHZP7TZeOoftGhRJux9a1zigBKO/9UxX/bTok=";
        dectry =  AesUtils.decrypt(result,secretKey,iv);
        KLog.d("dectry =  " +  dectry);
        KLog.d("width ,"+ dip2px(this,81) );
        KLog.d("height ,"+ dip2px(this,108) );
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
