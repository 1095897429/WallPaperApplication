package com.ngbj.wallpaper.adapter.app;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/***
 * 分类界面 vp的适配器
 */

public class CategoryNewAndHotAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragments;
    private List<String> list_Title;

    public CategoryNewAndHotAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public CategoryNewAndHotAdapter(FragmentManager fm, List<Fragment> fragments, List<String> list_Titl) {
        super(fm);
        this.fragments = fragments;
        this.list_Title = list_Titl;
    }

    public void setList_Title(List<String> list_Title) {
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position);
    }



}
