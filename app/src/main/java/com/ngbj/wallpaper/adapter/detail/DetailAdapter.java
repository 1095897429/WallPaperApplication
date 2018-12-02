package com.ngbj.wallpaper.adapter.detail;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.AdBean;

import java.util.List;

/***
 * vp的适配器 -- 关联 数据源
 */
public class DetailAdapter extends PagerAdapter {


    List<AdBean> adBeanList ;
    LayoutInflater inflater;
    View view;

    public DetailAdapter(Context context){
        inflater = LayoutInflater.from(context);

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
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
