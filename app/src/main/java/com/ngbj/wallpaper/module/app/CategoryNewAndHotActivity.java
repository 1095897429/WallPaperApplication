package com.ngbj.wallpaper.module.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.app.CategoryNewAndHotAdapter;
import com.ngbj.wallpaper.adapter.my.MyFragmentAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.module.fragment.CreateFragment;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class CategoryNewAndHotActivity extends BaseActivity{

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    CategoryNewAndHotAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    List<String> list_Title = new ArrayList<>();//标题
    String keyWord;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_hot;
    }


    @Override
    protected void initData() {
        keyWord = getIntent().getExtras().getString("keyword");
        title.setText(keyWord);

        getData();
        initIndicator();
    }

    /** 填充vp的数据源 */
    @SuppressLint("NewApi")
    private void getData() {
        fragments.add(CreateFragment.getInstance());
        fragments.add(CreateFragment.getInstance());
        list_Title.add("最新");
        list_Title.add("最热");
        pagerAdapter = new CategoryNewAndHotAdapter(getSupportFragmentManager(),fragments,list_Title);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

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

    @OnClick(R.id.back)
    public void Back(){
        finish();
    }



}
