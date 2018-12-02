package com.ngbj.wallpaper.adapter.index;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.bean.AdBean;

import java.util.List;

public class IndexRecommandAdapter extends BaseQuickAdapter<AdBean,BaseViewHolder> {

    public IndexRecommandAdapter(int layoutResId, @Nullable List<AdBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdBean item) {

    }
}
