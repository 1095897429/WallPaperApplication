package com.ngbj.wallpaper.bean.entityBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/***
 * 下载的次数
 */
@Entity
public class DownBean {
    @Id
    private Long id;
    private String date;//当日时间
    private int count;//当日免费点击的次数



    @Generated(hash = 353897327)
    public DownBean(Long id, String date, int count) {
        this.id = id;
        this.date = date;
        this.count = count;
    }

    @Generated(hash = 2113458446)
    public DownBean() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
