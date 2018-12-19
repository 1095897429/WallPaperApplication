package com.ngbj.wallpaper.module.app;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.app.Interest_Adapter;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.mvp.contract.app.InterestContract;
import com.ngbj.wallpaper.mvp.presenter.app.InterestPresenter;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.socks.library.KLog;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.Request;

/***
 * 1.presenter传递给父类，已实例化，子类直接拿对象调用方法
 */
public class InterestActivity extends BaseActivity<InterestPresenter>
            implements InterestContract.View{

    @BindView(R.id.interest_recyclerView)
    RecyclerView interest_recyclerView;

    GridLayoutManager layoutManager;
    List<InterestBean> interestBeanList = new ArrayList<>();
    Interest_Adapter interestAdapter;
    List<String> selectList = new ArrayList<>();//集合存储选择中的兴趣ID

    @Override
    protected int getLayoutId() {
        return R.layout.activity_interest;
    }


    @Override
    protected void initPresenter() {
        mPresenter = new InterestPresenter();
    }

    @Override
    protected void initData() {
        initRecycleView();
        mPresenter.getInterestData();
    }

    @Override
    protected void initEvent() {
        //兴趣爱好Item点击
        interestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InterestBean interestBean = interestBeanList.get(position);
                KLog.d("点击的条目 " + interestBean.getName() + " 兴趣ID " + interestBean.getIs_interested());
                if(interestBean.isSelect()){
                    interestBean.setSelect(false);
                    selectList.remove(interestBean.getIs_interested());
                }else{
                    interestBean.setSelect(true);
                    selectList.add(interestBean.getIs_interested());
                }
                if(!selectList.isEmpty()){
                    for (int i = 0; i < selectList.size(); i++) {
                        KLog.d("兴趣ID有 " + selectList.get(i));
                    }

                }

            interestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecycleView() {
        layoutManager = new GridLayoutManager(this,3);
        //设置布局管理器
        interest_recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        interestAdapter = new Interest_Adapter(interestBeanList);
        interest_recyclerView.setAdapter(interestAdapter);
        //一行代码开启动画 默认CUSTOM动画
        interestAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }


    @Override
    public void showInterestData(List<InterestBean> list) {
        interestBeanList.addAll(list);
        interestAdapter.setNewData(interestBeanList);
    }

    @Override
    public void showWriteInterestData(String string) {
        KLog.d(string);
    }


    @OnClick(R.id.interest_done)
    public void InterestDone(){
        //TODO TEST
        selectList.add("1");
        KLog.d("选中的数量:" + selectList.size());
        Gson gson = new Gson();
        String jsonStringlist = gson.toJson(selectList);
        KLog.d("转为" + jsonStringlist);

        Object object = gson.fromJson(jsonStringlist,Object.class);
        KLog.d("object:" + object.toString());

//        KLog.d("转为" + jsonStringlist);


//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("interest",selectList);
//        hashMap.put("fromPlat", "default");
//        hashMap.put("appVersion", AppHelper.getPackageName(MyApplication.getInstance()));
//        hashMap.put("deviceId", AppHelper.getUniquePsuedoID());
//        hashMap.put("deviceType", "android");
//        hashMap.put("timestamp", System.currentTimeMillis() + "");
//        hashMap.put("sign", "");
//        String strEntity = gson.toJson(hashMap);
//        KLog.d("转为" + strEntity);

        mPresenter.writeInterestData(selectList);

        startActivity(new Intent(InterestActivity.this,HomeActivity.class));
        finish();
    }

}
