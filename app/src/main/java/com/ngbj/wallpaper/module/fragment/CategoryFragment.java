package com.ngbj.wallpaper.module.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.category.CategoryNewAdapter;
import com.ngbj.wallpaper.adapter.category.Category_Top_Adapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaesLogicFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.LoveCatogoryEvent;
import com.ngbj.wallpaper.module.app.CategoryNewHotActivity;
import com.ngbj.wallpaper.module.app.SearchActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.CategoryContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.CategoryPresenter;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CategoryFragment extends BaesLogicFragment<CategoryPresenter>
                implements CategoryContract.View{

    @BindView(R.id.view_pager)
    ViewPager mViewPager;


    @BindView(R.id.move_top)
    RelativeLayout moveTop;


    @BindView(R.id.horizontalScrollView1)
    HorizontalScrollView mHorizontalScrollView;

    @BindView(R.id.top_recyclerView)
    RecyclerView  top_recyclerView;


    @BindView(R.id.recommandRecyclerView)
    RecyclerView  recommandRecyclerView;

    List<InterestBean> interestBeanList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Category_Top_Adapter categoryTopAdapter;

    GridLayoutManager gridLayoutManager;
    RecomendAdapter recomendAdapter;
    List<MulAdBean> recommendList = new ArrayList<>();//界面临时数据

    int page = 1;
    String category = "0";//分类的id 推荐为0
    String order = "0";// 排序 0最新 1最热
    String keyword = "";//兴趣名称

    public static CategoryFragment getInstance(){
        return new CategoryFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.category_fragment;
    }

    @Override
    protected void initPresenter() {
       mPresenter = new CategoryPresenter();
    }

    @Override
    protected void initData() {
        initTopRecycleView();
        mPresenter.getCategories();
    }


    private void initTopRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        top_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        categoryTopAdapter = new Category_Top_Adapter(interestBeanList,0);
        top_recyclerView.setAdapter(categoryTopAdapter);
        //一行代码开启动画 默认CUSTOM动画
        categoryTopAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }


    private void initRecommandRecycleView() {
        recomendAdapter = new RecomendAdapter(recommendList);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置占据的列数 -- 根据实体中的类型
        recomendAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return recommendList.get(position).getItemType();
            }
        });
        //设置布局管理器
        recommandRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        recommandRecyclerView.setAdapter(recomendAdapter);
        //一行代码开启动画 默认CUSTOM动画
        recomendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        //加载更多数据
        recomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ++page;
                mPresenter.getMoreRecommendData(page,category,order);
            }
        },recommandRecyclerView);
    }


    @Override
    protected void initEvent() {

        //分类上的点击
        categoryTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                keyword = interestBeanList.get(position).getName();
                category = interestBeanList.get(position).getId();
                CategoryNewHotActivity.openActivity(mContext,category,keyword);

                for (InterestBean bean: interestBeanList) {
                    bean.setSelect(false);
                }
                interestBeanList.get(position).setSelect(true);
                categoryTopAdapter.notifyDataSetChanged();
                top_recyclerView.smoothScrollToPosition(position);

                mViewPager.setCurrentItem(position);

            }
        });

    }


    @OnClick(R.id.search_part)
    public void SearchPart(){
        SearchActivity.openActivity(mContext,AppConstant.FROMINDEX_SEACHER,"","");
    }


    @OnClick(R.id.move_top)
    public void MoveTop(){
        recommandRecyclerView.smoothScrollToPosition(0);
    }





    @Override
    public void showData(List<AdBean> topList, List<MulAdBean> redListc) {

    }

    @Override
    public void showEndView() {
        recomendAdapter.loadMoreEnd();//加载结束
        return;
    }

    @Override
    public void showRecommendData(List<MulAdBean> list) {
        recommendList.addAll(list);
        recomendAdapter.setNewData(recommendList);

    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> list) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(list);

    }


    @Override
    public void showCategories(List<InterestBean> list) {
        list.get(0).setSelect(true);//默认第一个选中
        interestBeanList.addAll(list);
        categoryTopAdapter.setNewData(interestBeanList);

        //TODO 添加分类Fragment
        setFragmentData();

//        final LinearLayout layout = new LinearLayout(getActivity());
//
//        //添加子View
//        for (int i = 0; i < list.size(); i++) {
//            Button btn = new Button(getActivity());
//            btn.setId(i);
//            btn.setText(list.get(i).getName());
//            layout.addView(btn);
//            viewList.add(btn);
//        }
//
//        mHorizontalScrollView.addView(layout);
//
//        //开启动画效果
//        mHorizontalScrollView.setSmoothScrollingEnabled(true);


    }

    private List<Fragment> fragments = new ArrayList<>();
    CategoryNewAdapter mCategoryNewAdapter;
    //初始化偏移量
    private int offset = 0;
    private int scrollViewWidth = 0;
    /** 填充vp的数据源 */
    @SuppressLint("NewApi")
    private void setFragmentData() {
        for (int i = 0; i < interestBeanList.size(); i++) {
            category = interestBeanList.get(i).getId();
            fragments.add(NewFragment.getInstance(category,"0"));//0最新
        }

        mCategoryNewAdapter = new CategoryNewAdapter(getChildFragmentManager(),fragments);
        mViewPager.setAdapter(mCategoryNewAdapter);
        mViewPager.setOffscreenPageLimit(interestBeanList.size());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                KLog.d("position :" + position);
                for (InterestBean bean: interestBeanList) {
                    bean.setSelect(false);
                }
                interestBeanList.get(position).setSelect(true);
                categoryTopAdapter.notifyDataSetChanged();
                top_recyclerView.smoothScrollToPosition(position);

//                int color = Color.rgb(233, 63, 33); // #E94221
//                for (int i = 0; i < viewList.size(); i++) {
//                    Button btn = (Button) viewList.get(i);
//                    btn.setBackgroundColor(Color.GRAY);
//                }
//
//                final Button button = (Button) viewList.get(position);
//                button.setBackgroundColor(Color.parseColor("#4c000000"));
//
//                scrollViewWidth = mHorizontalScrollView.getWidth();
//                if((scrollViewWidth + offset) < button.getRight()){
//                    mHorizontalScrollView.smoothScrollBy(button.getRight() - (scrollViewWidth + offset),0);
//                    offset += button.getRight() - (scrollViewWidth + offset);
//                }
//
//                if (offset > button.getLeft()) {//需要向左移动
//                    mHorizontalScrollView.smoothScrollBy(button.getLeft() - offset, 0);
//                    offset += button.getLeft() - offset;
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    /** 用户喜好 */
    @Override
    public void showRecordData() {

    }

    /** 取消收藏 */
    @Override
    public void showDeleteCollection() {
        KLog.d("what the fucking thing");
    }



}
