package com.ngbj.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.utils.widget.GlideRoundTransform;

/***
 * 广告下载弹出框
 */

public class AdShowDialog {

    private Context context;
    private Dialog dialog;
    private Display display;
    private ImageView mImageView;
    private TextView mTextView;
    private AdBean mAdBean;

    public AdShowDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AdShowDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ad_show, null);

        // 获取自定义Dialog布局中的控件
        mImageView =  view.findViewById(R.id.ad_pic);
        mTextView = view.findViewById(R.id.makedone);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.setContentView(view);

        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);

        return this;
    }

    public AdShowDialog  setAdBean(AdBean adBean){
        mAdBean = adBean;
        return this;
    }


    public AdShowDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public AdShowDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }



    public AdShowDialog setPositionButton(String text, final View.OnClickListener listener) {

        mTextView.setText(text);
        /** 只有独立的按钮 */
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });

        return this;
    }


    public AdShowDialog setImageViewListener( final View.OnClickListener listener) {

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dialog.dismiss();
            }
        });

        return this;
    }



    private void setLayout() {
        if(mAdBean != null){
            Glide.with(MyApplication.getInstance())
                    .load(mAdBean.getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.dialog_ad)
                    .centerCrop()
                    .crossFade()
                    .into(mImageView);
        }


    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }


}