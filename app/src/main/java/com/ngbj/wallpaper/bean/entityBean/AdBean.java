package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * 1.Serializable 与 Parcelable
 * 2.规则  0 一般广告 1静态壁纸 2动态壁纸 3api广告
 * 3.正常的实体 -- 包含一些广告，壁纸等
 *
 */
public class AdBean implements Parcelable {

    private String id;
    private String title;
    private String movie_url;
    private String category_id;
    private String type;
    private String nickname;
    private String head_img;
    private String thumb_img_url;//缩略图
    private String is_collected;//0未收藏   1已收藏

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

    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }
    public String getMovie_url() {
        return movie_url;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
    public String getCategory_id() {
        return category_id;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
    public String getHead_img() {
        return head_img;
    }

    public void setThumb_img_url(String thumb_img_url) {
        this.thumb_img_url = thumb_img_url;
    }
    public String getThumb_img_url() {
        return thumb_img_url;
    }

    public String getIs_collected() {
        return is_collected;
    }

    public void setIs_collected(String is_collected) {
        this.is_collected = is_collected;
    }



    public AdBean(String title,String thumb_img_url) {
        this.title = title;
        this.thumb_img_url = thumb_img_url;
    }

    public AdBean(String type,String title,String thumb_img_url) {
        this.type = type;
        this.title = title;
        this.thumb_img_url = thumb_img_url;
    }

    public AdBean(String type,String is_collected,String title,String thumb_img_url) {
        this.type = type;
        this.is_collected = is_collected;
        this.title = title;
        this.thumb_img_url = thumb_img_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.movie_url);
        dest.writeString(this.category_id);
        dest.writeString(this.type);
        dest.writeString(this.nickname);
        dest.writeString(this.head_img);
        dest.writeString(this.thumb_img_url);
        dest.writeString(this.is_collected);
    }

    protected AdBean(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.movie_url = in.readString();
        this.category_id = in.readString();
        this.type = in.readString();
        this.nickname = in.readString();
        this.head_img = in.readString();
        this.thumb_img_url = in.readString();
        this.is_collected = in.readString();
    }

    public static final Creator<AdBean> CREATOR = new Creator<AdBean>() {
        @Override
        public AdBean createFromParcel(Parcel source) {
            return new AdBean(source);
        }

        @Override
        public AdBean[] newArray(int size) {
            return new AdBean[size];
        }
    };
}
