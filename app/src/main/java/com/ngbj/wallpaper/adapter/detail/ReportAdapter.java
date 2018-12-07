package com.ngbj.wallpaper.adapter.detail;



import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;

import java.util.List;


public class ReportAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public ReportAdapter(List<String> data) {
        super(R.layout.report_tag_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.report_name,  item);

    }
}