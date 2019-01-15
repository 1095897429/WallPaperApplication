package com.ngbj.wallpaper.bean.entityBean;


import android.os.Parcel;
import android.os.Parcelable;

public class UploadTokenBean implements Parcelable {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
    }

    public UploadTokenBean() {
    }

    protected UploadTokenBean(Parcel in) {
        this.token = in.readString();
    }

    public static final Parcelable.Creator<UploadTokenBean> CREATOR = new Parcelable.Creator<UploadTokenBean>() {
        @Override
        public UploadTokenBean createFromParcel(Parcel source) {
            return new UploadTokenBean(source);
        }

        @Override
        public UploadTokenBean[] newArray(int size) {
            return new UploadTokenBean[size];
        }
    };
}
