package com.ngbj.wallpaper.module.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.mvp.contract.fragment.VpContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.VpPresenter;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.socks.library.KLog;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class VPFragment extends BaseFragment<VpPresenter>
            implements VpContract.View {


    @BindView(R.id.icon_love)
    ImageView icon_love;

    @BindView(R.id.imageView)
    ImageView mImageView;

    @BindView(R.id.part1)
    RelativeLayout part1;

    @BindView(R.id.part2)
    ConstraintLayout part2;

    private WallpagerBean mAdBean;
    private View view;
    private Context mContext;

    private WallpaperManager wpManager;
//    private Unbinder unbinder;

    String wallpagerId;

    public VPFragment(){

    }


    public static VPFragment getInstance(WallpagerBean wallpagerBean){
        VPFragment mFragment = new VPFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", wallpagerBean);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdBean = (WallpagerBean) getArguments().getSerializable("bean");
        wallpagerId = mAdBean.getWallpager_id();
        mContext = getActivity();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = LayoutInflater.from(getContext()).inflate(R.layout.activity_detail_item_1, null);
//        //初始化
//        mContext = getActivity();
//        unbinder = ButterKnife.bind(this, view);
//        //数据初始化
//        initData();
//        return view;
//    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_item_1;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new VpPresenter();
    }

    protected void initData() {
        //背景
        if(!TextUtils.isEmpty(mAdBean.getImg_url())){
            Glide.with(getActivity())
                    .load(mAdBean.getImg_url())
                    .placeholder(R.mipmap.detail_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(mImageView);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    //点击图片的事件
    @OnClick(R.id.imageView)
    public void ImageView(){
        if(part1.getVisibility() == View.VISIBLE){
            part1.setVisibility(View.GONE);
        }else
            part1.setVisibility(View.VISIBLE);

        if(part2.getVisibility() == View.VISIBLE){
            part2.setVisibility(View.GONE);
        }else
            part2.setVisibility(View.VISIBLE);
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
        diffRecod("1");
    }

    //收藏的事件
    @OnClick(R.id.icon_love)
    public void IconLove(){
        diffRecod("2");
    }







    /** ---------  方法 -------------*/

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

    //设置桌面壁纸
    private void setDesktopWallpaper() {

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

    //设置锁屏壁纸
    private void setLockScreenWallpaper() {
        Glide.with(this).load(mAdBean.getImg_url())
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    //获取类名
                    Class class1 = wpManager.getClass();
                    //获取设置锁屏壁纸的函数
                    Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);
                    //调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
                    setWallPaperMethod.invoke(wpManager, resource);
                    Toast.makeText(getActivity(), "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    Toast.makeText(getActivity(), "锁屏壁纸设置失败", Toast.LENGTH_SHORT).show();
                }
            }
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
}
