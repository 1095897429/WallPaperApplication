package com.ngbj.wallpaper.eventbus.activity;


import com.ngbj.wallpaper.bean.entityBean.JpushExtras;


public class JpushEvent {

    private JpushExtras mJpushExtras;

    public JpushExtras getJpushExtras() {
        return mJpushExtras;
    }

    public JpushEvent(JpushExtras jpushExtras) {
        mJpushExtras = jpushExtras;
    }
}
