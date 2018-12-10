package com.ngbj.wallpaper.module.app;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.view.GestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.DetailAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.mediaPlayer.setDataSource(this,Uri.parse(uri));//url 路径 方式一
 * 2.http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4
 */
public class DetailActivity extends BaseActivity<DetailPresenter>
            implements DetailContract.View{


    @BindView(R.id.imageView)
    ImageView imageView;


    @BindView(R.id.part1)
    ConstraintLayout part1;

    @BindView(R.id.part2)
    ConstraintLayout part2;

    @BindView(R.id.surfaceview)
    SurfaceView surfaceView;


    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailPresenter();
    }

    @Override
    protected void initData() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                KLog.d("准备完成，可能自动播放");
                mediaPlayer.start();
            }
        });
        surfaceHolder = surfaceView.getHolder();
        // 设置SurfaceView自己不管理的缓冲区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new MyCallBack());

        //TODO 加载第一帧图片
//        InputStream abpath = getClass().getResourceAsStream("/assets/test.mp4");
//        String path = new String(InputStreamToByte(abpath ));
//        getVideoThumbnail("",path);

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

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }



    @Override
    public void showVerCodeData() {

    }

    @Override
    public void showDynamicData(List<AdBean> list) {

        //准备数据
        if(mediaPlayer.isPlaying()) mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            //方式二
            AssetManager assetManager = getApplicationContext().getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.mp4");

            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),fileDescriptor.getLength());

            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();//获取解析资源
            KLog.d("开始播放");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }


    private void play() {
        if(mediaPlayer.isPlaying()) mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            //方式二
            AssetManager assetManager = getApplicationContext().getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.mp4");

            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),fileDescriptor.getLength());

            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();//获取解析资源
            //播放
            mediaPlayer.start();
            KLog.d("开始播放");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ----------  各种点击事件 ---------- */
    @OnClick(R.id.back)
    public void back(){
        finish();
    }

    @OnClick(R.id.down)
    public void down(){
        KLog.d("download");
        play();
    }


    @OnClick(R.id.imageView)
    public void ImageView(){
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


    @OnClick(R.id.image_tag)
    public void ImageTag(){
        KLog.d("点击了标签页");
    }


    @OnClick(R.id.icon_preview)
    public void IconPreview(){
        KLog.d("预览");
    }


    @OnClick(R.id.icon_love)
    public void IconLove(){
        KLog.d("喜好");
    }


    @OnClick(R.id.icon_share)
    public void IconShare(){
        KLog.d("分享");
    }

    /** 通过url网址或者本地文件路径获得视频的第一帧图片 url -- 网络地址  filePath -- 本地文件地址*/
    private Bitmap getVideoThumbnail(String url,String filePath){
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            //方式一 根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //方式一 retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        KLog.d("bitmap", "bitmap="+ bitmap);
        //设值
        if(null != bitmap)
            imageView.setImageBitmap(bitmap);
        return bitmap;
    }



}
