package com.ngbj.wallpaper.module.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.my.MyCommonAdapter;
import com.ngbj.wallpaper.base.BaseRefreshFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.socks.library.KLog;

import java.util.List;

/***
 * 我界面中第一个创作Fragment
 */
public class UploadHistoryFragment extends BaseRefreshFragment<MyPresenter,MulAdBean>
        implements MyContract.View{


    MyCommonAdapter myCommonAdapter;
    GridLayoutManager gridLayoutManager;


    public static UploadHistoryFragment getInstance(){
        UploadHistoryFragment mFragment = new UploadHistoryFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        mFragment.setArguments(bundle);
        return mFragment;
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
        //设置绑定关系
        myCommonAdapter.bindToRecyclerView(mRecyclerView);
        //设置空布局
        myCommonAdapter.setEmptyView(R.layout.commom_empty);
        //事件
        myCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("选择的--- ：" + mList.get(position).adBean.getTitle());
                DetailActivityNew.openActivity(mContext,position,mList.get(position).adBean.getId(),AppConstant.MY_1);
            }
        });
    }


    @Override
    protected void lazyAgainLoadData() {

        String accessToken = (String) SPHelper.get(mContext,AppConstant.ACCESSTOKEN,"");
        if(TextUtils.isEmpty(accessToken)){
            accessToken = "7v72FRobjPBvOFD6udGGq2UgRNPANUrv";
        }

        mPresenter.getUploadHistory(accessToken);
    }


    @Override
    public void showUploadHistory(List<MulAdBean> list) {
        KLog.d("list: " + list.size());
        complete();
        mList.addAll(list);
        myCommonAdapter.setNewData(mList);

        insertToSql(mList,AppConstant.MY_1);
    }


    @Override
    public void showUploadHeadData(LoginBean loginBean) {

    }

    @Override
    public void showRecord(List<MulAdBean> list) {
        complete();
        mList.addAll(list);
        myCommonAdapter.setNewData(mList);

        insertToSql(mList,AppConstant.MY_1);
    }




}
