package com.ngbj.wallpaper.adapter.index;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;

import java.util.List;

/**
 * 类描述：
 * 修改备注：gridView的适配器
 */
public class IndexCoolGridAdapter extends BaseAdapter {

    private List<IndexBean.Navigation> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//表示第几页 从0开始
    private int mPageSize;//每页显示最大数量

    public IndexCoolGridAdapter(Context context, List<IndexBean.Navigation> listData, int mIndex , int mPageSize){
        this.context = context;
        this.listData = listData;
        this.mIndex = mIndex;
        this.mPageSize = mPageSize;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listData.size() > (mIndex + 1) * mPageSize ? mPageSize : (listData.size() - mIndex * mPageSize);
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position + mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.index_cool_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.imgUrl = convertView.findViewById(R.id.imgUrl);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();

        //设置数据
        final int pos = position + mIndex * mPageSize;
        IndexBean.Navigation bean = listData.get(pos);
        viewHolder.title.setText(bean.getTitle());
        //图片 ，如果没有图片地址，显示默认
        if(!TextUtils.isEmpty(bean.getImg_url())){
            Glide.with(context)
                    .load(bean.getImg_url())
                    .into(viewHolder.imgUrl);
        }else{
            viewHolder.imgUrl.setImageResource(R.mipmap.cool_icon);
        }


        return convertView;
    }

    class ViewHolder{
        TextView title;
        ImageView imgUrl;
    }
}

