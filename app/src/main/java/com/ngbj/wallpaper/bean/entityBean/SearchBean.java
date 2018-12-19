package com.ngbj.wallpaper.bean.entityBean;

import java.util.List;

public class SearchBean {


    private List<IndexBean.HotSearch> hotSearch;
    private List<AdBean> ad;

    public List<IndexBean.HotSearch> getHotSearch() {
        return hotSearch;
    }

    public void setHotSearch(List<IndexBean.HotSearch> hotSearch) {
        this.hotSearch = hotSearch;
    }

    public List<AdBean> getAd() {
        return ad;
    }

    public void setAd(List<AdBean> ad) {
        this.ad = ad;
    }
}
