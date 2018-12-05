package com.ngbj.wallpaper.bean.entityBean;


public class InterestBean {

    private String name;
    private String interestId;//感兴趣分类id
    private boolean isSelect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public InterestBean(String name, String interestId, boolean isSelect) {
        this.name = name;
        this.interestId = interestId;
        this.isSelect = isSelect;
    }
}
