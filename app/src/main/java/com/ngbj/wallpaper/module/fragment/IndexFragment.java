package com.ngbj.wallpaper.module.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.index.IndexCoolAdapter;
import com.ngbj.wallpaper.adapter.index.IndexCoolGridAdapter;
import com.ngbj.wallpaper.adapter.index.Index_HotSearch_Adapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaesLogicFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.DetailParamBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.LoveEvent;
import com.ngbj.wallpaper.eventbus.fragment.ChangeFragmentEvent;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.SearchActivity;
import com.ngbj.wallpaper.module.app.SpecialActivity;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.IndexContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.IndexPresenter;
import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.widget.EmptyView;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexFragment extends BaesLogicFragment<IndexPresenter>
                implements IndexContract.View{


    @BindView(R.id.emptyView)
    EmptyView emptyView;

    @BindView(R.id.move_top)
    RelativeLayout moveTop;

    @BindView(R.id.recommandRecyclerView)
    RecyclerView  mRecommandRecyclerView;

    RecyclerView  hot_search_recycleView;
    Banner banner;
    ViewPager mViewpager;
    TextView more_recommend;

    List<IndexBean.Navigation> myCoolList = new ArrayList<>();
    List<IndexBean.Banner> myBannerList = new ArrayList<>();
    List<IndexBean.HotSearch> hotAdList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    Index_HotSearch_Adapter indexHotSearchAdapter;

    View headView;

    GridLayoutManager gridLayoutManager;
    RecomendAdapter recomendAdapter;
    List<MulAdBean> recommendList = new ArrayList<>();//界面临时数据
    ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据
    int mPage = 1;//默认为第一页

    IndexBean.Navigation mNavigation;//记录酷站的实体
    IndexBean.HotSearch mHotSearch;//记录热搜的实体


    /** 通过这种形式获取Fragment */
    public static IndexFragment getInstance(){
        return new IndexFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.index_fragment;
    }

    @Override
    protected void initPresenter() {
       mPresenter = new IndexPresenter();
    }


    @Override
    protected void initData() {
        initRecommandRecycleView();
        initRecycleView();
        mPresenter.getAdData(mPage);
    }

    @Override
    protected void initEvent() {

        //第一次网络加载失败点击事件
        emptyView.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getAdData(mPage);
                emptyView.setType(EmptyView.LOADING);
            }
        });

        //TODO 壁纸Item点击
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                /**  首先判断网络 */
                int netType = NetBroadcastReceiver.getNetWorkState(getActivity());
                if(AppHelper.isNetConnect(netType)){

                    MulAdBean mulAdBean = recommendList.get(position);
                    if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                        if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
//                            WebViewActivity.openActivity(mContext,"https://www.baidu.com/");

                            KLog.d("url: ",mulAdBean.adBean.getLink());
                            //不能用静态方法，导致内存泄漏
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("loadUrl", mulAdBean.adBean.getLink());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }else  if(mulAdBean.adBean.getType().equals(AppConstant.API_AD)){
                            //不能用静态方法，导致内存泄漏
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("loadUrl", mulAdBean.apiAdBean.getLink());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }else{
                            DetailParamBean bean = new DetailParamBean();
                            bean.setPage(mPage);
                            bean.setPosition(position);
                            bean.setWallpagerId(mulAdBean.adBean.getId());
                            bean.setFromWhere(AppConstant.INDEX);

//                            DetailActivity.openActivity(mContext,bean,temps);

                            //不能用静态方法，导致内存泄漏
                            Intent intent = new Intent(mContext, DetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean",bean);
                            bundle.putSerializable("list",temps);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);


                        }

                    }else {
                        KLog.d("tag -- api广告",mulAdBean.apiAdBean.getLink());
                        //不能用静态方法，导致内存泄漏
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("loadUrl", mulAdBean.apiAdBean.getLink());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }else
                    ToastHelper.customToastView(mContext,"网络异常，请先连接网络");
            }
        });

        //壁纸喜好
        recomendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AdBean mAdBean = recommendList.get(position).adBean;

                if("0".equals(mAdBean.getIs_collected())){
                    mAdBean.setIs_collected("1");
                    ToastHelper.customToastView(getActivity(),"收藏成功");
                    mPresenter.getRecordData(mAdBean.getId(),"2");
                    updateLove(position,true);
                }else{
                    mAdBean.setIs_collected("0");
                    ToastHelper.customToastView(getActivity(),"取消收藏");
                    mPresenter.getDeleteCollection(mAdBean.getId());
                    updateLove(position,false);
                }
                //刷新全部可见item
                recomendAdapter.notifyDataSetChanged();


            }
        });



        //更多
        more_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               EventBus.getDefault().post(new ChangeFragmentEvent());
            }
        });

        //热搜点击事件
        indexHotSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mHotSearch = hotAdList.get(position);
                KLog.d("tag",mHotSearch.getTitle());
                chooseActivity(AppConstant.FROMINDEX_HOTSEACHER);
            }
        });

        //banner点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                IndexBean.Banner banner = myBannerList.get(position);

