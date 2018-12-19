package com.ngbj.wallpaper.bean.entityBean;

import java.util.List;


public class BannerListBean {
    private BannerDetailBean info;//banner信息
    private List<AdBean> list;//壁纸列表数据

    public BannerDetailBean getInfo() {
        return info;
    }

    public void setInfo(BannerDetailBean info) {
        this.info = info;
    }

    public List<AdBean> getList() {
        return list;
    }

    public void setList(List<AdBean> list) {
        this.list = list;
    }
}
