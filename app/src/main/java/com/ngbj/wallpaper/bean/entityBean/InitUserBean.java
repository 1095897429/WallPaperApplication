package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 用户信息初始化
 */
public class InitUserBean implements Parcelable {
    private String latest_version;
    private String download_url;

    public String getLatest_version() {
        return latest_version;
    }

    public void setLatest_version(String latest_version) {
        this.latest_version = latest_version;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latest_version);
        dest.writeString(this.download_url);
    }

    public InitUserBean() {
    }

    protected InitUserBean(Parcel in) {
        this.latest_version = in.readString();
        this.download_url = in.readString();
    }

    public static final Parcelable.Creator<InitUserBean> CREATOR = new Parcelable.Creator<InitUserBean>() {
        @Override
        public InitUserBean createFromParcel(Parcel source) {
            return new InitUserBean(source);
        }

        @Override
        public InitUserBean[] newArray(int size) {
            return new InitUserBean[size];
        }
    };
}
