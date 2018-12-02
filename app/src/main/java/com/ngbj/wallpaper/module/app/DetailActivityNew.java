package com.ngbj.wallpaper.module.app;

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
import android.widget.ImageView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.DetailAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/***
 * 1.mediaPlayer.setDataSource(this,Uri.parse(uri));//url 路径 方式一
 * 2.http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4
 */
public class DetailActivityNew extends BaseActivity<DetailPresenter>
            implements DetailContract.View{

    @BindView(R.id.verticalviewpager)
    VerticalViewPager verticalviewpager;


//    @BindView(R.id.imageView)
//    ImageView imageView;
//
//
//    @BindView(R.id.part1)
//    ConstraintLayout part1;
//
//    @BindView(R.id.part2)
//    ConstraintLayout part2;
//
//    @BindView(R.id.surfaceview)
//    SurfaceView surfaceView;
//
//
//    SurfaceHolder surfaceHolder;
//    MediaPlayer mediaPlayer;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_new;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenter();
    }

    @Override
    protected void initData() {

//        mPresenter.getDynamicData();

        //TODO viewpager加载view的数据源
        mPresenter.getVerCodeData();
    }


    @Override
    protected void initEvent() {

    }

    private byte[] InputStreamToByte(InputStream is) {

        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





    @Override
    public void showVerCodeData() {
        DetailAdapter detailAdapter = new DetailAdapter(this);
        verticalviewpager.setAdapter(detailAdapter);

    }

    @Override
    public void showDynamicData() {

    }








}
