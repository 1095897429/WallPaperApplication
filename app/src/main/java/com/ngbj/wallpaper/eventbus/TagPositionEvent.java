package com.ngbj.wallpaper.eventbus;

import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;

import java.util.List;
import java.util.Map;

/***
 * 上传 tag 发送的事件
 */
public class TagPositionEvent {
    private Map<Integer,UploadTagBean> mMap;

    public TagPositionEvent(Map<Integer, UploadTagBean> map) {
        mMap = map;
    }

    public Map<Integer, UploadTagBean> getMap() {
        return mMap;
    }
}
