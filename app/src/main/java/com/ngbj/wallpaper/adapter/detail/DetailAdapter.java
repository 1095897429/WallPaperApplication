package com.ngbj.wallpaper.adapter.detail;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.socks.library.KLog;

import java.util.List;

/***
 * vp的适配器 -- 关联 数据源
 */
public class DetailAdapter extends PagerAdapter {


    List<AdBean> adBeanList ;
    LayoutInflater inflater;
    View view;

    public DetailAdapter(Context context,List<AdBean> adBeanList){
        inflater = LayoutInflater.from(context);
        this.adBeanList = adBeanList;
    }

    @Override
    public int getCount() {
        return 10 * 1000;
//        return adBeanList.isEmpty() ? 0 : adBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = inflater.inflate(R.layout.activity_detail_item,null);
        container.addView(view);
        initEvent(view);
        return view;
    }

    private void initEvent(View view) {
        final ConstraintLayout part1 = view.findViewById(R.id.part1);
        final ConstraintLayout part2 = view.findViewById(R.id.part2);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("ImageView");
                if(part1.getVisibility() == View.VISIBLE){
                    part1.setVisibility(View.GONE);
                }else
                    part1.setVisibility(View.VISIBLE);

                if(part2.getVisibility() == View.VISIBLE){
                    part2.setVisibility(View.GONE);
                }else
                    part2.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
