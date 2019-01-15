package com.ngbj.wallpaper.eventbus.fragment;


/***
 * 分类中下方的子项
 */
public class LoveNewCategoryEvent {

    private boolean isLove;// 0 不喜欢   1 ❤️
    private int position;//当前的位置
    private String fromWhere;//修改哪里的来源



    /** 喜好 */
    public LoveNewCategoryEvent(String fromWhere,int position, boolean isLove) {
        this.fromWhere = fromWhere;
        this.isLove = isLove;
        this.position = position;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public boolean isLove() {
        return isLove;
    }


    public int getPosition() {
        return position;
    }

}
