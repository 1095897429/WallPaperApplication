package com.ngbj.wallpaper.adapter.index;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.AdBean;
import com.ngbj.wallpaper.bean.MulAdBean;

import java.util.List;

public class RecomendAdapter extends BaseMultiItemQuickAdapter<MulAdBean,BaseViewHolder> {


    public RecomendAdapter(List<MulAdBean> data) {
        super(data);
        addItemType(MulAdBean.TYPE_ONE, R.layout.recommand_item);
        addItemType(MulAdBean.TYPE_TWO, R.layout.recommand_item_ad);
    }


    /** 只是更新 屏幕上所有能看见的数据  */
    @Override
    protected void convert(BaseViewHolder holder, MulAdBean item) {
        switch (holder.getItemViewType()){
            case MulAdBean.TYPE_ONE://正常
                AdBean adBean = item.adBean;
                holder.setText(R.id.live_or_ad,adBean.getTitle());
                if("0".equals(adBean.getIs_collected())){
                    holder.setImageResource(R.id.icon_love,R.mipmap.recommend_unlove);
                }else{
                    holder.setImageResource(R.id.icon_love,R.mipmap.recommend_love);
                }
                //设置点击
                holder.addOnClickListener(R.id.icon_love);
                break;

            case MulAdBean.TYPE_TWO:
                break;

        }
    }
}
