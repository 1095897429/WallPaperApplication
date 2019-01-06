package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * 1.Serializable 与 Parcelable
 * 2.规则  0 一般广告(在一个item) 1静态壁纸 2动态壁纸 3api广告
 * 3.正常的实体 -- 包含一些广告，壁纸等
 *
 */
public class AdBean implements Parcelable {

    //壁纸
    private String id;//共用
    private String type;//共用
    private String title;//共用
    private String movie_url;
    private String category_id;
    private String nickname;
    private String head_img;
    private String thumb_img_url;//缩略图
    private String is_collected;//0未收藏   1已收藏
    private List<Category> category;//分类信息
    //列表页广告
    private String link;
    private String img_url;
    private String begin_time;
    private String end_time;
    private String show_position;

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getShow_position() {
        return show_position;
    }

    public void setShow_position(String show_position) {
        this.show_position = show_position;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.title);
        dest.writeString(this.movie_url);
        dest.writeString(this.category_id);
        dest.writeString(this.nickname);
        dest.writeString(this.head_img);
        dest.writeString(this.thumb_img_url);
        dest.writeString(this.is_collected);
        dest.writeList(this.category);
        dest.writeString(this.link);
        dest.writeString(this.img_url);
        dest.writeString(this.begin_time);
        dest.writeString(this.end_time);
        dest.writeString(this.show_position);
    }

    protected AdBean(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.title = in.readString();
        this.movie_url = in.readString();
        this.category_id = in.readString();
        this.nickname = in.readString();
        this.head_img = in.readString();
        this.thumb_img_url = in.readString();
        this.is_collected = in.readString();
        this.category = new ArrayList<Category>();
        in.readList(this.category, Category.class.getClassLoader());
        this.link = in.readString();
        this.img_url = in.readString();
        this.begin_time = in.readString();
        this.end_time = in.readString();
        this.show_position = in.readString();
    }

    public static final Parcelable.Creator<AdBean> CREATOR = new Parcelable.Creator<AdBean>() {
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
