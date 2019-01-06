package com.ngbj.wallpaper.adapter.my;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.utils.widget.GlideCircleTransform;
import com.ngbj.wallpaper.utils.widget.GlideRoundTransform;

import java.util.List;

public class MyCommonAdapter extends BaseQuickAdapter<MulAdBean,BaseViewHolder> {


    public MyCommonAdapter(List<MulAdBean> data) {
        super(R.layout.recommand_item,data);
    }



    /** 只是更新 屏幕上所有能看见的数据  */
    @Override
    protected void convert(BaseViewHolder holder, MulAdBean mulAdBean) {

        AdBean adBean = mulAdBean.adBean;
        //背景
        if(!TextUtils.isEmpty(adBean.getThumb_img_url())){

            Glide.with(mContext)
                    .load(adBean.getThumb_img_url())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .transform(new GlideRoundTransform(mContext,5))
                    .into((ImageView) holder.getView(R.id.imageView));

        }else
            holder.setImageResource(R.id.imageView,R.mipmap.release_image);



        //头像

        Glide.with(mContext)
                .load("http://pjb68wj3e.bkt.clouddn.com/jmCTgYg96Nh_2u4HfI_UJXIks_lvOGsR.jpg?imageView2/1/w/162/h/216")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .transform(new GlideCircleTransform(mContext,4,
                        mContext.getResources().getColor(R.color.item_bottom_color)))
                .into((ImageView) holder.getView(R.id.author_icon));


        //圆形头像
        if (!TextUtils.isEmpty(adBean.getHead_img())) {

            Glide.with(MyApplication.getInstance())
                    .load(adBean.getHead_img())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .transform(new GlideCircleTransform(mContext))
                    .into((ImageView) holder.getView(R.id.author_icon));
        } else {
            holder.setImageResource(R.id.author_icon,R.mipmap.author_head);
        }


        //喜爱
        if("0".equals(adBean.getIs_collected())){
            holder.setImageResource(R.id.icon_love,R.mipmap.recommend_unlove);
        }else{
            holder.setImageResource(R.id.icon_love,R.mipmap.recommend_love);
        }

        //作者
        holder.setText(R.id.author_name,adBean.getNickname());

        //隐藏 左上角的文本 右上角喜好 作者 头像
        if(!TextUtils.isEmpty(adBean.getType())){
            if(adBean.getType().equals("0")){
                holder.setVisible(R.id.live_or_ad,true);
                holder.setVisible(R.id.author_name,false);
                holder.setVisible(R.id.author_icon,false);
                holder.setVisible(R.id.icon_love,false);
                holder.setText(R.id.live_or_ad,"广告");
            }else if(adBean.getType().equals("2")){
                holder.setVisible(R.id.live_or_ad,true);
                holder.setVisible(R.id.author_name,true);
                holder.setVisible(R.id.author_icon,true);
                holder.setVisible(R.id.icon_love,true);
                holder.setText(R.id.live_or_ad,"live");
            }else if(adBean.getType().equals("1")){
                holder.setVisible(R.id.live_or_ad,false);
                holder.setVisible(R.id.author_name,true);
                holder.setVisible(R.id.author_icon,true);
                holder.setVisible(R.id.icon_love,true);
            }
        }

        //设置点击
        holder.addOnClickListener(R.id.icon_love);
    }

}
