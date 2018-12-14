package com.ngbj.wallpaper.bean.entityBean;


public class InterestBean {

    private String id;//感兴趣分类id
    private String img_url;
    private String name;
    private String is_interested;//用户是否感兴趣 0否 1是
    private boolean isSelect;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getIs_interested() {
        return is_interested;
    }

    public void setIs_interested(String is_interested) {
        this.is_interested = is_interested;
    }

    public InterestBean(String name, String is_interested, boolean isSelect) {
        this.name = name;
        this.is_interested = is_interested;
        this.isSelect = isSelect;
    }
}
