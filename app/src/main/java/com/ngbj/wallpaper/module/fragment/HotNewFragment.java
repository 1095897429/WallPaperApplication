package com.ngbj.wallpaper.module.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.adapter.my.MyCommonAdapter;
import com.ngbj.wallpaper.base.BaseRefreshFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.CategoryContract;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.CategoryPresenter;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.socks.library.KLog;

import java.util.List;

/***
 * 最新最热Fragment
 * 1.共用列表的adapter
 */
public class HotNewFragment extends BaseRefreshFragment<CategoryPresenter,MulAdBean>
            implements CategoryContract.View{


    RecomendAdapter mRecomendAdapter;
    GridLayoutManager gridLayoutManager;

    int page = 1;
    String category = "0";//分类的id 推荐为0
    String order = "0";// 排序 0最新 1最热


    public static HotNewFragment getInstance(String category,String order){
        HotNewFragment mFragment = new HotNewFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("order", order);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
        order = getArguments().getString("order");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.create_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CategoryPresenter();
    }


    @Override
    protected void initRecyclerView() {
        mRecomendAdapter = new RecomendAdapter(mList);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置占据的列数 -- 根据实体中的类型
        mRecomendAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return mList.get(position).getItemType();
            }
        });
        //设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        mRecyclerView.setAdapter(mRecomendAdapter);

        //一行代码开启动画 默认CUSTOM动画
        mRecomendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //加载更多数据
        mRecomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                    ++page;
                    mPresenter.getMoreRecommendData(page,category,order);
            }
        },mRecyclerView);
        //设置空布局
        mRecomendAdapter.setEmptyView(R.layout.commom_empty);
        //事件
        mRecomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MulAdBean mulAdBean = mList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                    if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                        KLog.d("tag -- 广告");
                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                    }else{
                        KLog.d("tag -- 正常",mList.get(position).adBean.getTitle());


                        String from = "";
                        if(order.equals("1")){//最热
                            from = AppConstant.HOT;
                        }else if(order.equals("0")){
                            from = AppConstant.NEW;
                        }

                        DetailActivity.openActivity(mContext,position,mulAdBean.adBean.getId(),from);
                    }

                }else {
                    KLog.d("tag -- 广告",mList.get(position).apiAdBean.getName());
                }
            }
        });
    }


    @Override
    protected void lazyAgainLoadData() {
        mPresenter.getRecommendData(page,category,order);
    }


    @Override
    public void showData(List<AdBean> topList, List<MulAdBean> recommendList) {

    }

    @Override
    public void showEndView() {
        mRecomendAdapter.loadMoreEnd();//加载结束
        return;
    }

    @Override
    public void showRecommendData(List<MulAdBean> recommendList) {
        complete();
        mList.addAll(recommendList);
        mRecomendAdapter.setNewData(mList);

        if(order.equals("1")){//最热
            insertToSql(recommendList,AppConstant.HOT);
        }else if(order.equals("0")){
            insertToSql(recommendList,AppConstant.NEW);
        }

    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        mRecomendAdapter.loadMoreComplete();
        mRecomendAdapter.addData(recommendList);

        if(order.equals("1")){//最热
            insertToSql(recommendList,AppConstant.HOT);
        }else if(order.equals("0")){
            insertToSql(recommendList,AppConstant.NEW);
        }
    }

    @Override
    public void showInterestData(List<InterestBean> interestBeanList) {

    }


    /** 隐藏加载进度框 */
//    @Override
//    public void complete() {
//
//        if(mRefresh != null)
//            mRefresh.setRefreshing(false);
//
//        if(mIsRefreshing){
//            if(mList != null && !mList.isEmpty()){
//                mList.clear();
//                KLog.d("刷新成功");
//            }
//        }
//        mIsRefreshing = false;
//    }


}
