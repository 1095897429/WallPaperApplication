package com.ngbj.wallpaper.module.app;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.app.History_Search_Adapter;
import com.ngbj.wallpaper.adapter.app.Interest_Adapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.app.InterestContract;
import com.ngbj.wallpaper.mvp.contract.app.LoginContract;
import com.ngbj.wallpaper.mvp.contract.fragment.IndexContract;
import com.ngbj.wallpaper.mvp.presenter.app.InterestPresenter;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class InterestActivity extends BaseActivity<InterestPresenter>
            implements InterestContract.View{

    @BindView(R.id.interest_recyclerView)
    RecyclerView interest_recyclerView;

    GridLayoutManager layoutManager;
    List<InterestBean> interestBeanList = new ArrayList<>();
    Interest_Adapter interestAdapter;
    List<String> selectList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_interest;
    }


    @Override
    protected void initPresenter() {
        mPresenter = new InterestPresenter();
    }

    @Override
    protected void initData() {
        initRecycleView();
        mPresenter.getInterestData();
    }

    @Override
    protected void initEvent() {
        //兴趣爱好Item点击
        interestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InterestBean interestBean = interestBeanList.get(position);
                KLog.d("点击的条目 " + interestBean.getName() + " " + interestBean.getInterestId());
                if(interestBean.isSelect()){
                    interestBean.setSelect(false);
                    selectList.remove(interestBean.getInterestId());
                }else{
                    interestBean.setSelect(true);
                    selectList.add(interestBean.getInterestId());
                }
            interestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecycleView() {
        layoutManager = new GridLayoutManager(this,3);
        //设置布局管理器
        interest_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        interestAdapter = new Interest_Adapter(interestBeanList);
        interest_recyclerView.setAdapter(interestAdapter);
        //一行代码开启动画 默认CUSTOM动画
        interestAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }


    @Override
    public void showInterestData(List<InterestBean> list) {
        interestBeanList.addAll(list);
        interestAdapter.setNewData(interestBeanList);
    }


    @OnClick(R.id.interest_done)
    public void InterestDone(){
        KLog.d("选中的数量:" + selectList.size());
    }

}
