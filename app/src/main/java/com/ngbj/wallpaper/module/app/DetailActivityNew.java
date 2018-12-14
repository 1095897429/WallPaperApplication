package com.ngbj.wallpaper.module.app;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.PreviewPicActivity;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.DetailAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.module.fragment.VPFragment;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.mvp.presenter.app.DetailPresenter;
import com.ngbj.wallpaper.utils.common.ScreenHepler;
import com.ngbj.wallpaper.utils.common.WallpaperUtil;
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
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/***
 * 1.mediaPlayer.setDataSource(this,Uri.parse(uri));//url 路径 方式一
 * 2.http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4
 */
public class DetailActivityNew extends BaseActivity<DetailPresenter>
            implements DetailContract.View{

    private final static int REQUEST_CODE_SET_WALLPAPER = 0x001;//动态
    private final static int REQUEST_CODE_SELECT_SYSTEM_WALLPAPER = 0x002;//静态

    @BindView(R.id.verticalviewpager)
    VerticalViewPager verticalviewpager;


    int position;//当前选择的位置
    String wallpagerId;//当前选择的壁纸ID
    AdBean mAdBean;//当前选择的实体
    MediaPlayer mMediaPlayer;


    private List<VPFragment> vpFragments = new ArrayList<>();   //碎片集合


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
    }

    /** ------ viewPager的PagerAdapter适配器 结束 ------ */


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
        position = getIntent().getExtras().getInt("position");
        KLog.d("当前选择的位置为：" + position);
        mAdBean = getIntent().getExtras().getParcelable("bean");
        KLog.d("bean" + mAdBean.getTitle());
        wallpagerId = getIntent().getExtras().getString("wallpagerId");
        KLog.d("wallpagerId" + wallpagerId);

        //TODO viewpager加载view的数据源
        mPresenter.getData(wallpagerId);
    }


    @Override
    protected void initEvent() {

    }



    @Override
    protected void onPause() {
        super.onPause();
        KLog.d("onPause");
        if(mMediaPlayer != null)
            mMediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.d("onDestroy");
        if(mMediaPlayer != null)
            mMediaPlayer.stop();
    }



    @Override
    public void showData(AdBean adBean) {

        KLog.d("大图的url：" + adBean.getImg_url());
        mPresenter.getDynamicData();
    }

    @Override
    public void showDynamicData(List<WallpagerBean> adBeanList) {
        //根据数据,我们是可以知道我们要返回多少页的,所以我们就创建多少个碎片
        for(int position = 0; position < adBeanList.size(); position++){
            vpFragments.add(new VPFragment(adBeanList.get(position)));
        }
        //给viewpager设置适配器
        verticalviewpager.setAdapter(new VpAdapter(getSupportFragmentManager()));
        //给viewpager设置索引
        verticalviewpager.setCurrentItem(position);

    }





}
