package com.ngbj.wallpaper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.service.VideoLiveWallpaperService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 *  视频的地址:  http://phcsxfrh8.bkt.clouddn.com/icon_20181101173049
 *  缩略视频的地址: http://phcsxfrh8.bkt.clouddn.com/icon_20181101173049?imageView2/1/w/108/h/192
 */
public class PreviewDynamicPicActivity extends BaseActivity {

    private static final String TAG = "PreviewDynamicPicActivity";

    @BindView(R.id.id_cb_voice)
    CheckBox checkBox;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_dynamic);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // 静音
                            VideoLiveWallpaperService.voiceSilence(getApplicationContext());
                        } else {
                            VideoLiveWallpaperService.voiceNormal(getApplicationContext());
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }


    @OnClick(R.id.home_pic)
    public void home_pic() {
        VideoLiveWallpaperService.setToWallPaper(this);
    }

    @OnClick(R.id.dynamic_img)
    public void dynamic_img() {

    }





}
