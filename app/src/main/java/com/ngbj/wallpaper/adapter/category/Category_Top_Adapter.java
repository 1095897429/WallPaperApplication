package com.ngbj.wallpaper.adapter.category;



import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;

import java.util.HashMap;
import java.util.List;


public class Category_Top_Adapter extends BaseQuickAdapter<InterestBean,BaseViewHolder> {

    public int mPositon;

    public Category_Top_Adapter(List<InterestBean> data,int positon) {
        super(R.layout.category_top_item,data);
        mPositon = positon;
    }


    @Override
    protected void convert(final BaseViewHolder holder, InterestBean item) {
        holder.setText(R.id.title,  item.getName());

        if(!TextUtils.isEmpty(item.getImg_url())){
            Glide.with(MyApplication.getInstance())
                    .load(item.getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into((ImageView) holder.getView(R.id.title_bg));
        }


        if (item.isSelect()) {
            holder.setTextColor(R.id.title,Color.parseColor("#4558E6"));
        }else {
            holder.setTextColor(R.id.title,Color.WHITE);
        }


    }
}