package com.ngbj.wallpaper.module.app;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.Detail_Adapter;
import com.ngbj.wallpaper.base.BaesLogicActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.DetailParamBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.bean.entityBean.TestBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.AdShowDialog;
import com.ngbj.wallpaper.dialog.LoadingDialog;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.eventbus.LoveEvent;
import com.ngbj.wallpaper.eventbus.LoveHotNewEvent;
import com.ngbj.wallpaper.eventbus.LoveCreateEvent;
import com.ngbj.wallpaper.eventbus.LoveNewEvent;
import com.ngbj.wallpaper.eventbus.LoveSearchEvent;
import com.ngbj.wallpaper.eventbus.LoveUploadWorksEvent;
import com.ngbj.wallpaper.eventbus.fragment.LoveSpecialEvent;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.ngbj.wallpaper.service.VideoLiveWallpaperService;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.ScreenHepler;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.downfile.DownManager;
import com.ngbj.wallpaper.utils.widget.OnViewPagerListener;
import com.ngbj.wallpaper.utils.widget.ViewPagerLayoutManager;
import com.sigmob.windad.Drift.WindDriftAdListener;
import com.sigmob.windad.WindAdError;
import com.sigmob.windad.WindAdOptions;
import com.sigmob.windad.WindAdRequest;
import com.sigmob.windad.WindAds;
import com.sigmob.windad.rewardedVideo.WindRewardInfo;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAdListener;
import com.socks.library.KLog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/***
 * 总结：
 * 1.先adpter -- 请求  -- 更新界面 + 数据  -- 数据源数据跟着改变 -- imgUrl有值，不会发送请求 -- ok
 * 2.预加载 -- ok
 * 3.预加载把数据返回给来源 -- IndexFragment
 * 4.预加载不把数据返回给来源 -- NewFragment NewHotFragment
 * Time 2018.12.29
 *
 * 加上动态的壁纸部分
 */

