package com.ngbj.wallpaper.eventbus;


import java.util.List;

/***
 * 上传 tag 发送的事件
 */
public class UploadTagEvent {
    private List<Integer> mPositions;

    public UploadTagEvent(List<Integer> mPositions){
        this.mPositions = mPositions;
    }

    public List<Integer> getPositions() {
        return mPositions;
    }
}
