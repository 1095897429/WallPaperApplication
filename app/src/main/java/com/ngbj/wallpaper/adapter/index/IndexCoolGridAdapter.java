package com.ngbj.wallpaper.adapter.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.AdBean;

import java.util.List;

/**
 * 类描述：
 * 修改备注：gridView的适配器
 */
public class IndexCoolGridAdapter extends BaseAdapter {

    private List<AdBean> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//表示第几页 从0开始
    private int mPageSize;//每页显示最大数量

    public IndexCoolGridAdapter(Context context, List<AdBean> listData, int mIndex , int mPageSize){
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
        AdBean bean = listData.get(pos);
        viewHolder.title.setText("动态壁纸");
//        Glide.with(context)
//                .load(bean.getImg_url())
//                .into(viewHolder.imgUrl);

        return convertView;
    }

    class ViewHolder{
        TextView title;
        ImageView imgUrl;
    }
}

