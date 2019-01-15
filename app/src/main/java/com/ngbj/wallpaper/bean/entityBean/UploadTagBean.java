package com.ngbj.wallpaper.bean.entityBean;


import android.os.Parcel;
import android.os.Parcelable;

public class UploadTagBean implements Parcelable {

    private String name;
    private String tagId;//标签的ID
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public UploadTagBean() {
    }

    public UploadTagBean(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.tagId);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected UploadTagBean(Parcel in) {
        this.name = in.readString();
        this.tagId = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UploadTagBean> CREATOR = new Parcelable.Creator<UploadTagBean>() {
        @Override
        public UploadTagBean createFromParcel(Parcel source) {
            return new UploadTagBean(source);
        }

        @Override
        public UploadTagBean[] newArray(int size) {
            return new UploadTagBean[size];
        }
    };
}
