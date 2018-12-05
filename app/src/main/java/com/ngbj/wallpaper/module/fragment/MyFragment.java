package com.ngbj.wallpaper.module.fragment;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.my.MyFragmentAdapter;
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.module.app.SettingActivity;
import com.ngbj.wallpaper.mvp.presenter.fragment.IndexPresenter;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.ngbj.wallpaper.utils.common.UtilBitmap;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class MyFragment extends BaseFragment{

    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.iv_blur)
    ImageView iv_blur;


    MyFragmentAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    List<String> list_Title = new ArrayList<>();//标题

    public static MyFragment getInstance(){
        return new MyFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initPresenter() {
       mPresenter = new IndexPresenter();
    }

    @Override
    protected void initData() {
        setBlur();
        getData();
        initIndicator();
    }

    @SuppressLint("NewApi")
    private void setBlur() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.mipmap.default_head);
//        part1_bg.setImageBitmap(bitmap);
//        UtilBitmap utilBitmap = new UtilBitmap();
//        utilBitmap.blurImageView(getActivity(), part1_bg, 12.5f);
        Glide.with(this)
                .load(R.mipmap.default_head)
                .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                .into(iv_blur);
    }

    /** 填充vp的数据源 */
    @SuppressLint("NewApi")
    private void getData() {
        fragments.add(CreateFragment.getInstance());
        fragments.add(CreateFragment.getInstance());
        fragments.add(CreateFragment.getInstance());
        fragments.add(CreateFragment.getInstance());
        list_Title.add("创作");
        list_Title.add("收藏");
        list_Title.add("分享");
        list_Title.add("下载");
        pagerAdapter = new MyFragmentAdapter(getChildFragmentManager(),fragments,list_Title);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                KLog.d("position :" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }



    @OnClick(R.id.settting)
    public void Setting(){
       startActivity(new Intent(getActivity(),SettingActivity.class));
    }


    @OnClick(R.id.upload_fl)
    public void UploadFl(){
        KLog.d("upload_fl");
    }

    @OnClick(R.id.default_head)
    public void DefaultHead(){
        KLog.d("上传头像");
    }


}