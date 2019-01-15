package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * api的实体 总返回19个数据 -- position = 16 返回数据的倒数第二个数据
 */
public class ApiAdBean implements Parcelable {

    private String id;
    private String name;
    private String imgUrl;
    private String type;

    private String thumb_img_url;//缩略图
    private String link;//TODO 新增链接
    private String ad_id;//广告的id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb_img_url() {
        return thumb_img_url;
    }

    public void setThumb_img_url(String thumb_img_url) {
        this.thumb_img_url = thumb_img_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.imgUrl);
        dest.writeString(this.type);
        dest.writeString(this.thumb_img_url);
        dest.writeString(this.link);
        dest.writeString(this.ad_id);
    }

    public ApiAdBean() {
    }

    protected ApiAdBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imgUrl = in.readString();
        this.type = in.readString();
        this.thumb_img_url = in.readString();
        this.link = in.readString();
        this.ad_id = in.readString();
    }

    public static final Parcelable.Creator<ApiAdBean> CREATOR = new Parcelable.Creator<ApiAdBean>() {
        @Override
        public ApiAdBean createFromParcel(Parcel source) {
            return new ApiAdBean(source);
        }

        @Override
        public ApiAdBean[] newArray(int size) {
            return new ApiAdBean[size];
        }
    };
}
