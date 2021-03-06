package com.ngbj.wallpaper.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/***
 * 初始化一些参数逻辑，具体的presenter逻辑还是具体的Fragment中实现
 */
public abstract  class BaseRefreshFragment<T extends BaseContract.BasePresenter,K>
            extends BaseFragment<T> implements SwipeRefreshLayout.OnRefreshListener{

    protected RecyclerView mRecyclerView;
    protected SwipeRefreshLayout mRefresh;
    protected List<K> recommendList = new ArrayList<>();
    protected ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据
    protected boolean mIsRefreshing = false;//第一次或者手动的下拉操作
    protected int mPage = 1;//当前页数


    /** 初始化需要的控件,调用父类的懒加载 */
    @Override
    protected void finishCreateView() {
        mRefresh = ButterKnife.findById(mRootView, R.id.refresh);
        mRecyclerView = ButterKnife.findById(mRootView,R.id.recycler);
        super.finishCreateView();
    }


    /** 设置一些属性 自动出现下拉加载框 */
    protected void initRefreshLayout(){
        if(null != mRefresh){
            mRefresh.setColorSchemeResources(R.color.colorPrimary);
            mRefresh.post(new Runnable() {
                @Override
                public void run() {
                    mRefresh.setRefreshing(true);
                    lazyAgainLoadData();
                }
            });
            mRefresh.setOnRefreshListener(this);
        }
    }


    /** 交给每个子类具体实现 */
    protected  void lazyAgainLoadData(){}

    /** 交给每个子类具体实现 */
    protected void initRecyclerView(){}


    @Override
    protected void lazyLoadData() {
         initRefreshLayout();
         initRecyclerView();

    }

    /** 下拉重新加载 , mList清空  mPage设为1*/
    @Override
    public void onRefresh() {
        mIsRefreshing = true;
        mPage = 1;
        lazyAgainLoadData();
    }


    /** 隐藏加载进度框 */
    @Override
    public void complete() {

        if(mRefresh != null)
            mRefresh.setRefreshing(false);


        if(mIsRefreshing){
            if(recommendList != null && !recommendList.isEmpty()){
                recommendList.clear();
                temps.clear();
                KLog.d("刷新成功");
            }
        }
        mIsRefreshing = false;
    }

    @Override
    public void showError(String msg) {

        if(mRefresh != null){
            mRefresh.setRefreshing(false);
            mIsRefreshing = false;
        }
    }




    // 临时数据 数据库数据
    protected void commonDtaLogin(String fromWhere,boolean isMore,List<MulAdBean> list) {

        if(!isMore){
            temps.clear();//临时数据清空
            MyApplication.getDbManager().deleteWallpagerBeanList(fromWhere);//删除某来源
        }


        List<WallpagerBean> wTemps = transformDataToWallpaper(fromWhere,list);
        temps.addAll(wTemps);

        for (WallpagerBean wallpagerBean:wTemps) {
            MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);//新增
        }

    }


}
