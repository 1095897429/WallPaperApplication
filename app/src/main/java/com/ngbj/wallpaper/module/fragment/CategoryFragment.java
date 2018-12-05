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
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.module.app.CategoryNewAndHotActivity;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.module.app.SearchActivity;
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

    List<AdBean> myTopList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Category_Top_Adapter categoryTopAdapter;

    GridLayoutManager gridLayoutManager;
    RecomendAdapter recomendAdapter;
    List<MulAdBean> recommendList = new ArrayList<>();

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
        mPresenter.getData();
    }


    private void initTopRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        top_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        categoryTopAdapter = new Category_Top_Adapter(myTopList);
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
                mPresenter.getMoreRecommendData();
            }
        },recommandRecyclerView);
    }


    @Override
    protected void initEvent() {

        //分类上的点击
        categoryTopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("tag",myTopList.get(position).getTitle());
                Intent intent = new Intent(getActivity(),CategoryNewAndHotActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("keyword",myTopList.get(position).getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //壁纸Item点击
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MulAdBean mulAdBean = recommendList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){
                    KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());
                }else {
                    KLog.d("tag -- 广告",recommendList.get(position).apiAdBean.getName());
                }
                startActivity(new Intent(getActivity(),DetailActivityNew.class));
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
        startActivity(new Intent(getActivity(),SearchActivity.class));
    }


    @OnClick(R.id.move_top)
    public void MoveTop(){
        recommandRecyclerView.smoothScrollToPosition(0);
    }





    @Override
    public void showData(List<AdBean> topList, List<MulAdBean> redListc) {
        myTopList.addAll(topList);
        categoryTopAdapter.setNewData(topList);
        recommendList.addAll(redListc);
        recomendAdapter.setNewData(recommendList);

    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);
    }
}
