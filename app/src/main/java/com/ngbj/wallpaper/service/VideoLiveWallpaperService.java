package com.ngbj.wallpaper.service;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.TestBean;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.socks.library.KLog;

import java.io.IOException;

/***
 * 实现动态壁纸的 Service
 */
public class VideoLiveWallpaperService extends WallpaperService {

    private  Engine mEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        onCreateEngine();
    }

    public Engine onCreateEngine() {
        KLog.d("VideoLiveWallpaperService","onCreateEngine");
        mEngine = new VideoEngine();
//        if(mEngine.isPreview()){
//
//        }
        return mEngine;
    }

    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.zl.my_wallpaper";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;

    public static void voiceSilence(Context context) {
        Intent intent = new Intent(VideoLiveWallpaperService.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoLiveWallpaperService.KEY_ACTION, VideoLiveWallpaperService.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    public static void voiceNormal(Context context) {
        Intent intent = new Intent(VideoLiveWallpaperService.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoLiveWallpaperService.KEY_ACTION, VideoLiveWallpaperService.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    /** 跳转到系统设置壁纸界面 4.1.2之上的版本 请看下面的方法 */
    static String path;
    public static void setToWallPaper(Context context,String destinationUri) {
        path = destinationUri;
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, VideoLiveWallpaperService.class));
        context.startActivity(intent);
    }

    /** 测试 1 */
    public static void startLiveWallpaperPrevivew(Activity activity) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(activity, VideoLiveWallpaperService.class));
        }
        activity.startActivityForResult(intent, 100);
    }



    /**
     * 去往某个动态壁纸的预览页面,那里可以设置壁纸
     *
     * @param packageName
     *            动态壁纸的包名
     * @param classFullName
     *            动态壁纸service类的类全名
     */
    public static void startLiveWallpaperPrevivew(Activity activity, String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        activity.startActivityForResult(intent, 100);
    }


    class VideoEngine extends Engine {

        private MediaPlayer mMediaPlayer;

        private BroadcastReceiver mVideoParamsControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            KLog.d("VideoEngine#onCreate");

            boolean isPreview = isPreview();
            if(isPreview){
                KLog.d("预览的engine");
            }


            IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
            registerReceiver(mVideoParamsControlReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    KLog.d("onReceive");
                    int action = intent.getIntExtra(KEY_ACTION, -1);

                    switch (action) {
                        case ACTION_VOICE_NORMAL:
                            mMediaPlayer.setVolume(1.0f, 1.0f);
                            break;
                        case ACTION_VOICE_SILENCE:
                            mMediaPlayer.setVolume(0, 0);
                            break;

                    }
                }
            }, intentFilter);


        }

        @Override
        public void onDestroy() {
            KLog.d("VideoEngine#onDestroy");
            unregisterReceiver(mVideoParamsControlReceiver);
            super.onDestroy();

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            KLog.d("VideoEngine#onVisibilityChanged visible = " + visible);
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            KLog.d("VideoEngine#onSurfaceCreated ");
            super.onSurfaceCreated(holder);

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(holder.getSurface());
            try {
//                AssetManager assetMg = getApplicationContext().getAssets();
//                AssetFileDescriptor fileDescriptor = assetMg.openFd("test.mp4");
//                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
//                        fileDescriptor.getStartOffset(), fileDescriptor.getLength());

                //本地的视频  需要在手机SD卡根目录添加一个 fl1234.mp4 视频
//                String path = (String) SPHelper.get(MyApplication.getInstance(),"video","");
                TestBean bean = MyApplication.getDbManager().queryTestBean();
                KLog.d("path: " ,bean.getUrl());
                mMediaPlayer.setDataSource(bean.getUrl());


                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.prepare();
                mMediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d("VideoEngine#onSurfaceChanged ");
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            KLog.d("VideoEngine#onSurfaceDestroyed ");
            super.onSurfaceDestroyed(holder);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;

            VideoLiveWallpaperService.this.stopSelf();
        }
    }
}
