package com.ngbj.wallpaper.adapter.detail;
/*
 *  项目名：  LoveWallpaper 
 *  包名：    com.liuguilin.lovewallpaper.adapter
 *  文件名:   GalleryAdapter
 *  创建者:   LGL
 *  创建时间:  2017/1/11 13:24
 *  描述：    画廊适配器
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ngbj.wallpaper.utils.common.GlideUtils;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mList;

    public GalleryAdapter(Context mContext, ArrayList<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ImageView imageView = new ImageView(mContext);
        GlideUtils.loadImageCrop(mContext, mList.get(i), imageView);
        imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.WRAP_CONTENT));


        Glide.with(mContext).load(mList.get(i)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap bitmap = rotateBitmap(resource);
                imageView.setImageBitmap(bitmap);
            }
        });

        return imageView;

    }


    private Bitmap rotateBitmap(Bitmap bitmap) {
        Bitmap bitmap2 = null;
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return bitmap2;
    }

}
