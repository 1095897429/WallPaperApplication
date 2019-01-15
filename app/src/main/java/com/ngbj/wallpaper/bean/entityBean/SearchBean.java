package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SearchBean implements Parcelable {


    private List<IndexBean.HotSearch> hotSearch;
    private List<AdBean> ad;

    public List<IndexBean.HotSearch> getHotSearch() {
        return hotSearch;
    }

    public void setHotSearch(List<IndexBean.HotSearch> hotSearch) {
        this.hotSearch = hotSearch;
    }

    public List<AdBean> getAd() {
        return ad;
    }

    public void setAd(List<AdBean> ad) {
        this.ad = ad;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.hotSearch);
        dest.writeTypedList(this.ad);
    }

    public SearchBean() {
    }

    protected SearchBean(Parcel in) {
        this.hotSearch = in.createTypedArrayList(IndexBean.HotSearch.CREATOR);
        this.ad = in.createTypedArrayList(AdBean.CREATOR);
    }

    public static final Parcelable.Creator<SearchBean> CREATOR = new Parcelable.Creator<SearchBean>() {
        @Override
        public SearchBean createFromParcel(Parcel source) {
            return new SearchBean(source);
        }

        @Override
        public SearchBean[] newArray(int size) {
            return new SearchBean[size];
        }
    };
}
