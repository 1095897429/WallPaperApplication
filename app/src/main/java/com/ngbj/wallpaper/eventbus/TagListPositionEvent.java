package com.ngbj.wallpaper.eventbus;

import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;

import java.util.Map;

/***
 * 上传 tag 发送的事件
 */
public class TagListPositionEvent {
    private Map<Integer,UploadTagBean> mMap;

    public TagListPositionEvent(Map<Integer, UploadTagBean> map) {
        mMap = map;
    }

    public Map<Integer, UploadTagBean> getMap() {
        return mMap;
    }
}
