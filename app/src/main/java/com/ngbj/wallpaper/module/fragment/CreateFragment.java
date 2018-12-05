package com.ngbj.wallpaper.module.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.my.MyCommonAdapter;
import com.ngbj.wallpaper.base.BaseRefreshFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.module.app.SearchActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.socks.library.KLog;

import java.util.List;

/***
 * 创作Fragment
 */
public class CreateFragment extends BaseRefreshFragment<MyPresenter,AdBean>
            implements MyContract.View{


    MyCommonAdapter myCommonAdapter;
    GridLayoutManager gridLayoutManager;

    public static CreateFragment getInstance(){
        return new CreateFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.create_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyPresenter();
    }


    @Override
    protected void initRecyclerView() {
        myCommonAdapter = new MyCommonAdapter(mList);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        mRecyclerView.setAdapter(myCommonAdapter);

        //一行代码开启动画 默认CUSTOM动画
        myCommonAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //加载更多数据
        myCommonAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                    mPresenter.getMoreData("创作");
            }
        },mRecyclerView);
        //设置空布局
        myCommonAdapter.setEmptyView(R.layout.commom_empty);
        //事件
        myCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("选择的--- ：" + mList.get(position).getTitle());
                startActivity(new Intent(getActivity(),DetailActivityNew.class));
            }
        });
    }


    @Override
    protected void lazyAgainLoadData() {
        mPresenter.getAdData("创作");
    }


    @Override
    public void showAdData(List<AdBean> list) {
//        list.clear();
        complete();
        mList.addAll(list);
        myCommonAdapter.setNewData(mList);

    }

    @Override
    public void showMoreData(List<AdBean> list) {

        myCommonAdapter.loadMoreComplete();
        mList.addAll(list);
        myCommonAdapter.addData(list);
    }

    /** 隐藏加载进度框 */
    @Override
    public void complete() {

        if(mRefresh != null)
            mRefresh.setRefreshing(false);


        if(mIsRefreshing){
            if(mList != null && !mList.isEmpty()){
                mList.clear();
                KLog.d("刷新成功");
            }
        }
        mIsRefreshing = false;
    }


}
