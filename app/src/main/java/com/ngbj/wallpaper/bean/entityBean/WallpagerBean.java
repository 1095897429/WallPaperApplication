package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.ArrayList;
import java.util.List;


/**
 * 壁纸实体
 */
@Entity
public class WallpagerBean  {
    //壁纸
    @Id(autoincrement = true)
    private Long id;//共用
    private String type;//共用
    private String title;//共用
    private String movie_url;
    private String wallpager_id;
    private String nickname;//昵称
    private String head_img;//用户上传头像
    private String thumb_img_url;//缩略图
    private String is_collected;//0未收藏   1已收藏

    //由于greendao中用原始数据
    private String category_id;
    private String category_name;
    private String img_url;//图片地址

    @Generated(hash = 1221811270)
    public WallpagerBean(Long id, String type, String title, String movie_url,
            String wallpager_id, String nickname, String head_img,
            String thumb_img_url, String is_collected, String category_id,
            String category_name, String img_url) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.movie_url = movie_url;
        this.wallpager_id = wallpager_id;
        this.nickname = nickname;
        this.head_img = head_img;
        this.thumb_img_url = thumb_img_url;
        this.is_collected = is_collected;
        this.category_id = category_id;
        this.category_name = category_name;
        this.img_url = img_url;
    }

    @Generated(hash = 785783416)
    public WallpagerBean() {
    }

    public Long getId() {
        return id;
    }

    public String getWallpager_id() {
        return wallpager_id;
    }

    public void setWallpager_id(String wallpager_id) {
        this.wallpager_id = wallpager_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setId(Long id) {
        this.id = id;
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


}
