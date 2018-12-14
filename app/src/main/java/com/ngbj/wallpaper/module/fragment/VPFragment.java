package com.ngbj.wallpaper.module.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.dialog.PreviewAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class VPFragment extends Fragment {

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
    private Unbinder unbinder;

    public VPFragment(){

    }

    public VPFragment(WallpagerBean adBean){
      this.mAdBean = adBean;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.activity_detail_item_1, null);
        //初始化
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, view);
        //数据初始化
        initData();
        return view;
    }

    private void initData() {
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
        unbinder.unbind();
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
        Glide.with(this).load(mAdBean.getThumb_img_url())
                .asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    //壁纸管理器
                    wpManager = WallpaperManager.getInstance(getActivity());
                    wpManager.setBitmap(resource);
                    Toast.makeText(getActivity(), "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        downImageVideo();
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

    private void downImageVideo() {

    }


}
