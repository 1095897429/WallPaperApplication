package com.ngbj.wallpaper.bean.entityBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/***
 * 登录的信息
 */
@Entity
public class LoginBean implements Serializable {
    @Id
    private Long id;
    private String access_token;
    private int expire_time;//过期时间戳
    private String mobile;
    private String nickname;
    private int gender;//性别 0 -- 女
    private String head_img;


    @Generated(hash = 1331838894)
    public LoginBean(Long id, String access_token, int expire_time, String mobile,
            String nickname, int gender, String head_img) {
        this.id = id;
        this.access_token = access_token;
        this.expire_time = expire_time;
        this.mobile = mobile;
        this.nickname = nickname;
        this.gender = gender;
        this.head_img = head_img;
    }

    @Generated(hash = 1112702939)
    public LoginBean() {
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
