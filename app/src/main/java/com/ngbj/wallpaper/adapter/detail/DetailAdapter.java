package com.ngbj.wallpaper.adapter.detail;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.socks.library.KLog;

import java.util.List;

/***
 * vp的适配器 -- 关联 数据源
 */
public class DetailAdapter extends PagerAdapter implements View.OnClickListener {


    List<AdBean> adBeanList ;
    LayoutInflater inflater;
    Context context;
    View view;

    public DetailAdapter(Context context,List<AdBean> adBeanList){
        inflater = LayoutInflater.from(context);
        this.adBeanList = adBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
//        return 10 * 1000;
        return adBeanList.isEmpty() ? 0 : adBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = inflater.inflate(R.layout.activity_detail_item,null);

        initView(position);
        container.addView(view);
        return view;
    }


    ConstraintLayout part1;
    ConstraintLayout part2;
    ImageView imageView;
    private void initView(int position) {
          part1 = view.findViewById(R.id.part1);
          part2 = view.findViewById(R.id.part2);
          imageView  = view.findViewById(R.id.imageView);
         ImageView back = view.findViewById(R.id.back);
        final ImageView down = view.findViewById(R.id.down);

        Glide.with(context)
                .load(adBeanList.get(position).getThumb_img_url())
                .centerCrop()
                .into(imageView);
        back.setOnClickListener(this);

        imageView.setOnClickListener(this);

    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                ((Activity)context).finish();
                break;
            case R.id.imageView:
                if(part1.getVisibility() == View.VISIBLE){
                    part1.setVisibility(View.GONE);
                }else
                    part1.setVisibility(View.VISIBLE);

                if(part2.getVisibility() == View.VISIBLE){
                    part2.setVisibility(View.GONE);
                }else
                    part2.setVisibility(View.VISIBLE);
                break;

        }
    }
}
