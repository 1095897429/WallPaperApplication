package com.ngbj.wallpaper.module.app;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.PreviewPicActivity;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.DetailAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.fragment.VPFragment;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ScreenHepler;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.common.WallpaperUtil;
import com.ngbj.wallpaper.utils.widget.VerticalViewPager;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.mediaPlayer.setDataSource(this,Uri.parse(uri));//url 路径 方式一
 * 2.http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4
 */
public class DetailActivityNew extends BaseActivity<DetailPresenter>
            implements DetailContract.View{

    @BindView(R.id.verticalviewpager)
    VerticalViewPager verticalviewpager;


    int mPosition;//当前选择的位置
    String wallpagerId;//当前选择的壁纸ID
    String fromWhere;//从哪里点击



    private List<VPFragment> vpFragments = new ArrayList<>();   //碎片集合



    /** position -- 点击的位置   wallpagerId -- 壁纸唯一的索引  */
    public static void openActivity(Context context, int position, String wallpagerId,String fromWhere){
        Intent intent = new Intent(context,DetailActivityNew.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("wallpagerId",wallpagerId);
        bundle.putString("fromWhere",fromWhere);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_new;
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
        KLog.d("wallpagerId: " + wallpagerId);

        fromWhere = getIntent().getExtras().getString("fromWhere");
        KLog.d("从哪里点击,并存入数据库中：" + fromWhere);
        SPHelper.put(this,"fromWhere",fromWhere);


        //viewpager加载view的数据源
        setViewPager();
    }


    @Override
    protected void initEvent() {
        verticalviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                KLog.d("postion: " + position);
                KLog.d("adBean: " + adBeanList.get(position));
                if(position < 0){
//                    ToastHelper.customToastView(MyApplication.getInstance(),"已达到第一页");
                    return;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }






    List<WallpagerBean> adBeanList;
    private void setViewPager(){

        //根据数据,我们是可以知道我们要返回多少页的,所以我们就创建多少个碎片

        //根据来源不同查询数据
        adBeanList =  MyApplication.getDbManager().queryDifferCome(fromWhere);

        for(int position = 0; position < adBeanList.size(); position++){
            WallpagerBean wallpagerBean = adBeanList.get(position);
            vpFragments.add(VPFragment.getInstance(wallpagerBean));
        }
        //给viewpager设置适配器
        VpAdapter adapter = new VpAdapter(getSupportFragmentManager());
        verticalviewpager.setAdapter(adapter);
        verticalviewpager.setOffscreenPageLimit(0);
        verticalviewpager.setCurrentItem(mPosition);

    }


    @Override
    public void showData(AdBean adBean) {

     }


    @Override
    public void showDynamicData(List<WallpagerBean> adBeanList) {


    }

    @Override
    public void showReportData() {

    }

    @Override
    public void showRecordData() {

    }


    /** ------ viewPager的PagerAdapter适配器 开始 ------ */

    class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        /** 返回要显示的碎片 */
        @Override
        public Fragment getItem(int position) {
            return vpFragments.get(position);
        }

        /** 返回要显示多少页 */
        @Override
        public int getCount() {
            return vpFragments.size();
        }

        /** 销毁 */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }

    /** ------ viewPager的PagerAdapter适配器 结束 ------ */





}
