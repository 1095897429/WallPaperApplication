package com.ngbj.wallpaper.module.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
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
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
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


public class SearchActivity extends BaseActivity<SearchPresenter>
            implements SearchContract.View {


    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.edittext)
    EditTextWithDel editText;

    @BindView(R.id.part1)
    ConstraintLayout part1;

    @BindView(R.id.part2)
    ConstraintLayout part2;

    @BindView(R.id.ad_part)
    ConstraintLayout ad_part;

    @BindView(R.id.tagflowlayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.history_recyclerView)
    RecyclerView history_recyclerView;

    LinearLayoutManager layoutManager;
    History_Search_Adapter historySearchAdapter;
    List<HistoryBean> historyList = new ArrayList<>();
    List<AdBean> hotWords = new ArrayList<>();
    List<AdBean> ads = new ArrayList<>();

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
        initRecycleView();
        initHistoryRecycleView();
        mPresenter.getHotWordsAndAd();
        mPresenter.getHistoryData();
        mPresenter.getRecommendData();
    }

    @Override
    protected void initEvent() {

        //热搜的点击
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                AdBean adBean = hotWords.get(position);
                KLog.d("选择的标签是：" + adBean.getTitle());

                searchContent(adBean.getTitle());
                //TODO 打开列表页，隐藏历史记录页
                editText.setText(adBean.getTitle());
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
                searchContent(historyBean.getHistoryName());
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
                    searchContent(content);

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
    public void showHotWordsAndAd(List<AdBean> hotWords, List<AdBean> ads) {
        this.hotWords = hotWords;
        this.ads = ads;
        initHotSearchWords(hotWords);
        initAds(ads);
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

    private void initHotSearchWords(List<AdBean> hotWords) {
        tagFlowLayout.setAdapter(new TagAdapter<AdBean>(hotWords) {
            @Override
            public View getView(FlowLayout parent, int position, AdBean adBean) {
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


    private void searchContent(String content){
        HistoryBean historyBean = MyApplication.getDbManager().queryHistory(content);
        if(null == historyBean){
            historyBean = new HistoryBean(content,StringUtils.getDate(SearchActivity.this)[0]);
            MyApplication.getDbManager().insertHistrory(historyBean);
        }else{
            historyBean.setClickTime(StringUtils.getDate(SearchActivity.this)[0]);
            MyApplication.getDbManager().updateHistory(historyBean);
        }
    }


    /** -----------------------  搜索列表页  -----------------------*/
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
                mPresenter.getMoreRecommendData();
            }
        },recycler);
        //点击事件
        recomendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(SearchActivity.this,DetailActivityNew.class));
            }
        });
    }



    @Override
    public void showRecommendData(List<MulAdBean> recommendList) {
        this.recommendList = recommendList;
        recomendAdapter.setNewData(recommendList);
    }

    @Override
    public void showMoreRecommendData(List<MulAdBean> recommendList) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(recommendList);
    }







}
