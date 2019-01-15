package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

//后台返回的数据
public class AllData implements Parcelable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
    }

    public AllData() {
    }

    protected AllData(Parcel in) {
        this.data = in.readString();
    }

    public static final Parcelable.Creator<AllData> CREATOR = new Parcelable.Creator<AllData>() {
        @Override
        public AllData createFromParcel(Parcel source) {
            return new AllData(source);
        }

        @Override
        public AllData[] newArray(int size) {
            return new AllData[size];
        }
    };
}
