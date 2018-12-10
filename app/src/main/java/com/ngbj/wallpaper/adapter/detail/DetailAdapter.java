package com.ngbj.wallpaper.adapter.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
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
import com.ngbj.wallpaper.module.app.ReleaseActivity;
import com.socks.library.KLog;

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
        container.addView(view);
        return view;
    }



    private void initView(int position) {
        final ConstraintLayout part1 = view.findViewById(R.id.part1);//头部
        final ConstraintLayout part2 = view.findViewById(R.id.part2);//底部
        final ImageView imageView  = view.findViewById(R.id.imageView);//图片
        final ImageView back = view.findViewById(R.id.back);//返回
        final ImageView report = view.findViewById(R.id.report);//举报
        final TextView down = view.findViewById(R.id.down);//下载
        final ImageView icon_share = view.findViewById(R.id.icon_share);//分享
        final ImageView icon_preview = view.findViewById(R.id.icon_preview);//预览


        Glide.with(mContext)
                .load(adBeanList.get(position).getThumb_img_url())
                .centerCrop()
                .into(imageView);

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
