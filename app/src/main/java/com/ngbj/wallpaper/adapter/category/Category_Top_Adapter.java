package com.ngbj.wallpaper.adapter.category;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;

import java.util.List;


public class Category_Top_Adapter extends BaseQuickAdapter<InterestBean,BaseViewHolder> {

    public Category_Top_Adapter(List<InterestBean> data) {
        super(R.layout.category_top_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterestBean item) {
        helper.setText(R.id.title,  item.getName());

    }
}