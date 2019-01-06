package com.ngbj.wallpaper.module.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.my.MyCommonAdapter;
import com.ngbj.wallpaper.base.BaseRefreshFragment;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.DetailParamBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.eventbus.LoveCreateEvent;
import com.ngbj.wallpaper.module.app.DetailActivity;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * 创作Fragment
 */
public class CreateFragment extends BaseRefreshFragment<MyPresenter,MulAdBean>
            implements MyContract.View{


    MyCommonAdapter myCommonAdapter;
    GridLayoutManager gridLayoutManager;
    String mType;
    String fromWhere = "";//来源

    ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据
    HashMap<String ,ArrayList<WallpagerBean>> mHashMap = new HashMap<>();//key -- category  value -- list



    public static CreateFragment getInstance(String type){
        CreateFragment mFragment = new CreateFragment();
        // 通过bundle传递数据
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        mFragment.setArguments(bundle);
        return mFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.create_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyPresenter();
    }


    @Override
    protected void initRecyclerView() {
        myCommonAdapter = new MyCommonAdapter(recommendList);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        //设置布局管理器
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //设置Adapter
        mRecyclerView.setAdapter(myCommonAdapter);

        //一行代码开启动画 默认CUSTOM动画
        myCommonAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        myCommonAdapter.bindToRecyclerView(mRecyclerView);
        //设置空布局
        myCommonAdapter.setEmptyView(R.layout.commom_empty);
        //事件
        myCommonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                KLog.d("选择的--- ：" + recommendList.get(position).adBean.getTitle());

                MulAdBean mulAdBean = recommendList.get(position);
                if(mulAdBean.getItemType() == MulAdBean.TYPE_ONE){

                    if(mulAdBean.adBean.getType().equals(AppConstant.COMMON_AD)){
                        KLog.d("tag -- 广告");
                        WebViewActivity.openActivity(mContext,"https://www.baidu.com/");
                    }else{
                        KLog.d("tag -- 正常",recommendList.get(position).adBean.getTitle());


//                        if(AppConstant.COLLECTION.equals(mType)){
//                            fromWhere = AppConstant.MY_2;
//                        }else if(AppConstant.SHARE.equals(mType)){
//                            fromWhere = AppConstant.MY_3;
//                        }else if(AppConstant.DOWNLOAD.equals(mType)){
//                            fromWhere = AppConstant.MY_4;
//                        }



                        DetailParamBean bean = new DetailParamBean();
                        bean.setPage(1);
                        bean.setPosition(position);
                        bean.setWallpagerId(mulAdBean.adBean.getId());
                        bean.setFromWhere(AppConstant.MY_1);

                        //从不同的集合中获取
                        ArrayList<WallpagerBean> mytemp = mHashMap.get(mType);

                        DetailActivity.openActivity(mContext,bean,mytemp);

                    }

                }else {
                    KLog.d("tag -- 广告",recommendList.get(position).apiAdBean.getName());
                }

            }
        });

        //壁纸喜好
        myCommonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                myCommonAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void lazyAgainLoadData() {
        mPresenter.getRecord(mType);
    }



    @Override
    public void showUploadHistory(List<MulAdBean> list) {
        complete();
        recommendList.addAll(list);
        myCommonAdapter.setNewData(recommendList);


        //临时数据清空
        temps.clear();
        temps.addAll(transformDataToWallpaper(list));
        mHashMap.put(mType,temps);
    }


    @Override
    public void showUploadHeadData(LoginBean loginBean) {

    }

    @Override
    public void showRecord(List<MulAdBean> list) {
        complete();
        recommendList.addAll(list);
        myCommonAdapter.setNewData(recommendList);

        temps.addAll(transformDataToWallpaper(list));
        mHashMap.put(mType,temps);
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
        mType = getArguments().getString("type");
        KLog.d("类型： " + mType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    @Subscribe
    public void onLoveCreateEvent(LoveCreateEvent event){
        boolean isLove = event.isLove();
        int position = event.getPosition();
        MulAdBean mulAdBean= recommendList.get(position);
        mulAdBean.adBean.setIs_collected(isLove ? "1" : "0");
        mHashMap.get(mType).get(position).setIs_collected(isLove ? "1" : "0");

        myCommonAdapter.notifyDataSetChanged();

    }



    /** 主界面喜好修改 */
    private void updateLove(int position,boolean isLove) {

        MulAdBean mulAdBean= recommendList.get(position);
        if(isLove){
            mulAdBean.adBean.setIs_collected("1");
            mHashMap.get(mType).get(position).setIs_collected("1");
        }else{
            mulAdBean.adBean.setIs_collected("0");
            mHashMap.get(mType).get(position).setIs_collected("0");
        }


        myCommonAdapter.notifyDataSetChanged();

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





    /** 隐藏加载进度框 */
//    @Override
//    public void complete() {
//
//        if(mRefresh != null)
//            mRefresh.setRefreshing(false);
//
//        if(mIsRefreshing){
//            if(recommendList != null && !recommendList.isEmpty()){
//                recommendList.clear();
//                KLog.d("刷新成功");
//            }
//        }
//        mIsRefreshing = false;
//    }


}
