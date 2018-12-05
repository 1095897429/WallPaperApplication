package com.ngbj.wallpaper;


import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.IpBean;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.ngbj.wallpaper.network.response.HttpResponse;
import com.ngbj.wallpaper.service.VideoLiveWallpaperService;
import com.ngbj.wallpaper.utils.common.ScreenHepler;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Function;

/***
 *  图片的地址:  http://phcsxfrh8.bkt.clouddn.com/icon_20181101173049
 *  缩略图的地址: http://phcsxfrh8.bkt.clouddn.com/icon_20181101173049?imageView2/1/w/108/h/192
 */
public class PreviewPicActivity extends BaseActivity {
    private static final String TAG = "PreviewPicActivity";
    String url = "http://phcsxfrh8.bkt.clouddn.com/icon_20181101155834";

    @BindView(R.id.imageView)
    ImageView imageView;

    Bitmap bitmap;//此界面的图片


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
    }

    protected void initData() {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
		        .dontAnimate()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                        Glide.with(PreviewPicActivity.this)
//                                .load("http://phcsxfrh8.bkt.clouddn.com/icon_20181101173049")
//                                .placeholder(R.mipmap.ic_launcher)
//							    .dontAnimate()
//                                .into(imageView);
                        bitmap = imageCropper(resource);
                        imageView.setImageBitmap(bitmap);
                    }

                });
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    private Bitmap imageCropper(Bitmap resource){
        int imageHeight = resource.getHeight();
        int imageWidth = resource.getWidth();
        KLog.d("图片的高度： " + imageHeight + "  " + "图片的宽度： " + imageWidth );
        KLog.d("屏幕的高度： " + ScreenHepler.getScreenHeight(PreviewPicActivity.this) + "  "
                + "屏幕的宽度： " + ScreenHepler.getScreenWidth(PreviewPicActivity.this) );
        int wantWidth = ScreenHepler.getScreenWidth(PreviewPicActivity.this);
        int wantHeight = ScreenHepler.getScreenHeight(PreviewPicActivity.this);
        //计算缩放比
        float scaleWidth = (float) wantWidth / imageWidth ;
        float scaleHeight = (float)wantHeight / imageHeight;
        //通过Matrix缩放
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        //得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(resource,0,0,imageWidth,imageHeight,matrix,true);
        return newBitmap;
    }

    //设置桌面壁纸
    private void setScreenWallpaper() {
        int wantWidth = ScreenHepler.getScreenWidth(PreviewPicActivity.this);
        int wantHeight = ScreenHepler.getScreenHeight(PreviewPicActivity.this);
         WallpaperManager mWallManager = WallpaperManager.getInstance(this);
         try {
             mWallManager.suggestDesiredDimensions(wantWidth,wantHeight);
             mWallManager.setBitmap(bitmap);
             Toast.makeText(PreviewPicActivity.this, "壁纸设置成功", Toast.LENGTH_SHORT) .show();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }


    //设置锁屏壁纸
    private void setLockScreenWallpaper() {
        try {
            WallpaperManager mWallManager = WallpaperManager.getInstance(this);
            Class class1 = mWallManager.getClass();
            //获取类名
            Method setWallPaperMethod = class1.getDeclaredMethod("setBitmapToLockWallpaper",Bitmap.class);
            //获取设置锁屏壁纸的函数
            setWallPaperMethod.invoke(mWallManager,bitmap);
            //调用锁屏壁纸的函数,并指定壁纸的路径imageFilesPath
            Toast.makeText(PreviewPicActivity.this, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @OnClick(R.id.home_pic)
    public void home_pic() {

        Map<String,String> map = new HashMap<>();
        map.put("ad_id","12");
        MobclickAgent.onEvent(this, "setWallEvent",map);//壁纸自定义事件
        KLog.d("去上传 setWallEvent");

        RetrofitHelper.getApiService()
              .getIpMsg("180.167.137.215").map(new Function<HttpResponse<IpBean>, String>() {
            @Override
            public String apply(HttpResponse<IpBean> ipBeanHttpResponse) throws Exception {
                return null;
            }
        });

    }

    @OnClick(R.id.lock_img)
    public void lock_img() {
        setLiveWallpaper(this,this,200);
    }


    /**
     * 跳转到系统设置壁纸界面
     *
     * @param context
     * @param paramActivity
     */
    public static void setLiveWallpaper(Context context, Activity paramActivity, int requestCode) {
        try {
            Intent localIntent = new Intent();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {//ICE_CREAM_SANDWICH_MR1  15
                localIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);//android.service.wallpaper.CHANGE_LIVE_WALLPAPER
                //android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT
                localIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT
                        , new ComponentName(context.getApplicationContext().getPackageName()
                                , VideoLiveWallpaperService.class.getCanonicalName()));
            } else {
                localIntent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);//android.service.wallpaper.LIVE_WALLPAPER_CHOOSER
            }
            paramActivity.startActivityForResult(localIntent, requestCode);
        } catch (Exception localException) {
            localException.printStackTrace();
        }

    }




}
