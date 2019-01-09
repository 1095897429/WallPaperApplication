package com.ngbj.wallpaper.bean.entityBean;


public class UploadTagBean {

    private String name;
    private String tagId;//标签的ID
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


    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public UploadTagBean() {
    }

    public UploadTagBean(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }
}
