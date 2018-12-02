package com.ngbj.wallpaper.adapter.category;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.AdBean;

import java.util.List;


public class Category_Top_Adapter extends BaseQuickAdapter<AdBean,BaseViewHolder> {

    public Category_Top_Adapter(List<AdBean> data) {
        super(R.layout.category_top_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdBean item) {
        helper.setText(R.id.title,  item.getTitle());

    }
}