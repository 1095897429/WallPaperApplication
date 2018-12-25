package com.ngbj.wallpaper.bean.entityBean;

/***
 * api的实体 总返回19个数据 -- position = 16 返回数据的倒数第二个数据
 */
public class ApiAdBean {

    private String name;
    private String imgUrl;
    private String type;

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
}
