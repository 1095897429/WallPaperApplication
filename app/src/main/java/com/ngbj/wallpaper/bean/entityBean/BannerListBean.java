package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class BannerListBean implements Parcelable {
    private BannerDetailBean info;//banner信息
    private List<AdBean> list;//壁纸列表数据

    public BannerDetailBean getInfo() {
        return info;
    }

    public void setInfo(BannerDetailBean info) {
        this.info = info;
    }

    public List<AdBean> getList() {
        return list;
    }

    public void setList(List<AdBean> list) {
        this.list = list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.info, flags);
        dest.writeTypedList(this.list);
    }

    public BannerListBean() {
    }

    protected BannerListBean(Parcel in) {
        this.info = in.readParcelable(BannerDetailBean.class.getClassLoader());
        this.list = in.createTypedArrayList(AdBean.CREATOR);
    }

    public static final Parcelable.Creator<BannerListBean> CREATOR = new Parcelable.Creator<BannerListBean>() {
        @Override
        public BannerListBean createFromParcel(Parcel source) {
            return new BannerListBean(source);
        }

        @Override
        public BannerListBean[] newArray(int size) {
            return new BannerListBean[size];
        }
    };
}
