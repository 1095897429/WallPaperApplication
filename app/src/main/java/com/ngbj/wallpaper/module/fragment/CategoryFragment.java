package com.ngbj.wallpaper.module.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.category.Category_Top_Adapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.CategoryNewHotActivity;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.module.app.SearchActivity;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.CategoryContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.CategoryPresenter;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CategoryFragment extends BaseFragment<CategoryPresenter>
                implements CategoryContract.View{


    @BindView(R.id.move_top)
    RelativeLayout moveTop;


    @BindView(R.id.top_recyclerView)
    RecyclerView  top_recyclerView;


    @BindView(R.id.recommandRecyclerView)
    RecyclerView  recommandRecyclerView;

    List<InterestBean> interestBeanList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Category_Top_Adapter categoryTopAdapter;

    GridLayoutManager gridLayoutManager;
    RecomendAdapter recomendAdapter;
    List<MulAdBean> recommendList = new ArrayList<>();
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
        initRecommandRecycleView();
        mPresenter.getInterestData();
        mPresenter.getRecommendData(page,category,order);
    }


    private void initTopRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        top_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        categoryTopAdapter = new Category_Top_Adapter(interestBeanList);
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
            }
        });

        //壁纸Item点击
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MulAdBean mulAdBean = recommendList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                    if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                        KLog.d("tag -- 广告");
                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                    }else{
                        KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());
                        DetailActivityNew.openActivity(mContext,position,mulAdBean.adBean.getId(),AppConstant.CATEGORY);
                    }

                }else {
                    KLog.d("tag -- 广告",recommendList.get(position).apiAdBean.getName());
                }


            }
        });

        //壁纸喜好
        recomendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AdBean adBean = recommendList.get(position).adBean;
                if("0".equals(adBean.getIs_collected())){
                    adBean.setIs_collected("1");
                    ToastHelper.customToastView(getActivity(),"收藏成功");
                }else{
                    adBean.setIs_collected("0");
                    ToastHelper.customToastView(getActivity(),"取消收藏");
                }
                //全局刷新
                recomendAdapter.notifyDataSetChanged();
            }
        });


        //滑动监听
        recommandRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //做处理
                if(gridLayoutManager != null){
                    //当前条目索引 -- 根据索引做显示隐藏判断
                    int position = gridLayoutManager.findFirstVisibleItemPosition();
                    if(position > 6){//此postion检测是每一行作为一个position
                        moveTop.setVisibility(View.VISIBLE);
                    }else{
                        moveTop.setVisibility(View.GONE);
                    }
                }
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

        insertToSql(recommendList,AppConstant.CATEGORY);
    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);

        insertToSql(recommendList,AppConstant.CATEGORY);
    }

    @Override
    public void showInterestData(List<InterestBean> list) {
        interestBeanList.addAll(list);
        categoryTopAdapter.setNewData(interestBeanList);
    }
}
