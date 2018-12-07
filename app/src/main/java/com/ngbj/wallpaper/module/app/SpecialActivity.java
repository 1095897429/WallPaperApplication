package com.ngbj.wallpaper.module.app;

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
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
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
 */
public class SpecialActivity extends BaseActivity<SpecialPresenter>
            implements SpecialContract.View {


    @BindView(R.id.move_top)
    RelativeLayout moveTop;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @BindView(R.id.headBack)
    RelativeLayout headBack;


    RecomendAdapter mRecomendAdapter;
    GridLayoutManager gridLayoutManager;
    List<MulAdBean> recommendList = new ArrayList<>();

    private View headView;
    private ImageView imageView;
    private ImageView back;


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
        //设置滑动事件
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;
                KLog.d("scrollY:" + scrollY);
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

        //item点击事件
        mRecomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(SpecialActivity.this,DetailActivityNew.class));
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
        initRecommandRecycleView();
        mPresenter.getRecommendData();
    }

    private void initRecommandRecycleView() {
        mRecomendAdapter = new RecomendAdapter(recommendList);
        gridLayoutManager = new GridLayoutManager(this,2);
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
                mPresenter.getMoreRecommendData();
            }
        },mRecyclerView);
        //设置头布局
        mRecomendAdapter.addHeaderView(headView);

    }


    @Override
    public void showRecommendData(List<MulAdBean> recommendList) {
        this.recommendList = recommendList;
        mRecomendAdapter.setNewData(recommendList);
    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        mRecomendAdapter.loadMoreComplete();
        mRecomendAdapter.addData(recommendList);
    }

    @OnClick(R.id.move_top)
    public void MoveTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }



}
