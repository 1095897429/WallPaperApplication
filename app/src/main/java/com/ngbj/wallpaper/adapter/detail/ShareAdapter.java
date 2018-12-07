package com.ngbj.wallpaper.adapter.detail;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;

import java.util.List;


public class ShareAdapter extends BaseQuickAdapter<ShareBean,BaseViewHolder> {

    public ShareAdapter(List<ShareBean> data) {
        super(R.layout.share_tag_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareBean item) {
        helper.setText(R.id.text,  item.getName());
        helper.setImageResource(R.id.imageView,item.getResId());

    }
}