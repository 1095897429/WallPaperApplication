package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaesLogicActivity;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.BannerDetailBean;
import com.ngbj.wallpaper.bean.entityBean.DetailParamBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.LoveEvent;
import com.ngbj.wallpaper.eventbus.fragment.LoveSpecialEvent;
import com.ngbj.wallpaper.mvp.contract.app.SpecialContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.mvp.presenter.app.SpecialPresenter;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.StatusBarUtil;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 专题详情页
 * 1.没有加载更多
 */
public class SpecialActivity extends BaesLogicActivity<SpecialPresenter>
            implements SpecialContract.View {


//    ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据



    @BindView(R.id.title_part2)
    TextView title_part2;

    @BindView(R.id.move_top)
    RelativeLayout moveTop;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @BindView(R.id.headBack)
    RelativeLayout headBack;


    private View headView;
    private ImageView imageView;
    private TextView title;
    private TextView content;

    private ImageView back;

    String mBannerId;
    Context mContext;
    private int mImageViewHeight;//图片高度



    @Override
    protected int getLayoutId() {
        return R.layout.activity_specail;
    }

    @Override
    protected void initPresenter() {
        mPresenter =  new SpecialPresenter();
    }

    int scrollY;
    @Override
    protected void initEvent() {

        //点击事件
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                MulAdBean mulAdBean = recommendList.get(position);
                if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                    KLog.d("tag -- 广告");

                    //不能用静态方法，导致内存泄漏
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("loadUrl", mulAdBean.adBean.getLink());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }else{
                    KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());


                    DetailParamBean bean = new DetailParamBean();
                    bean.setPage(1);
                    bean.setPosition(position);
                    bean.setWallpagerId(mulAdBean.adBean.getId());
                    bean.setFromWhere(AppConstant.SPECIAL);

//                    DetailActivity.openActivity(mContext,bean,temps);

                    //不能用静态方法，导致内存泄漏
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",bean);
                    bundle.putSerializable("list",null);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

        //壁纸喜好
        recomendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AdBean mAdBean = recommendList.get(position).adBean;

                if("0".equals(mAdBean.getIs_collected())){
                    mAdBean.setIs_collected("1");
                    ToastHelper.customToastView(SpecialActivity.this,"收藏成功");
                    mPresenter.getRecordData(mAdBean.getId(),"2");
                    updateLove(position,true);
                }else{
                    mAdBean.setIs_collected("0");
                    ToastHelper.customToastView(SpecialActivity.this,"取消收藏");
                    mPresenter.getDeleteCollection(mAdBean.getId());
                    updateLove(position,false);
                }
                //刷新全部可见item
                recomendAdapter.notifyDataSetChanged();


            }
        });


        //设置滑动事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;
