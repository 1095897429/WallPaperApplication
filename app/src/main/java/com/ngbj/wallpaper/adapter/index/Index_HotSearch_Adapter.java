package com.ngbj.wallpaper.adapter.index;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;

import java.util.List;


public class Index_HotSearch_Adapter extends BaseQuickAdapter<IndexBean.HotSearch,BaseViewHolder> {

    public Index_HotSearch_Adapter(List<IndexBean.HotSearch> data) {
        super(R.layout.index_hot_search_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexBean.HotSearch item) {
        helper.setText(R.id.title,  item.getTitle());

    }
}