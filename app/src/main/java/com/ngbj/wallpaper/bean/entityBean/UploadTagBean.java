package com.ngbj.wallpaper.bean.entityBean;


public class UploadTagBean {

    private String name;
    private int tagId;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public UploadTagBean(String name) {
        this.name = name;
    }
}
