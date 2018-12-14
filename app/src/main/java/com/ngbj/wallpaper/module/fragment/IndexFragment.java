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
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.bean.greenBeanDao.DBManager;
import com.ngbj.wallpaper.module.app.DetailActivityNew;
import com.ngbj.wallpaper.module.app.GalleryActivity;
import com.ngbj.wallpaper.module.app.SearchActivity;
import com.ngbj.wallpaper.module.app.SpecialActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.IndexContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.IndexPresenter;
import com.ngbj.wallpaper.receiver.NetBroadcastReceiver;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexFragment extends BaseFragment<IndexPresenter>
                implements IndexContract.View{

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
    List<MulAdBean> recommendList = new ArrayList<>();
    int page = 1;//默认为第一页


    /** 通过这种形式获取Fragment */
    public static IndexFragment getInstance(){
        return new IndexFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addHeadView();
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
        mPresenter.getAdData(page);
    }

    @Override
    protected void initEvent() {
        //TODO 壁纸Item点击
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                /**  首先判断网络 */
                int netType = NetBroadcastReceiver.getNetWorkState(getActivity());
                if(AppHelper.isNetConnect(netType)){

                    MulAdBean mulAdBean = recommendList.get(position);
                    if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){
                        KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());
                        Intent intent = new Intent(getActivity(),DetailActivityNew.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position",position);
                        bundle.putString("wallpagerId",mulAdBean.adBean.getCategory_id());
                        bundle.putParcelable("bean",mulAdBean.adBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        KLog.d("tag -- 广告",recommendList.get(position).apiAdBean.getName());
                    }
                }else
                    ToastHelper.customToastView(mContext,"网络异常，请先连接网络");


            }
        });

        //壁纸喜好
        recomendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AdBean adBean = recommendList.get(position).adBean;
//                KLog.d("点击的position为: " + position);
//                KLog.d("点击之前的 tag",adBean.getIs_collected());
                if("0".equals(adBean.getIs_collected())){
                    adBean.setIs_collected("1");
                    ToastHelper.customToastView(getActivity(),"收藏成功");
                }else{
                    adBean.setIs_collected("0");
                    ToastHelper.customToastView(getActivity(),"取消收藏");
                }
//                KLog.d("点击之后的 tag",adBean.getIs_collected());
                //全局刷新
                recomendAdapter.notifyDataSetChanged();
            }
        });

        //更多
        more_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.d("to do next");
            }
        });

        //热搜点击事件
        indexHotSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("tag",hotAdList.get(position).getTitle());
            }
        });

        //banner点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
               startActivity(new Intent(getActivity(),SpecialActivity.class));
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
                ++page;
                mPresenter.getMoreRecommendData(page);
            }
        },mRecommandRecyclerView);
        //设置头布局
        recomendAdapter.addHeaderView(headView);
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
        this.recommendList = recommendList;
        recomendAdapter.setNewData(recommendList);

        MyApplication.getDbManager().deleteAllWallpagerBean();

        //TODO 线程加入到数据库中 -- 先删除，后添加
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpagerBean wallpagerBean;
                AdBean adBean ;
                for (MulAdBean bean: recommendList) {
                    if(bean.getItemType() == MulAdBean.TYPE_ONE){
                        adBean = bean.adBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setCategory_id(adBean.getCategory_id());
                        wallpagerBean.setNickname(adBean.getNickname());
                        wallpagerBean.setTitle(adBean.getTitle());
                        wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }
                }
            }
        }).start();
    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);
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
                    KLog.d("选择的cool ：" + myCoolList.get(position).getTitle());
                }
            });
            viewPagerList.add(gridView);
        }

        mViewpager.setAdapter(new IndexCoolAdapter(viewPagerList));
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
        startActivity(new Intent(getActivity(),SearchActivity.class));
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
}