//                KLog.d("scrollY:" + scrollY);
                if(scrollY > mImageViewHeight){
                    headBack.setVisibility(View.VISIBLE);

                    if(scrollY > mImageViewHeight + AppHelper.dp2px(SpecialActivity.this,180)){
                        moveTop.setVisibility(View.VISIBLE);
                    }else
                        moveTop.setVisibility(View.GONE);

                }else
                    headBack.setVisibility(View.INVISIBLE);
            }
        });


        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void addHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.specail_head,null);
        imageView = headView.findViewById(R.id.imageView);
        title = headView.findViewById(R.id.title);
        content = headView.findViewById(R.id.content);
        back = headView.findViewById(R.id.back);
    }

    /** 获取 view 的宽高 */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mImageViewHeight = imageView.getHeight();
            KLog.d("onWindowFocusChanged width=" + imageView.getWidth() + " height=" + imageView.getHeight());
        }
    }



    @Override
    protected void initData() {
        mContext = this;
        mBannerId = getIntent().getExtras().getString("bannerId");
//        bannerImageUrl = getIntent().getExtras().getString("bannerImageUrl");
//        bannerTitle = getIntent().getExtras().getString("bannerTitle");

        initRecommandRecycleView();
        mPresenter.getRecommendData(mBannerId);

        //TODO 1.11
//        StatusBarUtil.setTransparentForWindow(this);
    }

    private void initRecommandRecycleView() {
        recomendAdapter = new RecomendAdapter(recommendList);
        gridLayoutManager = new GridLayoutManager(this,2);
        //设置占据的列数 -- 根据实体中的类型
        recomendAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return recommendList.get(position).getItemType();
            }
        });
        //设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        mRecyclerView.setAdapter(recomendAdapter);
        //一行代码开启动画 默认CUSTOM动画
        recomendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置头布局
        recomendAdapter.addHeaderView(headView);

    }


    /** =================== 接口方法回调  开始 =================== */

    @Override
    public void showRecommendData(BannerDetailBean bannerDetailBean,List<MulAdBean> list) {
        //设置标题
        title_part2.setText(bannerDetailBean.getTitle());
        //设置内容
        content.setText(bannerDetailBean.getComment());
        //设置标题
        title.setText(bannerDetailBean.getTitle());
        //大图
        if(!TextUtils.isEmpty(bannerDetailBean.getImg_url())){

            Glide.with(MyApplication.getInstance())
                    .load(bannerDetailBean.getImg_url())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        this.recommendList = list;
        recomendAdapter.setNewData(recommendList);
        KLog.d("size: " + list.size());

        /** 构建临时变量  */
//        temps.addAll(transformDataToWallpaper(list));

        commonDtaLogin(AppConstant.SPECIAL,false,list);
    }


    @Override
    public void showMoreRecommendData(List<MulAdBean> list) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(list);

//        temps.addAll(transformDataToWallpaper(list));

        commonDtaLogin(AppConstant.SPECIAL,true,list);
    }

    @Override
    public void showRecordData() {

    }

    @Override
    public void showDeleteCollection() {

    }

    /** =================== 接口方法回调  结束 =================== */

    @OnClick(R.id.move_top)
    public void MoveTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }

    @OnClick(R.id.back_2)
    public void Back2(){
        finish();
    }

    /** =================== EventBus  开始 =================== */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        addHeadView();
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(LoveEvent event){
//        boolean isLove = event.isLove();
//        boolean isReset = event.isReset();
//        int page = event.getPage();
//        if(!isReset){ //不需要更新全体数据
//            MulAdBean mulAdBean= recommendList.get(event.getPosition());
//            mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
//            temps.get(event.getPosition()).setIs_collected(isLove ? "1" : "0");
//        }else{
//            temps.addAll(transformDataToWallpaper(event.getMulAdBeanList()));
//            recomendAdapter.addData(event.getMulAdBeanList());
//        }
//
//        recomendAdapter.notifyDataSetChanged();

        String fromWhere = event.getFromWhere();
        KLog.d("明细中发送过来的fromWhere是：" + fromWhere);
        if(fromWhere.equals(AppConstant.SPECIAL)){
            boolean isLove = event.isLove();
            MulAdBean mulAdBean= recommendList.get(event.getPosition());
            mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
            recomendAdapter.notifyDataSetChanged();

            WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpager(mulAdBean.adBean.getId(),AppConstant.SPECIAL);
            wallpagerBean.setIs_collected(isLove ? "1" : "0");
            MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);

        }
    }

    /** 主界面喜好修改 temps修改 */
    private void updateLove(int position,boolean isLove) {

//        MulAdBean mulAdBean= recommendList.get(position);
//        if(isLove){
//            mulAdBean.adBean.setIs_collected("1");
//            temps.get(position).setIs_collected("1");
//        }else{
//            mulAdBean.adBean.setIs_collected("0");
//            temps.get(position).setIs_collected("0");
//        }
//
//        recomendAdapter.notifyDataSetChanged();


        MulAdBean mulAdBean= recommendList.get(position);
        if(isLove){
            mulAdBean.adBean.setIs_collected("1");
//            temps.get(position).setIs_collected("1");
            WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpager(mulAdBean.adBean.getId(),AppConstant.SPECIAL);
            wallpagerBean.setIs_collected("1");
            MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
        }else{
            mulAdBean.adBean.setIs_collected("0");
//            temps.get(position).setIs_collected("0");
            WallpagerBean wallpagerBean = MyApplication.getDbManager().queryWallpager(mulAdBean.adBean.getId(),AppConstant.SPECIAL);
            wallpagerBean.setIs_collected("0");
            MyApplication.getDbManager().updateWallpagerBean(wallpagerBean);
        }

        recomendAdapter.notifyDataSetChanged();
    }


    /** =================== EventBus  结束 =================== */




}
