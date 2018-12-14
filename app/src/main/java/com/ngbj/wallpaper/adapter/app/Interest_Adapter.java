package com.ngbj.wallpaper.adapter.app;


import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;

import java.util.List;


public class Interest_Adapter extends BaseQuickAdapter<InterestBean,BaseViewHolder> {

    public Interest_Adapter(List<InterestBean> data) {
        super(R.layout.interest_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InterestBean item) {
        helper.setText(R.id.interest_name,  item.getName());
        if(item.isSelect()){
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_select);
        }else
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_unselect);

        //头像
        if(!TextUtils.isEmpty(item.getImg_url())){
            Glide.with(mContext)
                    .load(item.getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.imageView));
        }else
            helper.setImageResource(R.id.imageView,R.mipmap.interest_img);


    }
}