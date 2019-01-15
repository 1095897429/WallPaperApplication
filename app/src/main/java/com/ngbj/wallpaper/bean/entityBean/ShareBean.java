package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 分享的信息
 */
public class ShareBean implements Parcelable {
    private int resId;
    private String name;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShareBean(int resId, String name) {
        this.resId = resId;
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resId);
        dest.writeString(this.name);
    }

    protected ShareBean(Parcel in) {
        this.resId = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ShareBean> CREATOR = new Parcelable.Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
