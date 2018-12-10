package com.ngbj.wallpaper.adapter.my;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;

import java.util.List;


public class HeadUploadAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public HeadUploadAdapter(List<String> data) {
        super(R.layout.head_tag_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.head_upload_name,  item);

    }
}