public class DetailActivity extends BaesLogicActivity<DetailPresenter>
        implements DetailContract.View, WindRewardedVideoAdListener,WindDriftAdListener {

    public static String TAG = "DetailActivity";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    int mPosition;//当前选择的位置
    int mPage;//当前选择的页数
    String mCategory = "0";//当前的分类类别
    String mOrder = "0";//当前的排序区分
    String wallpagerId;//当前选择的壁纸ID
    String fromWhere;//从哪里点击
    String hotSearchTag;//热词
    String navigation;//导航
    String keyWord;//关键词
    int searchType;//搜索类型
    int mSize;//当前壁纸数量,用于加载更多时判断依据
    DetailParamBean mDetailParamBean;//临时参数


    Detail_Adapter mDetail_adapter;//适配器
    ViewPagerLayoutManager mLayoutManager;
    List<WallpagerBean> mTemps = new ArrayList<>();//当前界面临时数据

    String type;//区分 静态 动态
    String dynamicUrl;//图片的地址 视频的地址态的标志
    WallpagerBean mWallpagerBean;//整个界面的实体
    boolean isPrepareOK;//资源是否加载完成
    boolean isDown;//是否下滑动作
    LoadingDialog dialog;//显示加载框


//    public static void openActivity(Context context, DetailParamBean bean,ArrayList<WallpagerBean> list) {
//        Intent intent = new Intent(context, DetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("bean",bean);
//        bundle.putSerializable("list",list);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }


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

        mDetailParamBean = (DetailParamBean) getIntent().getExtras().getSerializable("bean");
        mTemps = (List<WallpagerBean>) getIntent().getExtras().getSerializable("list");
        KLog.d(TAG,"数据的长度是：" + mTemps.size());
        mPosition = mDetailParamBean.getPosition();
        mPage = mDetailParamBean.getPage();
        fromWhere = mDetailParamBean.getFromWhere();
        mCategory = mDetailParamBean.getCategory();
        mOrder = mDetailParamBean.getOrder();
        keyWord = mDetailParamBean.getKeyWord();
        navigation = mDetailParamBean.getNavigation();
        hotSearchTag = mDetailParamBean.getHotSearchTag();
        searchType = mDetailParamBean.getSearchType();

        initRecycleView();  //实例化
        getDetailData();  //默认加载的数据
        isNeedGetRequest(); //网络请求
        adSetting(); //激励视频广告


    }

    private void adSetting() {

        initSDK(); //SDK 初始化
        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance(); //设置监听
        windRewardedVideoAd.setWindRewardedVideoAdListener(this);
        loadAd();   //加载
    }


    public void getDetailData() {
        mWallpagerBean = mTemps.get(mPosition);
        mSize = mTemps.size();
        mDetail_adapter.setNewData(mTemps);
        mLayoutManager.scrollToPositionWithOffset(mPosition, 0);
    }


    private void toWebView(WebView webView) {

        String loadUrl = mWallpagerBean.getLink();
//        loadUrl = "http://sle.semzyzh.com:8070/dc/redirect?m=3493894f";
        setSetting(webView);
        initClient(webView);
        initChromeClient(webView);
        webView.loadUrl(loadUrl);
    }


    public void isNeedGetRequest() {

        /** 刚进来的数据 */
        type = mWallpagerBean.getType();
        KLog.d("切换的type:  ",type);

        if(type.equals(AppConstant.COMMON_AD) || type.equals(AppConstant.API_AD) ){
            Log.e(TAG,"嘿嘿😋，我是广告 " + mWallpagerBean.getLink());
            View itemView = recyclerView.getChildAt(0);
            final WebView webView = itemView.findViewById(R.id.webview);
            final RelativeLayout part2 = itemView.findViewById(R.id.part2);
            part2.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            toWebView(webView);

            return;
        }else{
        }

        wallpagerId = mWallpagerBean.getWallpager_id();
        if(null == wallpagerId || "1".equals(wallpagerId)
                || "2".equals(wallpagerId) || "3".equals(wallpagerId)){


        }else{


            //判断大图路径是否为空
            if (TextUtils.isEmpty(mWallpagerBean.getImg_url())) {
                showDialog();
                mPresenter.getDetailData(mWallpagerBean.getWallpager_id());
                return;
            }

            //更新 事件
            updateToDesktop(mWallpagerBean.getImg_url(),mWallpagerBean.getCategory_name());



            /** 切换时加载视频 */
            if(type.equals(AppConstant.DYMATIC_WP)){

                //判断视频路径是否为空
                if(TextUtils.isEmpty(mWallpagerBean.getMovie_url())){
                    ToastHelper.customToastView(mContext,"视频获取失败，请重新加载");
                    finish();
                    return;
                }

                updateToDesktop(mWallpagerBean.getImg_url(),mWallpagerBean.getCategory_name());

            }
        }
    }




    /** 请求后操作 -- 更新界面 */
    private void updateToDesktop(String imgUrl,String categoryName) {

        View itemView = recyclerView.getChildAt(0);
        final ImageView imgAll = itemView.findViewById(R.id.img_all);//大图
        final TextView tag = itemView.findViewById(R.id.image_tag);//类别

        //大图
        Glide.with(MyApplication.getInstance())
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgAll);

        //分类
        tag.setText(TextUtils.isEmpty(categoryName)
                ? "#卡通动漫#"
                :"#" + categoryName +"#");


        playVideoTest(itemView);
    }



    private void playVideoTest(View itemView ) {

            final ImageView imgAll = itemView.findViewById(R.id.img_all);
            final RelativeLayout topPart = itemView.findViewById(R.id.top_part);//头部
            final ImageView back = itemView.findViewById(R.id.back);//返回
            final ImageView report = itemView.findViewById(R.id.report);//举报
            final LinearLayout down = itemView.findViewById(R.id.down);//下载
            final ConstraintLayout bottomPart = itemView.findViewById(R.id.bottom_part);//底部
            final ImageView iconSave = itemView.findViewById(R.id.icon_save);//设值壁纸
            final TextView tag = itemView.findViewById(R.id.image_tag);//类别
            final ImageView iconShare = itemView.findViewById(R.id.icon_share);//分享
            final ImageView iconLove = itemView.findViewById(R.id.icon_love);//喜好
            final ImageView iconPreview = itemView.findViewById(R.id.icon_preview);//预览
            final ImageView deskPreview = itemView.findViewById(R.id.desk_preview);//桌面预览
            final ImageView lockPreview = itemView.findViewById(R.id.lock_preview);//锁屏预览
            final WebView webView = itemView.findViewById(R.id.webview);//webview
            final RelativeLayout part2 = itemView.findViewById(R.id.part2);//整体布局
            part2.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);


            //大图的事件
            imgAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "imgAll onClick");

                    if(deskPreview.getVisibility() == View.VISIBLE){
                        deskPreview.setVisibility(View.GONE);
                    }

                    if(lockPreview.getVisibility() == View.VISIBLE){
                        lockPreview.setVisibility(View.GONE);
                    }

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

                int count = getDownCount();
                if(count != 3){
                    downAndRecord();
                    queryAndUpdate();
                    return;
                }

                showAdDialog();

                }
            });

            //分类的事件
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name  = mWallpagerBean.getCategory_name();
                    String category = mWallpagerBean.getCategory_id();

