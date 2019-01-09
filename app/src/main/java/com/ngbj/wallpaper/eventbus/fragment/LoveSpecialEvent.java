package com.ngbj.wallpaper.eventbus.fragment;


import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

/***
 * 明细界面 点击 love or unlove 反馈给数据来源的事件
 * 2018.12.26
 */
public class LoveSpecialEvent {

    private boolean isLove;// 0 不喜欢   1 ❤️
    private int position;//当前的位置
    private int page;//当前的页数
    private boolean isReset;//是否重新布局
    private List<MulAdBean> mMulAdBeanList;



    /** 喜好 */
    public LoveSpecialEvent(int position, boolean isLove) {
        this.isLove = isLove;
        this.position = position;
    }

    /** 全更新 */
    public LoveSpecialEvent(int position, int page, boolean isReset, List<MulAdBean> mulAdBeanList) {
        this.position = position;
        this.page = page;
        this.isReset = isReset;
        mMulAdBeanList = mulAdBeanList;
    }

    public int getPage() {
        return page;
    }

    public boolean isLove() {
        return isLove;
    }

    public List<MulAdBean> getMulAdBeanList() {
        return mMulAdBeanList;
    }


    public boolean isReset() {
        return isReset;
    }


    public int getPosition() {
        return position;
    }
}
