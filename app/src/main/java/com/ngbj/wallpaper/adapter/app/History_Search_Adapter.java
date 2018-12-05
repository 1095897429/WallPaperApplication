package com.ngbj.wallpaper.adapter.app;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;

import java.util.List;


public class History_Search_Adapter extends BaseQuickAdapter<HistoryBean,BaseViewHolder> {

    public History_Search_Adapter(List<HistoryBean> data) {
        super(R.layout.history_search_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryBean item) {
        helper.setText(R.id.history_name,  item.getHistoryName());
        //设置点击
        helper.addOnClickListener(R.id.history_delete);

    }
}