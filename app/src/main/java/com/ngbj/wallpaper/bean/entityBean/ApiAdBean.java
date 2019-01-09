package com.ngbj.wallpaper.bean.entityBean;

/***
 * api的实体 总返回19个数据 -- position = 16 返回数据的倒数第二个数据
 */
public class ApiAdBean {

    private String name;
    private String imgUrl;
    private String type;

    private String thumb_img_url;//缩略图
    private String link;//TODO 新增链接

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
}
