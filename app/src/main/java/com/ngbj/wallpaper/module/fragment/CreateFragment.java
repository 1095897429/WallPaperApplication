package com.ngbj.wallpaper.module.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.socks.library.KLog;

import java.util.List;

/***
 * 创作Fragment
 */
public class CreateFragment extends BaseRefreshFragment<MyPresenter,MulAdBean>
            implements MyContract.View{


    MyCommonAdapter myCommonAdapter;
    GridLayoutManager gridLayoutManager;
    String mType;
    String fromWhere = "";//来源


    public static CreateFragment getInstance(String type){
        CreateFragment mFragment = new CreateFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString("type");
        KLog.d("类型： " + mType);
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
        myCommonAdapter.bindToRecyclerView(mRecyclerView);
        //设置空布局
        myCommonAdapter.setEmptyView(R.layout.commom_empty);
        //事件
        myCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("选择的--- ：" + mList.get(position).adBean.getTitle());

                MulAdBean mulAdBean = mList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                    if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                        KLog.d("tag -- 广告");
                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                    }else{
                        KLog.d("tag -- 正常",mList.get(position).adBean.getTitle());


                        if(AppConstant.COLLECTION.equals(mType)){
                            fromWhere = AppConstant.MY_2;
                        }else if(AppConstant.SHARE.equals(mType)){
                            fromWhere = AppConstant.MY_3;
                        }else if(AppConstant.DOWNLOAD.equals(mType)){
                            fromWhere = AppConstant.MY_4;
                        }

                        DetailActivityNew.openActivity(mContext,position,mulAdBean.adBean.getId(),fromWhere);
                    }

                }else {
                    KLog.d("tag -- 广告",mList.get(position).apiAdBean.getName());
                }

            }
        });
    }


    @Override
    protected void lazyAgainLoadData() {
        mPresenter.getRecord(mType);
    }



    @Override
    public void showUploadHistory(List<MulAdBean> list) {
        complete();
        mList.addAll(list);
        myCommonAdapter.setNewData(mList);

        insertToSql(list,AppConstant.MY_2);
    }


    @Override
    public void showUploadHeadData(LoginBean loginBean) {

    }

    @Override
    public void showRecord(List<MulAdBean> list) {
        complete();
        mList.addAll(list);
        myCommonAdapter.setNewData(mList);

        if(AppConstant.COLLECTION.equals(mType)){
            insertToSql(list,AppConstant.MY_2);
        }else if(AppConstant.SHARE.equals(mType)){
            insertToSql(list,AppConstant.MY_3);
        }else if(AppConstant.DOWNLOAD.equals(mType)){
            insertToSql(list,AppConstant.MY_4);
        }
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
