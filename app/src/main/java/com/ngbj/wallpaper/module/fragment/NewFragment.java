package com.ngbj.wallpaper.module.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaseRefreshFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.DetailParamBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.LoveHotNewEvent;
import com.ngbj.wallpaper.eventbus.LoveNewEvent;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.CategoryContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.CategoryPresenter;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * 最热Fragment -- 分类的子项
 * 1.共用列表的adapter
 */
public class NewFragment extends BaseRefreshFragment<CategoryPresenter,MulAdBean>
            implements CategoryContract.View{


    RecomendAdapter mRecomendAdapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据
    HashMap<String ,ArrayList<WallpagerBean>> mHashMap = new HashMap<>();//key -- category  value -- list

    String category = "0";//分类的id 推荐为0
    String order = "0";// 排序 0最新 1最热


    public static NewFragment getInstance(String category, String order){
        NewFragment mFragment = new NewFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("order", order);
        mFragment.setArguments(bundle);
        return mFragment;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.create_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CategoryPresenter();
    }


    /**  事件放在此方法中，放在 */
    @Override
    protected void initRecyclerView() {
        mRecomendAdapter = new RecomendAdapter(recommendList);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置占据的列数 -- 根据实体中的类型
        mRecomendAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return recommendList.get(position).getItemType();
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
                    ++mPage;
                    mPresenter.getMoreRecommendData(mPage,category,order);
            }
        },mRecyclerView);
        //设置空布局
        mRecomendAdapter.setEmptyView(R.layout.commom_empty);


        //壁纸点击事件
        mRecomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MulAdBean mulAdBean = recommendList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                    if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                        KLog.d("tag -- 广告");
//                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                        //不能用静态方法，导致内存泄漏
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("loadUrl", "https://www.baidu.com/");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }else{
                        KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());


                        DetailParamBean bean = new DetailParamBean();
                        bean.setPage(mPage);
                        bean.setPosition(position);
                        bean.setWallpagerId(mulAdBean.adBean.getId());
                        bean.setFromWhere(AppConstant.CATEGORY_NEW);
                        bean.setCategory(category);
                        bean.setOrder(order);

                        //从不同的集合中获取
                        ArrayList<WallpagerBean> mytemp = mHashMap.get(category);

//                        DetailActivity.openActivity(mContext,bean,mytemp);

                        //不能用静态方法，导致内存泄漏
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean",bean);
                        bundle.putSerializable("list",mytemp);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    }

                }else {
                    KLog.d("tag -- 广告",recommendList.get(position).apiAdBean.getName());
                }
            }
        });


        //壁纸喜好
        mRecomendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AdBean adBean = recommendList.get(position).adBean;
                if("0".equals(adBean.getIs_collected())){
                    adBean.setIs_collected("1");
                    ToastHelper.customToastView(getActivity(),"收藏成功");
                    mPresenter.getRecordData(adBean.getId(),"2");
                    updateLove(position,true);
                }else{
                    adBean.setIs_collected("0");
                    ToastHelper.customToastView(getActivity(),"取消收藏");
                    mPresenter.getDeleteCollection(adBean.getId());
                    updateLove(position,false);
                }
                //全局刷新
                mRecomendAdapter.notifyDataSetChanged();
            }
        });

    }



    @Override
    protected void lazyAgainLoadData() {
        mPresenter.getRecommendData(mPage,category,order);
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
    public void showRecommendData(List<MulAdBean> list) {
        complete();
        recommendList.addAll(list);
        mRecomendAdapter.setNewData(list);

        //临时数据清空
        temps.clear();
        temps.addAll(transformDataToWallpaper(list));
        mHashMap.put(category,temps);

    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> list) {
        mRecomendAdapter.loadMoreComplete();
        recommendList.addAll(list);
        mRecomendAdapter.addData(list);


        temps.addAll(transformDataToWallpaper(list));
        mHashMap.put(category,temps);
    }

    @Override
    public void showCategories(List<InterestBean> interestBeanList) {

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


    /** =================== EventBus  开始 =================== */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        category = getArguments().getString("category");
        order = getArguments().getString("order");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Subscribe
    public void onLoveNewEvent(LoveNewEvent event){
        boolean isLove = event.isLove();
        boolean isReset = event.isReset();
        if(!isReset){ //不需要更新全体数据
            MulAdBean mulAdBean= recommendList.get(event.getPosition());
            mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
            mHashMap.get(category).get(event.getPage()).setIs_collected(isLove ? "1" : "0");
        }else{
            mPage = event.getPage();
            temps.addAll(transformDataToWallpaper(event.getMulAdBeanList()));
            mRecomendAdapter.addData(event.getMulAdBeanList());

        }

        mRecomendAdapter.notifyDataSetChanged();
    }



    /** 主界面喜好修改 */
    private void updateLove(int position,boolean isLove) {

        MulAdBean mulAdBean= recommendList.get(position);
        if(isLove){
            mulAdBean.adBean.setIs_collected("1");
            mHashMap.get(category).get(position).setIs_collected("1");
        }else{
            mulAdBean.adBean.setIs_collected("0");
            mHashMap.get(category).get(position).setIs_collected("0");
        }


        mRecomendAdapter.notifyDataSetChanged();

//        List<WallpagerBean> wallpagerBeanList = MyApplication.getDbManager().queryDifferWPId(mulAdBean.adBean.getId());
//        for (WallpagerBean wallpagerBean: wallpagerBeanList) {
//            if(isLove){
//                wallpagerBean.setIs_collected("1");
//            }else
//                wallpagerBean.setIs_collected("0");
//
//            MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
//        }
    }


    /** =================== EventBus  结束 =================== */




}
