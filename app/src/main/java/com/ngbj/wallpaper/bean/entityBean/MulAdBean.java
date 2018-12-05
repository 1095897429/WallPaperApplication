package com.ngbj.wallpaper.bean.entityBean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/***
 * 处理多布局的实体
 */
public class MulAdBean implements Serializable,MultiItemEntity {

    public static final int TYPE_ONE = 1;//布局样式1 -- 正常
    public static final int TYPE_TWO = 2;//布局样式2 -- ad
    public static final int ITEM_SPAN_SIZE = 1;//子item占1列数
    public static final int AD_SPAN_SIZE = 2;//AD占2列数
    public AdBean adBean;
    public ApiAdBean apiAdBean;

    public int itemType;
    public int spanSize;

    /** 正常Item的构造 */
    public MulAdBean(int itemType,int spanSize,AdBean adBean){
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.adBean = adBean;
    }


    /** ad的构造 */
    public MulAdBean(int itemType,int spanSize,ApiAdBean apiAdBean){
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.apiAdBean = apiAdBean;
    }

    /** 返回是哪种类型 */
    @Override
    public int getItemType() {
        return itemType;
    }

}
