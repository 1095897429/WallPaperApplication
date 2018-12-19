package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.app.History_Search_Adapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;
import com.ngbj.wallpaper.mvp.presenter.app.SearchPresenter;
import com.ngbj.wallpaper.utils.common.StringUtils;
import com.ngbj.wallpaper.utils.widget.EditTextWithDel;
import com.socks.library.KLog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 搜索关键字 + 首页导航 + 热搜 -- 共用一个界面
 */

public class SearchActivity extends BaseActivity<SearchPresenter>
            implements SearchContract.View,SwipeRefreshLayout.OnRefreshListener {



    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.edittext)
    EditTextWithDel editText;

    @BindView(R.id.part1)
    ConstraintLayout part1;

    @BindView(R.id.part2)
    ConstraintLayout part2;//搜索列表

    @BindView(R.id.ad_part)
    ConstraintLayout ad_part;

    @BindView(R.id.tagflowlayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.history_recyclerView)
    RecyclerView history_recyclerView;

    LinearLayoutManager layoutManager;
    History_Search_Adapter historySearchAdapter;
    List<HistoryBean> historyList = new ArrayList<>();
    List<IndexBean.HotSearch> hotWords = new ArrayList<>();
    List<AdBean> ads = new ArrayList<>();

    Context mContext;
    int page = 1;
    String keyWord;
    int fromWhere;
    String navigationId;//导航栏的Id
    String hotSearchTag;//热搜词

    /** type -- 来源  navId -- 酷站id  hotSearchTag -- 热搜词 */
    public static void openActivity(Context context,int type,String navigationId,
                                    String hotSearchTag){
        Intent intent = new Intent(context,SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.FROMWHERE,type);
        bundle.putString(AppConstant.NAVICATIONID,navigationId);
        bundle.putString(AppConstant.HOTSEARCHTAG,hotSearchTag);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SearchPresenter();
    }

    @Override
    protected void initData() {
        mContext = this;
        fromWhere = getIntent().getExtras().getInt(AppConstant.FROMWHERE);
        KLog.d("来源是：" + fromWhere);

        //TODO 通过来源显示不同的界面
        initRefreshLayout();
        initRecycleView();
        initHistoryRecycleView();
        mPresenter.getHotWordsAndAd();
        mPresenter.getHistoryData();

        if(fromWhere == AppConstant.FROMINDEX_NAVICATION){
            navigationId = getIntent().getExtras().getString(AppConstant.NAVICATIONID);
            mPresenter.getNavigationData(page,navigationId);
            part2.setVisibility(View.VISIBLE);
            part1.setVisibility(View.GONE);
        }else if(fromWhere == AppConstant.FROMINDEX_HOTSEACHER){
            hotSearchTag = getIntent().getExtras().getString(AppConstant.HOTSEARCHTAG);
            mPresenter.getHotSearchData(page,hotSearchTag);
            part2.setVisibility(View.VISIBLE);
            part1.setVisibility(View.GONE);
        }else if(fromWhere == AppConstant.FROMINDEX_SEACHER){

        }


    }

    @Override
    protected void initEvent() {

        //热搜的点击
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                IndexBean.HotSearch hotSearch = hotWords.get(position);
                KLog.d("选择的标签是：" + hotSearch.getTitle());

                hotSearchTag = hotSearch.getTitle();
                searchContent(AppConstant.FROMINDEX_HOTSEACHER,hotSearch.getTitle());

                editText.setText(hotSearch.getTitle());
                part2.setVisibility(View.VISIBLE);
                part1.setVisibility(View.GONE);
                return false;
            }
        });

        //历史记录点击
        historySearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HistoryBean historyBean = historyList.get(position);
                KLog.d("历史记录：" + historyBean.getHistoryName());
                searchContent(AppConstant.FROMINDEX_SEACHER,historyBean.getHistoryName());
                //TODO 打开列表页，隐藏历史记录页
                editText.setText(historyBean.getHistoryName());
                part2.setVisibility(View.VISIBLE);
                part1.setVisibility(View.GONE);
            }
        });

        //历史删除点击
        historySearchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                HistoryBean historyBean = historyList.get(position);
                KLog.d("删除历史记录：" + historyBean.getHistoryName());
                historyList.remove(position);
                historySearchAdapter.notifyDataSetChanged();
                MyApplication.getDbManager().deleteHistory(historyBean);
            }
        });

        //软键盘 -- 搜索
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){//搜索按键action
                    String content = editText.getText().toString().trim();
                    if(TextUtils.isEmpty(content)){
                        KLog.d("不能搜索空内容");
                        return true;
                    }

                    //隐藏软键盘
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    KLog.d("搜索内容:" + content);
                    searchContent(AppConstant.FROMINDEX_SEACHER,content);

                    //TODO 打开列表页，隐藏历史记录页
                    editText.setText(content);
                    part2.setVisibility(View.VISIBLE);
                    part1.setVisibility(View.GONE);
                }
                return false;
            }
        });

        //EditText的内容监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //内容为空 显示历史记录页
                if(TextUtils.isEmpty(s.toString().trim())){
                    part1.setVisibility(View.VISIBLE);
                    part2.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initHistoryRecycleView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        history_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        historySearchAdapter = new History_Search_Adapter(historyList);
        history_recyclerView.setAdapter(historySearchAdapter);
        //一行代码开启动画 默认CUSTOM动画
        historySearchAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }


    @Override
    public void showHistoryData(List<HistoryBean> historys) {
        historyList.addAll(historys);
        historySearchAdapter.setNewData(historyList);

    }


    private void initAds(List<AdBean> ads) {
        if(ads.isEmpty()){
            ad_part.setVisibility(View.GONE);
        }
    }

    private void initHotSearchWords(List<IndexBean.HotSearch> hotWords) {
        tagFlowLayout.setAdapter(new TagAdapter<IndexBean.HotSearch>(hotWords) {
            @Override
            public View getView(FlowLayout parent, int position, IndexBean.HotSearch adBean) {
                TextView tag_text = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.hot_item,tagFlowLayout,false);
                tag_text.setText(adBean.getTitle());
                return tag_text;
            }
        });
    }

    @OnClick(R.id.cancel)
    public void Cancel(){
        finish();
    }


    @OnClick(R.id.search_ad)
    public void SearchAd(){
       KLog.d("点击ad");
    }


    @OnClick(R.id.delete_ad)
    public void DeleteAd(){
        ad_part.setVisibility(View.GONE);
    }


    /** 搜索内容 */
    private void searchContent(int fromW,String content){

        keyWord = "动态壁纸";
        fromWhere = fromW;

        /** 点击热搜，走首页热搜的逻辑  */
        if(fromWhere == AppConstant.FROMINDEX_HOTSEACHER){
            mPresenter.getHotSearchData(page,hotSearchTag);
        }else{
            mPresenter.getKeySearchData(page,keyWord);
        }

        HistoryBean historyBean = MyApplication.getDbManager().queryHistory(content);
        if(null == historyBean){
            historyBean = new HistoryBean(content,StringUtils.getDate(SearchActivity.this)[0]);
            MyApplication.getDbManager().insertHistrory(historyBean);
        }else{
            historyBean.setClickTime(StringUtils.getDate(SearchActivity.this)[0]);
            MyApplication.getDbManager().updateHistory(historyBean);
        }
    }


    /** -----------------------  搜索列表页 -----------------------*/
    GridLayoutManager gridLayoutManager;
    RecomendAdapter recomendAdapter;
    List<MulAdBean> recommendList = new ArrayList<>();

    private void initRecycleView() {
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
        recycler.setLayoutManager(gridLayoutManager);
        //设置Adapter
        recycler.setAdapter(recomendAdapter);
        //一行代码开启动画 默认CUSTOM动画
        recomendAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        //加载更多数据
        recomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                ++page;
                toMoreDiffFunc();
            }
        },recycler);

        //设置空布局
        recomendAdapter.setEmptyView(R.layout.commom_empty);
        //设置自定义加载布局