//                SpecialActivity.openActivity(getActivity(),banner.getId(),
//                        banner.getImg_url(),banner.getTitle());

                if(banner.getType().equals(AppConstant.API_AD) ||
                        banner.getType().equals(AppConstant.COMMON_AD)){

                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("loadUrl", banner.getLink());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }else{
                    Intent intent = new Intent(mContext, SpecialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bannerId",banner.getId());
                    bundle.putString("bannerImageUrl",banner.getImg_url());
                    bundle.putString("bannerTitle",banner.getTitle());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

        //滑动监听
        mRecommandRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void addHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.index_head,null);
        hot_search_recycleView = headView.findViewById(R.id.recyclerView);
        banner = headView.findViewById(R.id.banner);
        mViewpager = headView.findViewById(R.id.viewpager);
        more_recommend = headView.findViewById(R.id.more_recommend);
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
        mRecommandRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        mRecommandRecyclerView.setAdapter(recomendAdapter);
        //一行代码开启动画 默认CUSTOM动画
        recomendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        //加载更多数据
        recomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ++mPage;
                mPresenter.getMoreRecommendData(mPage);
            }
        },mRecommandRecyclerView);
        //设置头布局
        recomendAdapter.addHeaderView(headView);
        //不让其自动滑动到顶部
        mRecommandRecyclerView.setFocusableInTouchMode(false);
        mRecommandRecyclerView.setFocusable(false);
