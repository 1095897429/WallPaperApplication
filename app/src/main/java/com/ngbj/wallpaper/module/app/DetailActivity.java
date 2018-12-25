package com.ngbj.wallpaper.module.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.app.Detail_Adapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.ngbj.wallpaper.service.VideoLiveWallpaperService;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ScreenHepler;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.downfile.DownManager;
import com.ngbj.wallpaper.utils.widget.OnViewPagerListener;
import com.ngbj.wallpaper.utils.widget.ViewPagerLayoutManager;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DetailActivity extends BaseActivity<DetailPresenter>
        implements DetailContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    int mPosition;//当前选择的位置
    String wallpagerId;//当前选择的壁纸ID
    String fromWhere;//从哪里点击
    String type;//区分 静态 动态
    String dynamicUrl;//图片的地址 视频的地址态的标志
    WallpagerBean mWallpagerBean;//整个界面的实体


    /**
     * position -- 点击的位置   wallpagerId -- 壁纸唯一的索引
     */
    public static void openActivity(Context context, int position, String wallpagerId, String fromWhere) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("wallpagerId", wallpagerId);
        bundle.putString("fromWhere", fromWhere);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


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
        mPosition = getIntent().getExtras().getInt("position");
        KLog.d("当前选择的位置为：" + mPosition);
        wallpagerId = getIntent().getExtras().getString("wallpagerId");
        KLog.d("初始选择的 wallpagerId: " + wallpagerId);

        fromWhere = getIntent().getExtras().getString("fromWhere");
        KLog.d("从哪里点击,并存入数据库中：" + fromWhere);
        SPHelper.put(this, "fromWhere", fromWhere);

        //实例化
        initRecycleView();
        //加载数据库数据
        getDetailData();
        //是否需要请求
        isNeedGetRequest();

    }


    /**
     * 数据库数据
     */
    public void getDetailData() {
        List<WallpagerBean> historyBeanList = MyApplication.getDbManager().queryDifferCome(fromWhere);
        mWallpagerBeanList.addAll(historyBeanList);
        mDetail_adapter.setNewData(mWallpagerBeanList);
        mLayoutManager.scrollToPositionWithOffset(mPosition, 0);
    }


    public void isNeedGetRequest() {

        /** 根据id 获取 实体Bean -- 初始化数据 */
        mWallpagerBean = MyApplication.getDbManager().queryWallpager(wallpagerId, fromWhere);
        if (TextUtils.isEmpty(mWallpagerBean.getImg_url())) {
            mPresenter.getData(mWallpagerBean.getWallpager_id());
            return;
        }
    }


    @Override
    public void showData(AdBean adBean) {
        KLog.d("获取的高清图: " + adBean.getImg_url());

        updateToSql(adBean);

        updateToDesktop();

    }

    /**
     * 更新操作 -- 界面
     */
    private void updateToDesktop() {

        View itemView = recyclerView.getChildAt(0);
        final ImageView imgAll = itemView.findViewById(R.id.img_all);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final TextView title = itemView.findViewById(R.id.image_title);//标题
        final ImageView autherIcon = itemView.findViewById(R.id.author_icon);//头像
        final TextView autherName = itemView.findViewById(R.id.author_name);//作者

        //设值
        if (mWallpagerBean != null) {
            title.setText(mWallpagerBean.getTitle() == null ? "标题" : mWallpagerBean.getTitle());
            autherName.setText(mWallpagerBean.getNickname() == null ? "Mask" : mWallpagerBean.getNickname());

            //头像
            if (!TextUtils.isEmpty(mWallpagerBean.getHead_img())) {
                Glide.with(MyApplication.getInstance())
                        .load(mWallpagerBean.getHead_img())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(autherIcon);
            } else {
                autherIcon.setImageResource(R.mipmap.author_head);
            }

        }

        if (mWallpagerBean.getType().equals(AppConstant.COMMON_WP)) {
           Glide.with(MyApplication.getInstance())
                    .load(mWallpagerBean.getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imgAll);

//            imgThumb.animate().alpha(0).setDuration(200).start();//渐变消失
        }


    }

    /**
     * 更新操作  -- sql
     */
    private void updateToSql(AdBean adBean) {
        WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpager(wallpagerId, fromWhere);
        wallpagerBean.setMovie_url(adBean.getMovie_url());
        wallpagerBean.setImg_url(adBean.getImg_url());
        MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
        //TODO 重新加载
        mWallpagerBean = MyApplication.getDbManager().queryWallpager(wallpagerId, fromWhere);
        type = mWallpagerBean.getType();
        if (type.equals(AppConstant.COMMON_WP)) {
            dynamicUrl = mWallpagerBean.getImg_url();
        } else if (type.equals(AppConstant.DYMATIC_WP)) {
            dynamicUrl = mWallpagerBean.getMovie_url();
        }
    }


    /**
     * ================== 适配器  开始====================
     */

    Detail_Adapter mDetail_adapter;
    List<WallpagerBean> mWallpagerBeanList = new ArrayList<>();

    private ViewPagerLayoutManager mLayoutManager;


    private void initRecycleView() {
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //设置Adapter
        mDetail_adapter = new Detail_Adapter(mWallpagerBeanList);
        recyclerView.setAdapter(mDetail_adapter);

        //一行代码开启动画 默认CUSTOM动画
        mDetail_adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

    }

    /**
     * ================== 适配器 ====================
     */


    @Override
    protected void initEvent() {
        //TODO 会默认加载下一项，可能为了释放资源时判断
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                Log.e("TAG", "onInitComplete ");

                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e("TAG", "释放位置:" + position + " 下一页:" + isNext);
                int index;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e("TAG", "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                //TODO 切换时重新设值
                mPosition = position;
                wallpagerId = mWallpagerBeanList.get(position).getWallpager_id();
                //TODO 2018.12.25 api广告  wallpagerId 为 null
                KLog.d("切换选择的 wallpagerId: " + wallpagerId);
                if(!TextUtils.isEmpty(wallpagerId)){
                       isNeedGetRequest();
                        playVideo(0);
                }else{
                    KLog.e("我是api广告  我的wallpagerId 为 null   ");
                }
            

            }

        });
    }


    /**
     * 此方法会在具体的界面加载完成后加载
     */
    private void playVideo(int position) {
        View itemView = recyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgAll = itemView.findViewById(R.id.img_all);
        final RelativeLayout topPart = itemView.findViewById(R.id.top_part);//头部
        final ImageView back = itemView.findViewById(R.id.back);//返回
        final ImageView report = itemView.findViewById(R.id.report);//举报
        final TextView down = itemView.findViewById(R.id.down);//下载
        final ConstraintLayout bottomPart = itemView.findViewById(R.id.bottom_part);//底部
        final ImageView iconSave = itemView.findViewById(R.id.icon_save);//设值壁纸
        final TextView title = itemView.findViewById(R.id.image_title);//标题
        final ImageView autherIcon = itemView.findViewById(R.id.author_icon);//头像
        final TextView autherName = itemView.findViewById(R.id.author_name);//作者
        final ImageView iconShare = itemView.findViewById(R.id.icon_share);//分享
        final ImageView iconLove = itemView.findViewById(R.id.icon_love);//喜好
        final ImageView iconPreview = itemView.findViewById(R.id.icon_preview);//预览

        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];

        // TODO 界面加载完成后调用
        if (mWallpagerBean != null) {

            if (mWallpagerBean.getType().equals(AppConstant.COMMON_WP)) {
                //大图
                if (!TextUtils.isEmpty(mWallpagerBean.getImg_url())) {
                    Glide.with(MyApplication.getInstance())
                            .load(mWallpagerBean.getImg_url())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .crossFade()
                            .into(imgAll);
                }
                //缩略图
//                imgThumb.animate().alpha(0).setDuration(200).start();//渐变消失
            }


            //其他
            title.setText(mWallpagerBean.getTitle() == null ? "标题" : mWallpagerBean.getTitle());
            autherName.setText(mWallpagerBean.getNickname() == null ? "Mask" : mWallpagerBean.getNickname());
            //头像
            if (!TextUtils.isEmpty(mWallpagerBean.getHead_img())) {
              Glide.with(MyApplication.getInstance())
                        .load(mWallpagerBean.getHead_img())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .crossFade()
                        .into(autherIcon);
            } else {
                autherIcon.setImageResource(R.mipmap.author_head);
            }
        }


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                KLog.d("DetailActivity: ", "onPrepared 视频资源已准备好~~~~");

            }
        });


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();//隐藏缩略图
                return false;
            }
        });


        //长按事件
        imgThumb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("ddd", "长按事件");
                videoView.start();
                return false;
            }
        });

        //播放的事件
        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });

        //大图的事件
        imgAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DetailActivity：", "imgAll onClick");
                if (topPart.getVisibility() == View.VISIBLE) {
                    topPart.setVisibility(View.GONE);
                } else
                    topPart.setVisibility(View.VISIBLE);

                if (bottomPart.getVisibility() == View.VISIBLE) {
                    bottomPart.setVisibility(View.GONE);
                } else
                    bottomPart.setVisibility(View.VISIBLE);
            }
        });

        //返回的事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //举报的事件
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport();
            }
        });

        //下载的事件
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVideo();
            }
        });


        //壁纸的事件
        iconSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });


        //分享的事件
        iconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });

        //喜好的事件
        iconLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(mWallpagerBean.getIs_collected())) {
                    mWallpagerBean.setIs_collected("1");
                    ToastHelper.customToastView(mContext, "收藏成功");
                    iconLove.setImageResource(R.mipmap.icon_love);
                } else {
                    mWallpagerBean.setIs_collected("0");
                    ToastHelper.customToastView(mContext, "取消收藏");
                    iconLove.setImageResource(R.mipmap.icon_unlove);
                }


                diffRecod("2");
            }
        });

        //预览的事件
        iconPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage();
            }
        });
    }

    private void previewImage() {
        List<String> temps = new ArrayList<>();
        temps.add("桌面预览");
        temps.add("锁屏预览");
        temps.add("取消");

        PreviewAlertDialog previewAlertDialog = new PreviewAlertDialog(mContext)
                .builder()
                .setPreviewBeanList(temps);
        previewAlertDialog.setOnDialogItemClickListener(new PreviewAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position) {
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


    /**
     * 记录用户下载，分享，收藏
     */
    private void diffRecod(String type) {
        mPresenter.getRecordData(wallpagerId, type);
    }

    private void shareImage() {
        List<ShareBean> temps = new ArrayList<>();
        temps.add(new ShareBean(R.mipmap.wechat_share, "微信"));
        temps.add(new ShareBean(R.mipmap.friend_share, "朋友圈"));
        temps.add(new ShareBean(R.mipmap.qq_share, "QQ"));
        temps.add(new ShareBean(R.mipmap.weibo_share, "微博"));
        temps.add(new ShareBean(R.mipmap.qqkj_share, "QQ空间"));

        ShareAlertDialog shareAlertDialog = new ShareAlertDialog(mContext)
                .builder()
                .seShareBeanList(temps);
        shareAlertDialog.setOnDialogItemClickListener(new ShareAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {

                diffRecod("3");

                switch (position) {
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


    /**
     * 下载视频
     */
    private void downVideo() {
        String destinationUri = getOutputImagePath();
        KLog.d("保存的地址：" + destinationUri);

        /** 委托DownManager 去下载 */
        DownManager downManager = new DownManager(MyApplication.getInstance(), dynamicUrl, destinationUri);
        downManager.downloadApk();

    }

    /**
     * 检查 创建文件
     */
    private String getOutputImagePath() {

        String preName = ".png";//后缀名
        if (type.equals(AppConstant.DYMATIC_WP)) {
            preName = ".mp4";
        } else if (type.equals(AppConstant.COMMON_WP)) {
            preName = ".png";
        }

        //创建File对象，用于存储下载后的照片
        File dir = new File(SDCardHelper.getSDCardBaseDir(), "BSLWallpaper");
        File outputImage = null;
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }

            outputImage = new File(dir, System.currentTimeMillis() + preName);

            if (!outputImage.exists()) {
                outputImage.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputImage.getAbsolutePath();
    }


    private void showReport() {
        List<String> temps = new ArrayList<>();
        temps.add("色情低俗");
        temps.add("侵犯版权");
        temps.add("取消");

        ReportAlertDialog reportAlertDialog = new ReportAlertDialog(mContext)
                .builder()
                .setReportBeanList(temps);
        reportAlertDialog.setOnDialogItemClickListener(new ReportAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position) {
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


    /**
     * 记录用户举报
     */
    private void diffReport(String type) {
        mPresenter.getReportData(wallpagerId, type);
    }


    private void setWallpaper() {
        List<String> temps = new ArrayList<>();
        temps.add("桌面壁纸");
        temps.add("锁屏壁纸");
        temps.add("取消");

        PreviewAlertDialog previewAlertDialog = new PreviewAlertDialog(mContext)
                .builder()
                .setPreviewBeanList(temps);
        previewAlertDialog.setOnDialogItemClickListener(new PreviewAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position) {
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

        if (mWallpagerBean.getType().equals(AppConstant.DYMATIC_WP)) {
            VideoLiveWallpaperService.setToWallPaper(this);
        } else {

            //方式一
//            fun1();
            //方式二
             fun2();

        }
    }

    private void fun2() {
        final int screenWidth = ScreenHepler.getScreenWidth(this);
        final int screenHeight = ScreenHepler.getScreenHeight(this);
        Glide.with(MyApplication.getInstance())
                .load(mWallpagerBean.getImg_url())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(screenWidth,screenHeight) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        try {
                            //壁纸管理器
                            WallpaperManager wpManager = WallpaperManager.getInstance(MyApplication.getInstance());
                            wpManager.suggestDesiredDimensions(screenWidth,screenHeight);
                            wpManager.setBitmap(resource);
                            Toast.makeText(DetailActivity.this, "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(DetailActivity.this, "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                        }
            }
        });
    }

    private void fun1() {
        Glide.with(MyApplication.getInstance()).load(mWallpagerBean.getImg_url())
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    //壁纸管理器
                    WallpaperManager wpManager = WallpaperManager.getInstance(MyApplication.getInstance());
                    wpManager.setBitmap(resource);
                    Toast.makeText(DetailActivity.this, "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(DetailActivity.this, "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //设置锁屏壁纸
    @SuppressLint("NewApi")
    private void setLockScreenWallpaper() {
        try {
            WallpaperManager mWallpaperManager = WallpaperManager.getInstance(this);
            if (mWallpaperManager != null) {
                mWallpaperManager.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.img_video_1), null, true,
                        WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void releaseVideo(int index) {
        View itemView = recyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//        final ImageView imgAll = itemView.findViewById(R.id.img_all);
//        final RelativeLayout topPart = itemView.findViewById(R.id.top_part);
//        final ConstraintLayout bottomPart = itemView.findViewById(R.id.bottom_part);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
//        imgAll.setVisibility(View.GONE);
//        topPart.animate().alpha(1f).start();
//        bottomPart.animate().alpha(1f).start();
    }


    /**
     * =================== 接口方法回调  开始 ===================
     */


    @Override
    public void showDynamicData(List<WallpagerBean> adBeanList) {
    }

    @Override
    public void showReportData() {
        KLog.d("用户举报");
    }

    @Override
    public void showRecordData() {
        KLog.d("用户下载");
    }

    /**
     * =================== 接口方法回调  结束 ===================
     */


    @Override
    protected void onPause() {
        super.onPause();
        View itemView = recyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        imgThumb.animate().alpha(1).start();
        imgThumb.animate().alpha(1).start();
    }

}
