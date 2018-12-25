package com.ngbj.wallpaper.utils.common;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/***
 * 统一的Toast
 * 1.采用统一的背景样式
 */
public class ToastHelper {

    private static Toast mToast;
    private View mView = null;

    /**自定义ToastView*/
    public static void customToastView(Context context,CharSequence msg){
        if(null == mToast){
            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);

        }else
            mToast.setText(msg);
        mToast.show();

    }


}