//                    CategoryNewHotActivity.openActivity(mContext,category,name);

                    Intent intent = new Intent(mContext,CategoryNewHotActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("category",category);
                    bundle.putString("keyword",name);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);


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

            //TODO 2018.12.26 壁纸喜好逻辑
            //喜好的事件
            iconLove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("0".equals(mWallpagerBean.getIs_collected())) {
                        mWallpagerBean.setIs_collected("1");
                        ToastHelper.customToastView(mContext, "收藏成功");
                        iconLove.setImageResource(R.mipmap.icon_love);
                        diffRecod("2");
                        updateLove(true);
                    } else {
                        mWallpagerBean.setIs_collected("0");
                        ToastHelper.customToastView(mContext, "取消收藏");
                        iconLove.setImageResource(R.mipmap.icon_unlove);
                        mPresenter.getDeleteCollection(mWallpagerBean.getWallpager_id());
                        updateLove(false);
                    }
                }
            });

            //预览的事件
            iconPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previewImage(deskPreview,lockPreview,topPart,bottomPart);
                }
            });
    }


    /** ================== 适配器  开始==================== */

     private void initRecycleView() {
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        mDetail_adapter = new Detail_Adapter(mTemps);//设置Adapter
        recyclerView.setAdapter(mDetail_adapter);
        mDetail_adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //一行代码开启动画 默认CUSTOM动画
    }

    /** ================== 适配器 ==================== */


    @Override
    protected void initEvent() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 ){//dy>0 表示下滑
                    onScrolledDown();
                }else
                    onScrolledUp();
            }
        });


        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                Log.e(TAG, "第一次落地页会加载 ： onInitComplete ");
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
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);

                   //TODO 2018.12.16 晚 当滑到倒数第二个时，加载下一页 只有此下方才加载下一页
                    if( isDown && position == mSize - 3){
                        ++mPage;

                        if(fromWhere.equals(AppConstant.INDEX)){//主界面
                            mPresenter.getIndexRecommendData(mPage);

                        }else if(fromWhere.equals(AppConstant.CATEGORY_NEW)){//分类子项
                            mPresenter.getMoreRecommendData(mPage,mCategory,mOrder);

                        }else if(fromWhere.equals(AppConstant.SEARCH)){

                            if(searchType == AppConstant.FROMINDEX_NAVICATION){
                                mPresenter.getMoreNavigationData(mPage,navigation);
                            }else if(searchType == AppConstant.FROMINDEX_HOTSEACHER){
                                mPresenter.getMoreHotSearchData(mPage,hotSearchTag);
                            }else if(searchType == AppConstant.FROMINDEX_SEACHER){
                                mPresenter.getMoreKeySearchData(mPage,keyWord);
                            }

                        }else if(fromWhere.equals(AppConstant.CATEGORY_NEW_HOT)){//分类最热/最新
                            mPresenter.getMoreRecommendData(mPage,mCategory,mOrder);
                        }
                    }

                   mPosition = position;
                   //TODO 切换时重置Bean -- 2018.12.29
                   mWallpagerBean = mTemps.get(position);
                  isNeedGetRequest();
            }

        });
    }

    private void onScrolledDown() {
        isDown = true;
    }

    private void onScrolledUp() {
        isDown = false;
    }


    private void downAndRecord() {
        diffRecod("1");
        downVideo(0);
    }


    /** 1.发送事件  2.改变数据库所有壁纸的喜好 */
    private void updateLove(boolean isLove) {
        List<WallpagerBean> wallpagerBeanList = MyApplication.getDbManager().queryDifferWPId(wallpagerId);
        for (WallpagerBean wallpagerBean: wallpagerBeanList) {
            if(isLove){
                wallpagerBean.setIs_collected("1");
            }else
                wallpagerBean.setIs_collected("0");

            MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
        }

        //TODO 根据不同的来源发送事件
        if(fromWhere.equals(AppConstant.INDEX)){//首页
            EventBus.getDefault().post(new LoveEvent(mPosition,isLove));
        }else if(fromWhere.equals(AppConstant.SEARCH)){//搜索页
            EventBus.getDefault().post(new LoveSearchEvent(mPosition,isLove));
        }else if(fromWhere.equals(AppConstant.CATEGORY_NEW)){//分类子页
            EventBus.getDefault().post(new LoveNewEvent(mPosition,isLove));
        }else if(fromWhere.equals(AppConstant.CATEGORY_NEW_HOT)){//最新最热
            EventBus.getDefault().post(new LoveHotNewEvent(mPosition,isLove));

        }else if(fromWhere.equals(AppConstant.MY_1)){//下载 收藏 分享
//            EventBus.getDefault().post(new LoveCreateEvent(mPosition,wallpagerId));
            EventBus.getDefault().post(new LoveCreateEvent(mPosition,isLove));
        }else if(fromWhere.equals(AppConstant.MY_UPLOAD_WORKD)){//创作
            EventBus.getDefault().post(new LoveUploadWorksEvent(mPosition,isLove));
        }else if(fromWhere.equals(AppConstant.SPECIAL)){//专题页

            EventBus.getDefault().post(new LoveSpecialEvent(mPosition,isLove));
        }


    }

    private void previewImage(final ImageView imageView, final ImageView lockView,
                              final RelativeLayout topPart, final ConstraintLayout bottomPart) {
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
                        imageView.setVisibility(View.VISIBLE);
                        topPart.setVisibility(View.GONE);
                        bottomPart.setVisibility(View.GONE);
                        break;
                    case 1:
                        KLog.d("锁屏预览");
                        lockView.setVisibility(View.VISIBLE);
                        topPart.setVisibility(View.GONE);
                        bottomPart.setVisibility(View.GONE);
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
        temps.add(new ShareBean(R.mipmap.qqkj_share, "QQ空间"));
//        temps.add(new ShareBean(R.mipmap.weibo_share, "微博"));

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
                        sharePic(SHARE_MEDIA.WEIXIN);
                        break;
                    case 1:
                        KLog.d("朋友圈");
                        sharePic(SHARE_MEDIA.WEIXIN_CIRCLE);
                        break;
                    case 2:
                        KLog.d("QQ");
                        sharePic(SHARE_MEDIA.QQ);
                        break;
//                    case 3:
//                        KLog.d("微博");
//                        break;
                    case 3:
                        KLog.d("QQ空间");
                        sharePic(SHARE_MEDIA.QZONE);
                        break;
                }
            }
        });
        shareAlertDialog.show();
    }

    private void sharePic(SHARE_MEDIA platform) {
        UMImage image = new UMImage(DetailActivity.this,dynamicUrl);
        UMImage thumb = new UMImage(DetailActivity.this,mWallpagerBean.getThumb_img_url());
        image.setThumb(thumb);
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        //压缩格式设置
        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色

        new ShareAction(DetailActivity.this)
                .setPlatform(platform)
                .withText("变色龙壁纸是一款非常好的壁纸应用，谁用谁知道，绝对错不了！")
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {}

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        KLog.d("当前线程: " + Thread.currentThread().getName());
                        Toast.makeText(DetailActivity.this, " 分享成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                      Toast.makeText(DetailActivity.this,  " 分享失败", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        Toast.makeText(DetailActivity.this,  " 分享取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .share();
    }



    /**
     * 下载 图片 + 视频 -- 0
     * 设置动态壁纸 -- 1
     */
    private void downVideo(final int mType) {

        final String destinationUri = getOutputImagePath();
        KLog.d("保存的地址：" + destinationUri);
        KLog.d("下载的地址：" + dynamicUrl);
        ToastHelper.customToastView(this,"正在下载...");
        showDialog();
        /** 委托DownManager 去下载 */
        DownManager downManager = new DownManager(MyApplication.getInstance(), dynamicUrl, destinationUri);
        downManager.downloadApk();
        downManager.setDownListener(new DownManager.DownListener() {
            @Override
            public void fun() {
                if(mType == 1){

                     MyApplication.getDbManager().deleteAllTestBean();

                      TestBean testBean = new TestBean();
                      testBean.setUrl(destinationUri);
                      MyApplication.getDbManager().insertTestBean(testBean);
//                     VideoLiveWallpaperService.startLiveWallpaperPrevivew(DetailActivity.this);

                    Intent localIntent = new Intent();
                    localIntent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);//android.service.wallpaper.CHANGE_LIVE_WALLPAPER
                    localIntent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT
                            , new ComponentName(DetailActivity.this.getApplicationContext().getPackageName()
                                    ,VideoLiveWallpaperService.class.getCanonicalName()));
                    startActivity(localIntent);
                }

                if(null != dialog)dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                KLog.d(TAG,"动态壁纸设置成功");
            }

        }
    }

    private void showAdDialog() {
        final AdShowDialog adShowDialog = new AdShowDialog(this).builder();
        adShowDialog.setPositionButton("看视频解锁下载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //激励广告播放
                playAd();
            }
        }).setCanceledOnTouchOutside(true);
        adShowDialog.show();
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


    //显示Dialog
    private void showDialog(){
        LoadingDialog.Builder builder1 = new LoadingDialog.Builder(mContext)
                .setCancelable(true);
        dialog = builder1.create();
        dialog.show();
    }


    /**
     * 记录用户举报
     */
    private void diffReport(String type) {


        showDialog();

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
                        SetLockWallPaper();
                        break;
                }
            }
        });
        previewAlertDialog.show();
    }


    //设置桌面壁纸 静态 + 动态
    private void setDesktopWallpaper() {

        if (mWallpagerBean.getType().equals(AppConstant.DYMATIC_WP)) {
            //TODO 先判断是否已下载，没有下载的话先下载
            diffRecod("1");
            downVideo(1);
//                    String string = (String) SPHelper.get(DetailActivity.this,"video","");
//                    KLog.d("string:" ,string);
//            VideoLiveWallpaperService.startLiveWallpaperPrevivew(DetailActivity.this);
        } else {
             showDialog();
             fun2();
        }
    }

    private void fun2() {

        final int screenWidth = ScreenHepler.getScreenWidth(this);
        final int screenHeight = ScreenHepler.getScreenHeight(this);

        Glide.with(MyApplication.getInstance())
                .load(mWallpagerBean.getImg_url())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            //壁纸管理器
                            WallpaperManager wpManager = WallpaperManager.getInstance(MyApplication.getInstance());
                            resource = imageCropper(resource);
                            wpManager.suggestDesiredDimensions(screenWidth,screenHeight);
                            wpManager.setBitmap(resource);
                            Toast.makeText(DetailActivity.this, "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException e) {
                            Toast.makeText(DetailActivity.this, "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
            }
        });
    }


    /** 设置为壁纸的图片应该填充满整个屏幕，所以需要先剪裁 */
    private Bitmap imageCropper(Bitmap bitMap){
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        KLog.d("图片的宽高：" + bitMap.getWidth() + "  " + bitMap.getHeight());
        // 设置想要的大小
        int newWidth = ScreenHepler.getScreenWidth(this);
        int newHeight = ScreenHepler.getScreenHeight(this);
        KLog.d("屏幕的宽高：" + newWidth + "  " + newHeight);

        /** 1 给予的图片尺寸 宽高【竖屏】 都小于 尺寸的屏幕 -- 直接给予图片的大小 -- ok */
        if(width <= newWidth && height <= newHeight){
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height);
            KLog.d("imageCropper() newWidth"+bitMap.getWidth());
            KLog.d(("imageCropper() newHeight"+bitMap.getHeight()));
            return bitMap;
        }

        /** 2. 给予的图片尺寸 宽高【横屏】 都小于 尺寸的屏幕 -- 没考虑 */


        /** 3. 给予的图片尺寸 宽高【竖屏】 都大于 尺寸的屏幕 -- ok */
        if(width >= newWidth && height >= newHeight){
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            KLog.d("scaleWidth: " + scaleWidth + "scaleHeight: " + scaleHeight );
            //如果高度的缩放比 大于 宽度的缩放比，按小的缩放
            if(scaleHeight > scaleWidth ){
                    scaleHeight = scaleWidth;
            }else if(scaleWidth < scaleHeight){
                    scaleWidth = scaleHeight;
            }

            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
            KLog.d("imageCropper() newWidth"+bitMap.getWidth());
            KLog.d(("imageCropper() newHeight"+bitMap.getHeight()));
            return bitMap;

        }


        /** 宽 或 高 大于 图片宽高  -- 希望是高 > 宽 的图片 */
        if(width >= newWidth || height >= newHeight){
            // 得到新的图片
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height);
            KLog.d("imageCropper() newWidth"+bitMap.getWidth());
            KLog.d(("imageCropper() newHeight"+bitMap.getHeight()));
            return bitMap;
        }

        return null;

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


    //在flyme系统下才有这个方法
    private void SetLockWallPaper() {

        Glide.with(MyApplication.getInstance())
                .load(mWallpagerBean.getImg_url())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        try {
                            resource = imageCropper(resource);
                            WallpaperManager mWallManager = WallpaperManager.getInstance(MyApplication.getInstance());
                            Class class1 = mWallManager.getClass();//获取类名
                            Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper",Bitmap.class);//获取设置锁屏壁纸的函数
                            setWallPaperMethod.invoke(mWallManager, resource);//调用锁屏壁纸的函数,并指定壁纸的路径imageFilesPath
                            Toast.makeText(DetailActivity.this, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
                        } catch (Throwable e) { // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void releaseVideo(int index) {
        View itemView = recyclerView.getChildAt(index);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgAll = itemView.findViewById(R.id.img_all);
        final WebView webView = itemView.findViewById(R.id.webview);//webview
        final RelativeLayout part2 = itemView.findViewById(R.id.part2);//整体布局
        part2.setVisibility(View.VISIBLE);
        webView.setVisibility(View.GONE);
        imgThumb.animate().alpha(1).start();
        imgAll.animate().alpha(1).start();

        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
    }


    /** =================== 接口方法回调  开始 =================== */

    @Override
    public void showIndexRecommendData(List<MulAdBean> list) {
            commonLogic(list);
    }

    private void commonLogic(List<MulAdBean> list) {

        mTemps.addAll(updateData(list));//转化，累加
        mSize = mTemps.size();//转化
        mDetail_adapter.notifyItemRangeChanged(mPosition + 1,list.size());//更新

        //TODO 发送事件给主界面
        if(fromWhere.equals(AppConstant.INDEX)){
            EventBus.getDefault().post(new LoveEvent(mPosition, mPage,true,list));
        }else if(fromWhere.equals(AppConstant.SEARCH)){

        }

    }


    /** 返回的有效数据是 img_url move_url category  -- 构建一个完整的Wallpaper实体*/
    @Override
    public void showDetailData(AdBean adBean) {
        KLog.d("获取的高清图: " + adBean.getImg_url());
        updateToDesktop(adBean.getImg_url(),adBean.getCategory().get(0).getName());
        mWallpagerBean = transformBean(adBean);
        dynamicUrl = type.equals(AppConstant.COMMON_WP) ? mWallpagerBean.getImg_url() : mWallpagerBean.getMovie_url();
    }

    private WallpagerBean transformBean(AdBean adBean) {
        mWallpagerBean.setMovie_url(adBean.getMovie_url());
        mWallpagerBean.setImg_url(adBean.getImg_url());
        mWallpagerBean.setCategory_id(adBean.getCategory().get(0).getId());
        mWallpagerBean.setCategory_name(adBean.getCategory().get(0).getName());
        return mWallpagerBean;
    }





    @Override
    public void showMoreRecommendData(List<MulAdBean> list) {
        commonLogic(list);
    }

    /** 搜索 回调  统一走一个方法 */
    @Override
    public void showMoreKeySearchData(List<MulAdBean> list) {
        KLog.d("加载更多数据：" + list.size());
        List<WallpagerBean> list1 = updateData(list);
        for (int i = 0; i < list.size(); i++) {
            KLog.d("wallpagerId:" + list1.get(i).getWallpager_id());
        }


//        EventBus.getDefault().post(new LoveSearchEvent(mPosition, mPage,true,list));
        //尾加
        mTemps.addAll(list1);

//        mWallpagerBeanList.addAll(list1);
//
//        mDetail_adapter.notifyDataSetChanged();
//
//        mSize = mWallpagerBeanList.size();

    }

    @Override
    public void showMoreNavigationData(List<MulAdBean> list) {
        showMoreKeySearchData(list);
    }

    @Override
    public void showMoreHotSearchData(List<MulAdBean> list) {
        showMoreKeySearchData(list);
    }




    @Override
    public void showReportData() {
        KLog.d("用户举报");
        ToastHelper.customToastView(getApplicationContext(),"举报成功");
    }

    @Override
    public void showRecordData() {
        KLog.d("用户下载");
    }

    /** 取消收藏 */
    @Override
    public void showDeleteCollection() {
        KLog.d("what the fucking thing");
    }


    @Override
    public void showError(String msg) {
        if(null != dialog)dialog.dismiss();
    }

    @Override
    public void complete() {
        if(null != dialog)dialog.dismiss();
    }

    /**
     * =================== 接口方法回调  结束 ===================
     */


    @Override
    protected void onPause() {
        super.onPause();
//        View itemView = recyclerView.getChildAt(0);
//        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//        imgThumb.animate().alpha(1).start();
//        imgThumb.animate().alpha(1).start();
    }


    /** 构建新的Bean */
    private List<WallpagerBean> updateData(final List<MulAdBean> recommendList){
         List<WallpagerBean> list = new ArrayList<>();
            WallpagerBean wallpagerBean;
            AdBean adBean ;
            ApiAdBean apiAdBean;
            for (MulAdBean bean: recommendList) {
                if(bean.getItemType() == MulAdBean.TYPE_ONE){
                    adBean = bean.adBean;
                    wallpagerBean = new WallpagerBean();
                    wallpagerBean.setFromWhere(fromWhere);
                    wallpagerBean.setType(adBean.getType());
                    wallpagerBean.setIs_collected(adBean.getIs_collected());
                    wallpagerBean.setMovie_url(adBean.getMovie_url());
                    wallpagerBean.setWallpager_id(adBean.getId());
                    wallpagerBean.setNickname(adBean.getNickname());
                    wallpagerBean.setTitle(adBean.getTitle());
                    wallpagerBean.setHead_img(adBean.getHead_img());
                    wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());

                    list.add(wallpagerBean);
                }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                    apiAdBean = bean.apiAdBean;
                    wallpagerBean = new WallpagerBean();
                    wallpagerBean.setFromWhere(fromWhere);
                    wallpagerBean.setType(apiAdBean.getType());
                    list.add(wallpagerBean);
                }
            }
         return list;
    }



    /**
     * =================== 广告  开始 ===================
     */

    private WindAdRequest request;

    private void initSDK() {
        WindAds ads = WindAds.sharedAds();
        ads.setDebugEnable(true);  //enable or disable debug log
        String appId = AppConstant.APPID;
        String appKey = AppConstant.APPKEY;

        ads.startWithOptions(this, new WindAdOptions(appId, appKey));

    }


    private void loadAd() {

        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();
        String placementId = AppConstant.PLACEMENTID;
        String userId = "-1";
        request = new WindAdRequest(placementId,userId,null);
        windRewardedVideoAd.loadAd(request);

    }


    private void playAd() {
        WindRewardedVideoAd windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();

        String placementId = AppConstant.PLACEMENTID;

        try {
            if(windRewardedVideoAd.isReady(placementId)){
                windRewardedVideoAd.show(this,request);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDriftAdLoadSuccess(String s) {

    }

    @Override
    public void onDriftAdError(WindAdError windAdError, String s) {

    }

    @Override
    public void onDriftAdExposured(String s) {

    }

    @Override
    public void onDriftAdClosed(String s) {

    }

    @Override
    public void onDriftAdViewClosed(String s) {

    }

    @Override
    public void onVideoAdLoadSuccess(String s) {
//        Toast.makeText(this, "激励视频广告加载成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoAdPlayStart(String s) {
//        Toast.makeText(this, "激励视频广告播放开始", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoAdClicked(String s) {
//        Toast.makeText(this, "激励视频广告CTA点击事件监听", Toast.LENGTH_SHORT).show();
    }

    //WindRewardInfo中isComplete方法返回是否完整播放
    @Override
    public void onVideoAdClosed(WindRewardInfo windRewardInfo, String placementId) {
        if(windRewardInfo.isComplete()){
//            Toast.makeText(mContext, "激励视频广告完整播放，给予奖励", Toast.LENGTH_SHORT).show();
            downAndRecord();
        }else{
//            Toast.makeText(mContext, "激励视频广告关闭", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 加载广告错误回调
     * WindAdError 激励视频错误内容
     * placementId 广告位
     */
    @Override
    public void onVideoAdLoadError(WindAdError windAdError, String placementId) {
        Toast.makeText(mContext, "激励视频广告错误" + windAdError, Toast.LENGTH_SHORT).show();
        KLog.d( "onVideoError() called with: error = [" + windAdError + "], placementId = [" + placementId + "]");
    }

    /**
     * 播放错误回调
     * WindAdError 激励视频错误内容
     * placementId 广告位
     */
    @Override
    public void onVideoAdPlayError(WindAdError windAdError, String placementId) {
        Toast.makeText(mContext, "激励视频广告错误" + windAdError, Toast.LENGTH_SHORT).show();
        KLog.d( "onVideoError() called with: error = [" + windAdError + "], placementId = [" + placementId + "]");
    }


    /**
     * =================== 广告  结束 ===================
     */






    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();//防止内存泄漏
        WindRewardedVideoAd.sharedInstance().setWindRewardedVideoAdListener(null);
        mTemps.clear();//防止内存泄漏
        mTemps = null;
    }


    /** ------------------------------------ 跳转到广告页  ------------------------------------



    /**
     * webview 默认设置
     */

    WebSettings webSettings;

    private void setSetting(WebView webview) {
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setSupportZoom(true); //支持屏幕缩放
        webSettings.setBuiltInZoomControls(true);
        //设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)
        webview.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webview.getSettings().setBlockNetworkImage(false); // 解决图片不显示
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);//设置适应Html5

        //其他细节操作 定位一些设置
        webSettings.setDatabaseEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setGeolocationEnabled(true);


        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为,
            // 在这种模式下,WebView将允许一个安全的起源从其他来源加载内容，即使那是不安全的.
            // 如果app需要安全性比较高，不应该设置此模式
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决app中部分页面非https导致的问题
        }

    }


    private void initClient(WebView webView) {
        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//在这里设置对应的操作
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受所有网站的证书
            }

        });
    }


    private void initChromeClient(WebView webView) {
        //获取网页进度
        webView.setWebChromeClient(new WebChromeClient() {

            /** 定位回调 */
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });
    }

}
