package com.ngbj.wallpaper.adapter.my;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.AdBean;
import com.ngbj.wallpaper.bean.MulAdBean;

import java.util.List;

public class MyCommonAdapter extends BaseQuickAdapter<AdBean,BaseViewHolder> {


    public MyCommonAdapter(List<AdBean> data) {
        super(R.layout.recommand_item,data);
    }



    /** 只是更新 屏幕上所有能看见的数据  */
    @Override
    protected void convert(BaseViewHolder holder, AdBean adBean) {
            holder.setText(R.id.live_or_ad,adBean.getTitle());
            if("0".equals(adBean.getIs_collected())){
                holder.setImageResource(R.id.icon_love,R.mipmap.recommend_unlove);
            }else{
                holder.setImageResource(R.id.icon_love,R.mipmap.recommend_love);
            }
            //设置点击
            holder.addOnClickListener(R.id.icon_love);
        }

}
