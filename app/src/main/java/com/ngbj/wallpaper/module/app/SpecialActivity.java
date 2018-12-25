package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaesLogicActivity;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.BannerDetailBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.app.SpecialContract;
import com.ngbj.wallpaper.mvp.presenter.app.LoginPresenter;
import com.ngbj.wallpaper.mvp.presenter.app.SpecialPresenter;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.socks.library.KLog;

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


    @BindView(R.id.move_top)
    RelativeLayout moveTop;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @BindView(R.id.headBack)
    RelativeLayout headBack;




    private View headView;
    private ImageView imageView;
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
                    WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                }else{
                    KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());
                    DetailActivityNew.openActivity(mContext,position,mulAdBean.adBean.getId(),AppConstant.SPECIAL);
                }
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

                    if(scrollY > mImageViewHeight + StringUtils.dp2px(SpecialActivity.this,180)){
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        addHeadView();
        super.onCreate(savedInstanceState);
    }

    private void addHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.specail_head,null);
        imageView = headView.findViewById(R.id.imageView);
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
        initRecommandRecycleView();
        mPresenter.getRecommendData(mBannerId);
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


    @Override
    public void showRecommendData(BannerDetailBean bannerDetailBean,List<MulAdBean> recommendList) {
        this.recommendList = recommendList;
        recomendAdapter.setNewData(recommendList);
        KLog.d("size: " + recommendList.size());
        insertToSql(1,recommendList,AppConstant.SPECIAL);
    }


    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);
        insertToSql(2,recommendList,AppConstant.SPECIAL);
    }

    @OnClick(R.id.move_top)
    public void MoveTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }

    @OnClick(R.id.back_2)
    public void Back2(){
        finish();
    }




}
