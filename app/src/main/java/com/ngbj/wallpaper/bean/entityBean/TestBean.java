package com.ngbj.wallpaper.bean.entityBean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TestBean {

    @Id
    private Long id;

    private String url;

    @Generated(hash = 834929861)
    public TestBean(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    @Generated(hash = 2087637710)
    public TestBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
