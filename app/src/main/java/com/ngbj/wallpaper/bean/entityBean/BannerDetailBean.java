package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * banner详情页
 */
public class BannerDetailBean implements Parcelable {

        private String id;
        private String title;
        private String img_url;
        private String comment;
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
        public String getImg_url() {
            return img_url;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
        public String getComment() {
            return comment;
        }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.img_url);
        dest.writeString(this.comment);
    }

    public BannerDetailBean() {
    }

    protected BannerDetailBean(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.img_url = in.readString();
        this.comment = in.readString();
    }

    public static final Parcelable.Creator<BannerDetailBean> CREATOR = new Parcelable.Creator<BannerDetailBean>() {
        @Override
        public BannerDetailBean createFromParcel(Parcel source) {
            return new BannerDetailBean(source);
        }

        @Override
        public BannerDetailBean[] newArray(int size) {
            return new BannerDetailBean[size];
        }
    };
}