//        mRecommandRecyclerView.setHasFixedSize(true);

    }


    private void initRecycleView() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置布局管理器
        hot_search_recycleView.setLayoutManager(layoutManager);
        //设置Adapter
        indexHotSearchAdapter = new Index_HotSearch_Adapter(hotAdList);
        hot_search_recycleView.setAdapter(indexHotSearchAdapter);
        //一行代码开启动画 默认CUSTOM动画
        indexHotSearchAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);


    }


    @Override
    public void showEndView() {
        recomendAdapter.loadMoreEnd();//加载结束
        return;
    }

    @Override
    public void showAdData(List<IndexBean.HotSearch> list, List<IndexBean.Banner> bannerList,
                           List<IndexBean.Navigation> coolList) {
        hotAdList.addAll(list);
        indexHotSearchAdapter.setNewData(list);
        myBannerList.addAll(bannerList);
        startBanner();
        myCoolList = coolList;
        setCoolData(myCoolList);
    }

    @Override
    public void showRecommendData(final List<MulAdBean> recommendList) {

        /** 第一次加载数据结束 -- Error HIDE  NODATA*/
        emptyView.setType(EmptyView.HIDE);
        mRecommandRecyclerView.setVisibility(View.VISIBLE);

        this.recommendList = recommendList;
        recomendAdapter.setNewData(recommendList);
        /** 构建临时变量  */
        temps.addAll(transformDataToWallpaper(recommendList));
    }




    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);

        temps.addAll(transformDataToWallpaper(recommendList));
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

    /** 设置 酷站数据 -- GridView数据 */
    private void setCoolData(final List<IndexBean.Navigation> myCoolList) {
        List<View> viewPagerList = new ArrayList<>();
        GridView gridView;
        IndexCoolGridAdapter indexCoolAdapter;
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        int mPageSize = 8;//每页显示的最大数量
        int totalPage = (int) Math.ceil(myCoolList.size() * 1.0 / mPageSize);
        for (int i = 0; i < totalPage; i++){
            gridView = (GridView) layoutInflater.inflate(R.layout.index_cool,mViewpager,false);
            indexCoolAdapter = new IndexCoolGridAdapter(getActivity(),myCoolList,i,mPageSize);
            gridView.setAdapter(indexCoolAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mNavigation = myCoolList.get(position);
                    KLog.d("选择的cool ：" + mNavigation.getTitle());

                    if(mNavigation.getType().equals(AppConstant.COMMON_AD) ||
                            mNavigation.getType().equals(AppConstant.API_AD)){
                            Intent intent = new Intent(mContext, WebViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("loadUrl", mNavigation.getLink());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                    }else
                        chooseActivity(AppConstant.FROMINDEX_NAVICATION);
                }
            });
            viewPagerList.add(gridView);
        }

        mViewpager.setAdapter(new IndexCoolAdapter(viewPagerList));
    }

    /** 3个 跳转到搜索界面  */
    private void chooseActivity(int type) {

        Intent intent = new Intent(mContext,SearchActivity.class);
        Bundle bundle = new Bundle();

        if(type == AppConstant.FROMINDEX_NAVICATION){
            bundle.putInt(AppConstant.FROMWHERE,AppConstant.FROMINDEX_NAVICATION);
            bundle.putString(AppConstant.NAVICATIONID,mNavigation.getId());
            bundle.putString(AppConstant.HOTSEARCHTAG,"");

        }else if(type == AppConstant.FROMINDEX_HOTSEACHER){
            bundle.putInt(AppConstant.FROMWHERE,AppConstant.FROMINDEX_HOTSEACHER);
            bundle.putString(AppConstant.NAVICATIONID,"");
            bundle.putString(AppConstant.HOTSEARCHTAG,mHotSearch.getTitle());
        }else if(type == AppConstant.FROMINDEX_SEACHER){
            bundle.putInt(AppConstant.FROMWHERE,AppConstant.FROMINDEX_SEACHER);
            bundle.putString(AppConstant.NAVICATIONID,"");
            bundle.putString(AppConstant.HOTSEARCHTAG,"");
        }

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }


    private void startBanner() {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(myBannerList);
        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片简单用法
            IndexBean.Banner banner = (IndexBean.Banner) path;
            Glide.with(context).load(banner.getImg_url()).centerCrop().into(imageView);
        }
    }


    @OnClick(R.id.search_part)
    public void SearchPart(){
        chooseActivity(AppConstant.FROMINDEX_SEACHER);
    }


    @OnClick(R.id.move_top)
    public void MoveTop(){
        mRecommandRecyclerView.smoothScrollToPosition(0);
    }


    @Override
    public void showError(String msg) {
        KLog.d("msg",msg);
        if(recomendAdapter.isLoadMoreEnable())
            recomendAdapter.loadMoreFail();
    }

    /** =================== EventBus  开始 =================== */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addHeadView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLoveEvent(LoveEvent event){
        boolean isLove = event.isLove();
        boolean isReset = event.isReset();
        int page = event.getPage();
        if(!isReset){ //不需要更新全体数据
            MulAdBean mulAdBean= recommendList.get(event.getPosition());
            mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
            temps.get(event.getPosition()).setIs_collected(isLove ? "1" : "0");
        }else{
            mPage = page;
            temps.addAll(transformDataToWallpaper(event.getMulAdBeanList()));
            recomendAdapter.addData(event.getMulAdBeanList());
        }

        recomendAdapter.notifyDataSetChanged();
    }

    /** 主界面喜好修改 temps修改 */
    private void updateLove(int position,boolean isLove) {

        MulAdBean mulAdBean= recommendList.get(position);
        if(isLove){
            mulAdBean.adBean.setIs_collected("1");
            temps.get(position).setIs_collected("1");
        }else{
            mulAdBean.adBean.setIs_collected("0");
            temps.get(position).setIs_collected("0");
        }

        recomendAdapter.notifyDataSetChanged();
    }


    /** =================== EventBus  结束 =================== */
}
