package com.ngbj.wallpaper.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
 * 最新最热Fragment
 * 1.共用列表的adapter
 */
public class HotNewFragment extends BaseRefreshFragment<CategoryPresenter,MulAdBean>
            implements CategoryContract.View{


    RecomendAdapter mRecomendAdapter;
    GridLayoutManager gridLayoutManager;
    List<MulAdBean> recommendList = new ArrayList<>();//界面临时数据
    ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据
    HashMap<String ,ArrayList<WallpagerBean>> mHashMap = new HashMap<>();//key -- category  value -- list

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
                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                    }else{
                        KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());


                        String from = "";
                        if(order.equals("1")){//最热
                            from = AppConstant.HOT;
                        }else if(order.equals("0")){
                            from = AppConstant.NEW;
                        }

                        DetailParamBean bean = new DetailParamBean();
                        bean.setPage(mPage);
                        bean.setPosition(position);
                        bean.setFromWhere(from);
                        bean.setWallpagerId(mulAdBean.adBean.getId());
                        bean.setFromWhere(AppConstant.CATEGORY_NEW_HOT);
                        bean.setCategory(category);
                        bean.setOrder(order);

                        //从不同的集合中获取
                        ArrayList<WallpagerBean> mytemp = mHashMap.get(category);

                        DetailActivity.openActivity(mContext,bean,mytemp);


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
        mRecomendAdapter.addData(list);
        recommendList.addAll(list);
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
    public void onLoveHotNewEvent(LoveHotNewEvent event){
        boolean isLove = event.isLove();
        boolean isReset = event.isReset();
        if(!isReset){ //不需要更新全体数据
            MulAdBean mulAdBean= recommendList.get(event.getPosition());
            mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
            mHashMap.get(category).get(event.getPosition()).setIs_collected(isLove ? "1":"0");
        }else{
            mPage = event.getPage();
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



    /** page == 1 表示初始化请求  其他情况是加载更多*/
    protected void insertToSql(int page, final List<MulAdBean> recommendList, final String fromWhere){

        if(1 == page){
            MyApplication.getDbManager().deleteWallpagerBeanList(fromWhere);

        }

        //TODO 线程加入到数据库中 -- 先删除，后添加
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpagerBean wallpagerBean;
                AdBean adBean ;
                ApiAdBean apiAdBean;
                for (MulAdBean bean: recommendList) {
                    if(bean.getItemType() == MulAdBean.TYPE_ONE){
                        adBean = bean.adBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(adBean.getType());
                        wallpagerBean.setIs_collected(adBean.getIs_collected());
                        wallpagerBean.setMovie_url(adBean.getMovie_url());
                        wallpagerBean.setWallpager_id(adBean.getId());
                        wallpagerBean.setNickname(adBean.getNickname());
                        wallpagerBean.setTitle(adBean.getTitle());
                        wallpagerBean.setHead_img(adBean.getHead_img());
                        wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                        apiAdBean = bean.apiAdBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(apiAdBean.getType());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }
                }
            }
        }).start();
    }



}
