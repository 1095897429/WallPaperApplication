package com.ngbj.wallpaper.adapter.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.dialog.HeadAlertDialog;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.ReleaseActivity;
import com.ngbj.wallpaper.utils.common.WallpaperUtil;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * vp的适配器 -- 关联 数据源
 */
public class DetailAdapter extends PagerAdapter{


    List<AdBean> adBeanList ;
    LayoutInflater inflater;
    Context mContext;
    View view;

    SurfaceHolder surfaceHolder;
    public static MediaPlayer mediaPlayer  = new MediaPlayer();


    /** 回调接口 开始 */
    public OnDialogItemClickListener mOnDialogItemClickListener;

    public interface OnDialogItemClickListener{
        void func(int position);
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        mOnDialogItemClickListener = onDialogItemClickListener;
    }

    /** 回调接口 结束 */



    public DetailAdapter(Context context,List<AdBean> adBeanList){
        inflater = LayoutInflater.from(context);
        this.adBeanList = adBeanList;
        mContext = context;
    }


    @Override
    public int getCount() {
        return adBeanList.isEmpty() ? 0 : adBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        view = inflater.inflate(R.layout.activity_detail_item,null);
        initView(position);
        mOnDialogItemClickListener.func(2);
        container.addView(view);
        return view;
    }



    private void initView(int position) {
        final AdBean adBean = adBeanList.get(position);
        final ConstraintLayout part1 = view.findViewById(R.id.part1);//头部
        final ConstraintLayout part2 = view.findViewById(R.id.part2);//底部
        final ImageView imageView  = view.findViewById(R.id.imageView);//图片
        final SurfaceView surfaceview = view.findViewById(R.id.surfaceview);//视频
        final ImageView back = view.findViewById(R.id.back);//返回
        final ImageView report = view.findViewById(R.id.report);//举报
        final TextView down = view.findViewById(R.id.down);//下载
        final ImageView icon_share = view.findViewById(R.id.icon_share);//分享
        final ImageView icon_preview = view.findViewById(R.id.icon_preview);//预览
        final ImageView icon_save = view.findViewById(R.id.icon_save);//设置壁纸

        if("2".equals(adBean.getType())){//动态
            imageView.setVisibility(View.GONE);
            surfaceview.setVisibility(View.VISIBLE);
            initMediaPlayer(surfaceview);
            initData();
        }else if("1".equals(adBean.getType())){//静态
            imageView.setVisibility(View.VISIBLE);
            surfaceview.setVisibility(View.GONE);
        }


//        Glide.with(mContext)
//                .load(adBeanList.get(position).getThumb_img_url())
//                .placeholder(R.mipmap.detail_image)
//                .centerCrop()
//                .into(imageView);

        //设置壁纸的事件
        icon_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adBean.getType().equals("2")){
                    KLog.d("跳转到系统");
                    mOnDialogItemClickListener.func(0);
                }else if(adBean.getType().equals("1")){
                    KLog.d("自动设置");
                    mOnDialogItemClickListener.func(1);
                }


            }
        });


        //预览的事件
        icon_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("预览");
                previewImage();
            }
        });

        //分享的事件
        icon_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("分享");
                shareImage();
            }
        });

        //下载的事件
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("下载");
                downImageVideo();
            }
        });

        //举报的事件
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showReport();
            }
        });


       //返回的事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });

        //点击图片的事件
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(part1.getVisibility() == View.VISIBLE){
                    part1.setVisibility(View.GONE);
                }else
                    part1.setVisibility(View.VISIBLE);

                if(part2.getVisibility() == View.VISIBLE){
                    part2.setVisibility(View.GONE);
                }else
                    part2.setVisibility(View.VISIBLE);
            }
        });
        //点击视频的事件
        surfaceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(part1.getVisibility() == View.VISIBLE){
                    part1.setVisibility(View.GONE);
                }else
                    part1.setVisibility(View.VISIBLE);

                if(part2.getVisibility() == View.VISIBLE){
                    part2.setVisibility(View.GONE);
                }else
                    part2.setVisibility(View.VISIBLE);
            }
        });
        //长按视频的事件
        surfaceview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mediaPlayer.start();
                return false;
            }
        });

    }

    private void initData() {
        //准备数据



        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            //方式二
            AssetManager assetManager = mContext.getApplicationContext().getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.mp4");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();//获取解析资源
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMediaPlayer(SurfaceView surfaceView) {

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                KLog.d("准备完成，可能自动播放");
//                mediaPlayer.start();
            }
        });
        surfaceHolder = surfaceView.getHolder();
        // 设置SurfaceView自己不管理的缓冲区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    private void previewImage() {
        List<String> temps = new ArrayList<>();
        temps.add("桌面预览");
        temps.add("锁屏预览");
        temps.add("取消");

        PreviewAlertDialog previewAlertDialog =  new PreviewAlertDialog(mContext)
                .builder()
                .setPreviewBeanList(temps);
        previewAlertDialog.setOnDialogItemClickListener(new PreviewAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("桌面预览");
                        yellowReport();
                        break;
                    case 1:
                        KLog.d("锁屏预览");
                        copyrightReport();
                        break;
                }
            }
        });
        previewAlertDialog.show();
    }



    private void shareImage() {
        List<ShareBean> temps = new ArrayList<>();
        temps.add(new ShareBean(R.mipmap.wechat_share,"微信"));
        temps.add(new ShareBean(R.mipmap.friend_share,"朋友圈"));
        temps.add(new ShareBean(R.mipmap.qq_share,"QQ"));
        temps.add(new ShareBean(R.mipmap.weibo_share,"微博"));
        temps.add(new ShareBean(R.mipmap.qqkj_share,"QQ空间"));

        ShareAlertDialog shareAlertDialog =  new ShareAlertDialog(mContext)
                .builder()
                .seShareBeanList(temps);
        shareAlertDialog.setOnDialogItemClickListener(new ShareAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("微信");

                        break;
                    case 1:
                        KLog.d("朋友圈");
                        break;
                    case 2:
                        KLog.d("QQ");
                        break;
                    case 3:
                        KLog.d("微博");
                        break;
                    case 4:
                        KLog.d("QQ空间");
                        break;
                }
            }
        });
        shareAlertDialog.show();
    }

    private void downImageVideo() {

    }

    private void showReport() {
        List<String> temps = new ArrayList<>();
        temps.add("色情低俗");
        temps.add("侵犯版权");
        temps.add("取消");

        ReportAlertDialog reportAlertDialog =  new ReportAlertDialog(mContext)
                .builder()
                .setReportBeanList(temps);
        reportAlertDialog.setOnDialogItemClickListener(new ReportAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("色情低俗");
                        yellowReport();
                        break;
                    case 1:
                        KLog.d("侵犯版权");
                        copyrightReport();
                        break;
                }
            }
        });
        reportAlertDialog.show();
    }

    private void copyrightReport() {

    }

    private void yellowReport() {

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