//        recomendAdapter.setLoadMoreView(new CustomLoadMoreView());

        //点击事件
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                DetailActivityNew.openActivity(mContext,position,recommendList.get(position).adBean.getId());
            }
        });
    }



    @Override
    public void showRecommendData(List<MulAdBean> recommendList) {
        this.recommendList = recommendList;
        recomendAdapter.setNewData(recommendList);
    }


    private void toDiffFunc(){
        page = 1;
        if(fromWhere == AppConstant.FROMINDEX_NAVICATION){
            mPresenter.getNavigationData(page,navigationId);
        }else if(fromWhere == AppConstant.FROMINDEX_HOTSEACHER){
            mPresenter.getHotSearchData(page,hotSearchTag);
        }else
            mPresenter.getKeySearchData(page,keyWord);
    }


    private void toMoreDiffFunc(){
        if(fromWhere == AppConstant.FROMINDEX_NAVICATION){
            mPresenter.getMoreNavigationData(page,navigationId);
        }else if(fromWhere == AppConstant.FROMINDEX_HOTSEACHER){
            mPresenter.getMoreHotSearchData(page,hotSearchTag);
        }else
            mPresenter.getMoreKeySearchData(page,keyWord);
    }


    @OnClick(R.id.ad_part)
    public void AdPart(){
        //TODO
        KLog.d("点击广告");
    }


    /** -- 根据热搜词搜索壁纸 -- */
    @Override
    public void showKeySearchData(List<MulAdBean> list) {
        recommendList = list;
        recomendAdapter.setNewData(list);
    }

    @Override
    public void showMoreKeySearchData(List<MulAdBean> list) {

        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(list);
    }


    /** -- 酷站搜索壁纸 -- */
    @Override
    public void showNavigationData(List<MulAdBean> list) {
        recommendList = list;
        recomendAdapter.setNewData(list);
    }

    @Override
    public void showMoreNavigationData(List<MulAdBean> list) {

        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(list);
    }

    /** -- 根据热搜词搜索壁纸 -- */
    @Override
    public void showHotSearchData(List<MulAdBean> list) {
        recommendList = list;
        recomendAdapter.setNewData(list);
    }

    @Override
    public void showMoreHotSearchData(List<MulAdBean> list) {

        recomendAdapter.loadMoreComplete();//本次数据加载结束并且还有下页数据
        recomendAdapter.addData(list);
    }

    @Override
    public void showHotWordsAndAd(List<IndexBean.HotSearch> hotWords, List<AdBean> ads) {
        this.hotWords = hotWords;
        this.ads = ads;
        initHotSearchWords(hotWords);
        initAds(ads);
    }


    @Override
    public void showEndView() {
        recomendAdapter.loadMoreEnd();//加载结束
        return;
    }

    /** -------------- 下拉刷新操作  -------------- */
    private boolean mIsRefreshing = false;//第一次或者手动的下拉操作

    /** 设置一些属性 去掉自动刷新*/
    protected void initRefreshLayout(){
        if(null != mRefresh){
            mRefresh.setColorSchemeResources(R.color.colorPrimary);
//            mRefresh.post(new Runnable() {
//                @Override
//                public void run() {
//                    mRefresh.setRefreshing(true);
//                    toDiffFunc();
//                }
//            });
            mRefresh.setOnRefreshListener(this);
        }
    }


    @Override
    public void onRefresh() {
        mIsRefreshing = true;
        toDiffFunc();
    }

    /** 隐藏加载进度框 */
    @Override
    public void complete() {

        if(mRefresh != null)
            mRefresh.setRefreshing(false);

        if(mIsRefreshing){
            if(recommendList != null && !recommendList.isEmpty()){
                recommendList.clear();
                KLog.d("刷新成功");
            }
        }
        mIsRefreshing = false;
    }

}
