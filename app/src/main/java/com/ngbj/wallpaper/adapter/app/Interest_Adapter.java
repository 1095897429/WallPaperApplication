package com.ngbj.wallpaper.adapter.app;


import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.utils.widget.GlideRoundTransform;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.List;


public class Interest_Adapter extends BaseQuickAdapter<InterestBean,BaseViewHolder> {

    public Interest_Adapter(List<InterestBean> data) {
        super(R.layout.interest_item,data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, InterestBean item) {
        helper.setText(R.id.interest_name,  item.getName());
        if(item.isSelect()){
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_select);
        }else
            helper.setImageResource(R.id.icon_love,R.mipmap.interest_unselect);


        if(!TextUtils.isEmpty(item.getImg_url())){

            Glide.with(mContext)
                    .load(item.getImg_url())
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.imageView));


        }else
            helper.setImageResource(R.id.imageView,R.mipmap.interest_img);





    }



    /** */
    /**
     * 把图片变成圆角
     *
     * @param bitmap
     *            需要修改的图片
     * @param pixels
     *            圆角的弧度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }




}