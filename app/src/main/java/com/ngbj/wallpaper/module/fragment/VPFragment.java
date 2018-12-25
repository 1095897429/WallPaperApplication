package com.ngbj.wallpaper.module.fragment;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.mvp.contract.fragment.VpContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.VpPresenter;
import com.ngbj.wallpaper.service.VideoLiveWallpaperService;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.downfile.DownManager;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class VPFragment extends BaseFragment<VpPresenter>
            implements VpContract.View {


    @BindView(R.id.image_title)
    TextView title;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.icon_love)
    ImageView icon_love;

    @BindView(R.id.imageView)
    ImageView mImageView;

    @BindView(R.id.author_icon)
    ImageView mAuthorIcon;


    @BindView(R.id.video_image)
    ImageView mVideoImage;


    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceView;

    @BindView(R.id.video_part)
    FrameLayout mFrameLayout;


    @BindView(R.id.part1)
    RelativeLayout part1;

    @BindView(R.id.part2)
    ConstraintLayout part2;

    private WallpagerBean mWallpagerBean;
    private AdBean mAdBean;
    private Context mContext;

    private WallpaperManager wpManager;

    String wallpagerId;
    String type;//区分 静态 动态的标志

    public VPFragment(){}


    public static VPFragment getInstance(WallpagerBean wallpagerBean){
        VPFragment mFragment = new VPFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", wallpagerBean);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_item_1;
    }


    @Override
    protected void lazyLoadData() {

        mWallpagerBean = (WallpagerBean) getArguments().getSerializable("bean");
        wallpagerId = mWallpagerBean.getWallpager_id();
        type = mWallpagerBean.getType();

        if(TextUtils.isEmpty(type)){
            KLog.d(" -------------- 没有type -------------- ");
            getActivity().finish();
            return;
        }

        KLog.d("点击的壁纸Id: "  + wallpagerId);
        mContext = getActivity();

        //头像
        if(!TextUtils.isEmpty(mWallpagerBean.getHead_img())){
            Glide.with(getActivity())
                    .load(mWallpagerBean.getHead_img())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(mAuthorIcon);
        }else{
            mAuthorIcon.setImageResource(R.mipmap.author_head);
        }


        /** 如果是动态的话，显示surface  + 初始化mediaplay + 保存动态url*/
        if(type.equals(AppConstant.DYMATIC_WP)){
            mVideoImage.setVisibility(View.VISIBLE);

            mFrameLayout.setVisibility(View.VISIBLE);

            mPresenter.getData(wallpagerId);
            smallImgUrl = mWallpagerBean.getThumb_img_url();

            Glide.with(getActivity())
                    .load(smallImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(mVideoImage);


            initMediaPlayer();

        }else if(type.equals(AppConstant.COMMON_AD) ||
                    type.equals(AppConstant.API_AD)){ //TODO 2018.12.20 如果是广告，接API广告 + api广告
            KLog.d(" -- 接API广告 --");
        }else{
            mPresenter.getData(wallpagerId);
        }

    }





    @Override
    protected void initPresenter() {
        mPresenter = new VpPresenter();
    }




    @Override
    protected void initEvent() {

        /** 长按点击事件 */
        mVideoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                play();
                mVideoImage.setVisibility(View.GONE);
                return false;
            }
        });
    }

    private void setDefault(){
        if(part1.getVisibility() == View.VISIBLE){
            part1.setVisibility(View.GONE);
        }else
            part1.setVisibility(View.VISIBLE);

        if(part2.getVisibility() == View.VISIBLE){
            part2.setVisibility(View.GONE);
        }else
            part2.setVisibility(View.VISIBLE);
    }

    //点击图片的事件
    @OnClick(R.id.imageView)
    public void ImageView(){

        setDefault();

    }

    @OnClick(R.id.video_image)
    public void VideoImage(){
        setDefault();

    }




    //设置壁纸的事件 http://pjb68wj3e.bkt.clouddn.com/bMXPcCIuQ9kef_bZucRl3puxmtnIQuZk.jpg
    @OnClick(R.id.icon_save)
    public void settingWallpaper(){
        setWallpaper();
    }

    //返回的事件
    @OnClick(R.id.back)
    public void Back(){
        getActivity().finish();
    }


    //举报的事件
    @OnClick(R.id.report)
    public void Report(){
        showReport();
    }

    //分享的事件
    @OnClick(R.id.icon_share)
    public void IconShare(){
        shareImage();
    }


    //预览的事件
    @OnClick(R.id.icon_preview)
    public void IconPreview(){
        previewImage();
    }


    //下载的事件
    @OnClick(R.id.down)
    public void Down(){
//        diffRecod("1");
        downVideo();
    }

    //收藏的事件
    @OnClick(R.id.icon_love)
    public void IconLove(){

        if("0".equals(mAdBean.getIs_collected())){
            mAdBean.setIs_collected("1");
            ToastHelper.customToastView(getActivity(),"收藏成功");
            icon_love.setImageResource(R.mipmap.icon_love);
        }else{
            mAdBean.setIs_collected("0");
            ToastHelper.customToastView(getActivity(),"取消收藏");
            icon_love.setImageResource(R.mipmap.icon_unlove);
        }


        diffRecod("2");
    }



    /** ---------  动态  开始 -------------*/

    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    String dynamicUrl;//图片的地址 视频的地址
    String smallImgUrl;

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                KLog.d("准备完成，可自动播放");
                mediaPlayer.start();//播放资源
            }
        });
        surfaceHolder = mSurfaceView.getHolder();
        // 设置SurfaceView自己不管理的缓冲区
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new MyCallBack());
    }


    private void play() {
        if(mediaPlayer.isPlaying()) mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            //方式二 测试
//            AssetManager assetManager = MyApplication.getInstance().getAssets();
//            AssetFileDescriptor fileDescriptor = assetManager.openFd("test.mp4");
//            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
//                    fileDescriptor.getStartOffset(),fileDescriptor.getLength());

            mediaPlayer.setDataSource(dynamicUrl);//设置播放资源
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();//获取解析资源

            KLog.d("开始播放");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private class MyCallBack implements SurfaceHolder.Callback {

        //确保SurfaceHolder已经准备好的回调
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);//设置surfaceHolder
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.d("surfaceDestroyed");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KLog.d("onCreateonCreate " + wallpagerId);
    }

    @Override
    public void onPause() {
        super.onPause();
        KLog.d("onPause");
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.d("onDestroy");
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onStop() {
        super.onStop();
        KLog.d("onStop : " + wallpagerId);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();

            //关键语句
            mediaPlayer.reset();

            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    /** ---------  动态  结束 -------------*/



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
                        diffReport("1");
                        break;
                    case 1:
                        KLog.d("侵犯版权");
                        diffReport("2");
                        break;
                }
            }
        });
        reportAlertDialog.show();
    }


    /** 记录用户举报 */
    private void diffReport(String type) {
        mPresenter.getReportData(wallpagerId,type);
    }


    /** 记录用户下载，分享，收藏 */
    private void diffRecod(String type) {
        mPresenter.getRecordData(wallpagerId,type);
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

                diffRecod("3");

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


    private void setWallpaper() {
        List<String> temps = new ArrayList<>();
        temps.add("桌面壁纸");
        temps.add("锁屏壁纸");
        temps.add("取消");

        PreviewAlertDialog previewAlertDialog =  new PreviewAlertDialog(mContext)
                .builder()
                .setPreviewBeanList(temps);
        previewAlertDialog.setOnDialogItemClickListener(new PreviewAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("桌面壁纸");
                        setDesktopWallpaper();
                        break;
                    case 1:
                        KLog.d("锁屏壁纸");
                        setLockScreenWallpaper();
                        break;
                }
            }
        });
        previewAlertDialog.show();
    }

    //设置桌面壁纸 静态 + 动态
    private void setDesktopWallpaper() {

        if(type.equals(AppConstant.DYMATIC_WP)){
            //TODO 下载
//            downVideo();

            VideoLiveWallpaperService.setToWallPaper(getActivity());
        }else {
            Glide.with(this).load(mAdBean.getImg_url())
                    .asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    try {
                        //壁纸管理器
                        wpManager = WallpaperManager.getInstance(MyApplication.getInstance());
                        wpManager.setBitmap(resource);
                        Toast.makeText(getActivity(), "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    /** 检查 创建文件 */
    private String getOutputImagePath() {

        String preName = ".png";//后缀名
        if(type.equals(AppConstant.DYMATIC_WP)){
            preName = ".mp4";
        }else if(type.equals(AppConstant.COMMON_WP)){
            preName = ".png";
        }

        //创建File对象，用于存储下载后的照片
        File dir = new File(SDCardHelper.getSDCardBaseDir(),"BSLWallpaper");
        File outputImage = null;
        try {
            if (!dir.exists()){
                dir.mkdir();
            }

            outputImage = new File(dir,System.currentTimeMillis() + preName);

            if(!outputImage.exists()){
                outputImage.createNewFile();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        return outputImage.getAbsolutePath();
    }

    /** 下载视频 */
    private void downVideo() {
        String destinationUri = getOutputImagePath();
        KLog.d("保存的地址：" + destinationUri);

        /** 委托DownManager 去下载 */
        DownManager downManager = new DownManager(MyApplication.getInstance(),dynamicUrl,destinationUri);
        downManager.downloadApk();

    }

    //设置锁屏壁纸
    @SuppressLint("NewApi")
    private void setLockScreenWallpaper() {
//        Glide.with(this).load(mAdBean.getImg_url())
//                .asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                try {
//                    //获取类名
//                    Class class1 = wpManager.getClass();
//                    //获取设置锁屏壁纸的函数
//                    Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);
//                    //调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
//                    setWallPaperMethod.invoke(wpManager, resource);
//                    Toast.makeText(getActivity(), "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
//                } catch (Throwable e) {
//                    Toast.makeText(getActivity(), "锁屏壁纸设置失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        try {
//            WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getActivity());
//            if (mWallpaperManager != null) {
//                mWallpaperManager.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.img_video_1), null, true,
//                        WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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

                        break;
                    case 1:
                        KLog.d("锁屏预览");

                        break;
                }
            }
        });
        previewAlertDialog.show();
    }


    /** ------------ 接口回调 ------------ */

    @Override
    public void showError(String msg) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showReportData() {
        KLog.d("用户举报");
    }

    @Override
    public void showRecordData() {
        KLog.d("用户下载");
    }

    @Override
    public void showData(AdBean adBean) {
        KLog.d("获取的高清图: " + adBean.getImg_url());

       mAdBean = adBean;

        if(type.equals(AppConstant.COMMON_WP)){
            dynamicUrl = mAdBean.getImg_url();
            mAuthorName.setText(mAdBean.getNickname());

        }else if(type.equals(AppConstant.DYMATIC_WP)){
            dynamicUrl = mAdBean.getMovie_url();
            if(TextUtils.isEmpty(dynamicUrl)){
                ToastHelper.customToastView(getActivity(),"视频获取失败，请重新加载");
                getActivity().finish();
                return;
            }
            SPHelper.put(getActivity(),"video",dynamicUrl);
        }

        //喜好
        if("0".equals(mAdBean.getIs_collected())){
            icon_love.setImageResource(R.mipmap.icon_unlove);
        }else{
            icon_love.setImageResource(R.mipmap.icon_love);
        }

        //标题
        title.setText(adBean.getTitle());

        //大图
        Glide.with(getActivity())
                .load(adBean.getImg_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(mImageView);

        //TODO 通过id在数据库中拿到对应的Bean
        String fromWhere = (String) SPHelper.get(getActivity(),"fromWhere","");
        WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpager(adBean.getId(),fromWhere);
        wallpagerBean.setImg_url(adBean.getImg_url());
        MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
    }



}
