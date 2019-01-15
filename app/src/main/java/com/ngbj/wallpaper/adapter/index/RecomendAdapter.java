package com.ngbj.wallpaper.adapter.index;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.utils.widget.GlideCircleTransform;
import com.ngbj.wallpaper.utils.widget.GlideRoundTransform;

import java.util.List;

/***
 * 用到的地方：
 * 1.首页推荐
 * 2.壁纸分类
 */
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

                //背景
                if(!TextUtils.isEmpty(adBean.getThumb_img_url())){

                    Glide.with(mContext)
                            .load(adBean.getThumb_img_url())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.default_loading)
                            .centerCrop()
                            .crossFade()
                            .transform(new GlideRoundTransform(mContext,8))
                            .into((ImageView) holder.getView(R.id.imageView));

                }else
                    holder.setImageResource(R.id.imageView,R.mipmap.default_loading);



                //圆形头像
                if (!TextUtils.isEmpty(adBean.getHead_img())) {

                    Glide.with(MyApplication.getInstance())
                            .load(adBean.getHead_img())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .centerCrop()
                            .crossFade()
                            .transform(new GlideCircleTransform(mContext,4,
                                   mContext.getResources().getColor(R.color.item_bottom_color)))
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

                //隐藏 左上角的文本 右上角喜好 作者 头像 底部背景色
                if(!TextUtils.isEmpty(adBean.getType())){
                    if(adBean.getType().equals("0")){
                        holder.setVisible(R.id.live_or_ad,true);
                        holder.setVisible(R.id.author_name,false);
                        holder.setVisible(R.id.author_icon,false);
                        holder.setVisible(R.id.icon_love,false);
                        holder.setVisible(R.id.bottom_view,false);
                        holder.setText(R.id.live_or_ad,"广告");

                    }else if(adBean.getType().equals("2")){
                        holder.setVisible(R.id.live_or_ad,true);
                        holder.setVisible(R.id.author_name,true);
                        holder.setVisible(R.id.author_icon,true);
                        holder.setVisible(R.id.icon_love,true);
                        holder.setVisible(R.id.bottom_view,true);
                        holder.setText(R.id.live_or_ad,"live");
                    }else if(adBean.getType().equals("1")){
                        holder.setVisible(R.id.live_or_ad,false);
                        holder.setVisible(R.id.author_name,true);
                        holder.setVisible(R.id.author_icon,true);
                        holder.setVisible(R.id.icon_love,true);
                        holder.setVisible(R.id.bottom_view,true);
                    }
                }

                //设置点击
                holder.addOnClickListener(R.id.icon_love);
                break;

            case MulAdBean.TYPE_TWO:
                ApiAdBean apiAdBean = item.apiAdBean;
                //大型广告
                if(!TextUtils.isEmpty(apiAdBean.getImgUrl())){

                    Glide.with(mContext)
                            .load(apiAdBean.getImgUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.mipmap.default_loading)
                            .centerCrop()
                            .crossFade()
                            .into((ImageView) holder.getView(R.id.imageView_ad));

                }

                break;

        }
    }
}
