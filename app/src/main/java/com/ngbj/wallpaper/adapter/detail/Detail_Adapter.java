package com.ngbj.wallpaper.adapter.detail;



import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.DownBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.utils.widget.GlideCircleTransform;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * 默认第一次加载的内容
 * 视频  缩略图 大图 -- 都显示
 * 发请求时，适配器加载 -- 正常现象
 */

public class Detail_Adapter extends BaseQuickAdapter<WallpagerBean,BaseViewHolder> {

    public Detail_Adapter(List<WallpagerBean> data) {
        super(R.layout.activity_detail_item,data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, WallpagerBean item) {

//        KLog.d("进入适配器中 ，缩略图一直显示的--=-=--=-==-=");

        if(item.getType().equals(AppConstant.COMMON_WP)){ //静态
//            helper.getView(R.id.video_view).animate().alpha(0).start();
//
//            helper.setVisible(R.id.webview,false);
//            helper.setVisible(R.id.part2,true);
            commonData(helper,item);

        }else if(item.getType().equals(AppConstant.DYMATIC_WP)){//动态

//            if(TextUtils.isEmpty(item.getMovie_url())){
//                ToastHelper.customToastView(mContext,"视频获取失败，请重新加载");
//                ((Activity)mContext).finish();
//                return;
//            }

            helper.getView(R.id.video_view).animate().alpha(1).start();
//            helper.getView(R.id.video_view).animate().alpha(1).start();


//            helper.setVisible(R.id.webview,false);
//            helper.setVisible(R.id.part2,true);
            commonData(helper,item);



//            ((VideoView)helper.getView(R.id.video_view)).setVideoPath(item.getMovie_url());

//            SPHelper.put(mContext,"video",item.getMovie_url());//用于系统动态设置

        }else if(item.getType().equals(AppConstant.COMMON_AD)){//item 广告
//            KLog.d("item 广告");

//            helper.setVisible(R.id.webview,true);
//            helper.setVisible(R.id.part2,false);

            //默认加载为缩略图
//            Glide.with(MyApplication.getInstance())
//                    .load(R.mipmap.img_video_1)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .crossFade()
//                    .into((ImageView) helper.getView(R.id.img_thumb));

        }else if(item.getType().equals(AppConstant.API_AD)){//Api 广告
//            KLog.d("Api 广告");

            //默认加载为缩略图
//            Glide.with(MyApplication.getInstance())
//                    .load(R.mipmap.img_video_2)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .crossFade()
//                    .into((ImageView) helper.getView(R.id.img_thumb));
        }
    }


    private  void commonData(BaseViewHolder helper, WallpagerBean item){
        //大图
//        if(!TextUtils.isEmpty(item.getImg_url())){
//            Glide.with(MyApplication.getInstance())
//                    .load(item.getImg_url())
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into((ImageView) helper.getView(R.id.img_all));
//        }else{
//            //默认加载为缩略图
//            Glide.with(MyApplication.getInstance())
//                    .load(item.getThumb_img_url())
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into((ImageView) helper.getView(R.id.img_thumb));
//        }

        //默认加载为缩略图
        Glide.with(MyApplication.getInstance())
                .load(item.getThumb_img_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) helper.getView(R.id.img_thumb));

        //大图
        Glide.with(MyApplication.getInstance())
                .load(item.getImg_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) helper.getView(R.id.img_all));

        //圆形头像
        if (!TextUtils.isEmpty(item.getHead_img())) {

            Glide.with(MyApplication.getInstance())
                    .load(item.getHead_img())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .transform(new GlideCircleTransform(mContext))
                    .into((ImageView) helper.getView(R.id.author_icon));
        } else {
            helper.setImageResource(R.id.author_icon,R.mipmap.author_head);
        }

        //标题
        helper.setText(R.id.image_title,item.getTitle() == null ? "标题" : item.getTitle());
        //作者名
        helper.setText(R.id.author_name,item.getNickname() == null ? "Mask" : item.getNickname());
        //下载
        if(getDownCount() == 3 ){
            helper.setVisible(R.id.down_lock,true);
        }else{
            helper.setVisible(R.id.down_lock,false);
        }


        //喜好
        if("0".equals(item.getIs_collected())){
            helper.setImageResource(R.id.icon_love,R.mipmap.icon_unlove);
        }else{
            helper.setImageResource(R.id.icon_love,R.mipmap.icon_love);
        }

    }


    /** ---------------- 查询 下载次数逻辑  ------------------  */
    SimpleDateFormat mSimpleDateFormat;
    Date mDate;
    String mCurrentYear_Month_Day;
    String mHistoryYear_Month_Day;
    DownBean mDownBean;
    int count ;

    //获取下载次数
    protected int getDownCount(){
        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mDate = new Date(System.currentTimeMillis());
        mCurrentYear_Month_Day = mSimpleDateFormat.format(mDate);
//        KLog.d("当前的年月日：" ,mCurrentYear_Month_Day);

        mDownBean =  MyApplication.getDbManager().queryDownBean();

        if(null == mDownBean){//项目第一次启动
            DownBean downBean = new DownBean();
            downBean.setCount(0);
            downBean.setDate(mCurrentYear_Month_Day);
            MyApplication.getDbManager().insertDownBean(downBean);
            return  0;
        }else{
            mHistoryYear_Month_Day = mDownBean.getDate();
//            KLog.d("历史的年月日：",mHistoryYear_Month_Day);

            if(!TextUtils.isEmpty(mHistoryYear_Month_Day)
                    && mCurrentYear_Month_Day.compareTo(mHistoryYear_Month_Day) > 0){//第二天 重置下次次数
                mDownBean.setCount(0);
                MyApplication.getDbManager().updateDownBean(mDownBean);
                return 0;

            }else{//当天
                count = mDownBean.getCount();//获取当日的下载免费次数
            }
        }

        return count;

    }

    //查询并修改
    protected void queryAndUpdate(){
        DownBean downBean = MyApplication.getDbManager().queryDownBean();
        downBean.setCount(count + 1);
        MyApplication.getDbManager().updateDownBean(downBean);
    }


}