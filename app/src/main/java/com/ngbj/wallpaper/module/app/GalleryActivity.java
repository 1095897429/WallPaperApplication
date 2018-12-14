package com.ngbj.wallpaper.module.app;

import android.app.WallpaperManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.GalleryAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class GalleryActivity extends BaseActivity<DetailPresenter>
            implements DetailContract.View{

    @BindView(R.id.mGallery)
    Gallery mGallery;


    private WallpaperManager wpManager;
    private ArrayList<String> mListBigUrl = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenter();
    }

    @Override
    protected void initData() {
        //TODO viewpager加载view的数据源
//        mPresenter.getData(1);


        //壁纸管理器
        wpManager = WallpaperManager.getInstance(this);
        mListBigUrl.add("http://pjb68wj3e.bkt.clouddn.com/icon_20181214145027");
        mListBigUrl.add("http://pjb68wj3e.bkt.clouddn.com/bMXPcCIuQ9kef_bZucRl3puxmtnIQuZk.jpg");
        mListBigUrl.add("http://pjb68wj3e.bkt.clouddn.com/icon_20181214145027");
        mListBigUrl.add("http://pjb68wj3e.bkt.clouddn.com/icon_20181214145027");
        mListBigUrl.add("http://pjb68wj3e.bkt.clouddn.com/icon_20181214145027");

        if (mListBigUrl.size() > 0) {
            mGallery.setAdapter(new GalleryAdapter(this, mListBigUrl));
            mGallery.setSelection(0);
        }
    }


    @Override
    protected void initEvent() {

    }



    @Override
    public void showData(AdBean adBean) {

    }

    @Override
    public void showDynamicData(List<WallpagerBean> list) {



    }





}
