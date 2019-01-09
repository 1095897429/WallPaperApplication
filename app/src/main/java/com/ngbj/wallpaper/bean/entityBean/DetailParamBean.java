package com.ngbj.wallpaper.bean.entityBean;

import java.io.Serializable;

/***
 * 传入到明细界面 所需要的参数
 */
public class DetailParamBean implements Serializable {
    private  int mPosition;//当前选择的位置
    private int mPage;//当前选择的页数
    private String mCategory;//当前的分类类别
    private String mOrder;//当前的排序区分
    private String wallpagerId;//当前选择的壁纸ID
    private String fromWhere;//从哪里点击
    private String keyWord;//当前关键字
    private String navigation;//导航Id
    private String hotSearchTag;//热搜词
    private int searchType ;//搜索的类型 【关键词 热搜 导航】

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getHotSearchTag() {
        return hotSearchTag;
    }

    public void setHotSearchTag(String hotSearchTag) {
        this.hotSearchTag = hotSearchTag;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        mPage = page;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getOrder() {
        return mOrder;
    }

    public void setOrder(String order) {
        mOrder = order;
    }

    public String getWallpagerId() {
        return wallpagerId;
    }

    public void setWallpagerId(String wallpagerId) {
        this.wallpagerId = wallpagerId;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }
}
