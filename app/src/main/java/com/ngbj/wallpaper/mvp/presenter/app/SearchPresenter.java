package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;

import com.ngbj.wallpaper.base.BaseListSubscriber;
import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.SearchBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;


public class SearchPresenter extends RxPresenter<SearchContract.View>
                implements SearchContract.Presenter<SearchContract.View> {


    /** 首页热搜请求壁纸数据 */
    @Override
    public void getHotSearchData(final int page, String hotSearchTag) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("hotSearchTag",hotSearchTag);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        addSubscribe(RetrofitHelper.getApiService()
                .hotSearchList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);


                        /** 根据 page判断是否是第一页  */
                        if(page == 1){
                            mView.showHotSearchData(list);
                        }else
                            mView.showMoreHotSearchData(list);


                        if(list.isEmpty() || list.size() < AppConstant.PAGESIZE){
                            mView.showEndView();
                            return;
                        }

                    }
                }));
    }

    @Override
    public void getMoreHotSearchData(int page, String hotSearchTag) {
        getHotSearchData(page,hotSearchTag);
    }


    /** 首页导航请求壁纸数据 */
    @Override
    public void getNavigationData(final int page, String navigation) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("navigationId",navigation);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .navigationList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        /** 根据 page判断是否是第一页  */
                        if(page == 1){
                            mView.showNavigationData(list);
                        }else
                            mView.showMoreNavigationData(list);

                        if(list.isEmpty() || list.size() < AppConstant.PAGESIZE){
                            mView.showEndView();
                            return;
                        }

                    }
                }));
    }

    @Override
    public void getMoreNavigationData(int page, String navigation) {
        getNavigationData(page,navigation);
    }


    /** 关键字搜索 -- 获取AdBean,然后转化为MulAdBean */
    @Override
    public void getKeySearchData(final int page, String keyWord) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("keyWord",keyWord);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        addSubscribe(RetrofitHelper.getApiService()
                .search(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
                    @Override
                    public void onSuccess(List<AdBean> adBeanList) {

                        /** 这里转换一下 */
                        List<MulAdBean> list = getMulAdBeanData(adBeanList);

                        /** 根据 page判断是否是第一页  */
                        if(page == 1){
                            mView.showKeySearchData(list);
                        }else
                            mView.showMoreKeySearchData(list);


                        if(list.isEmpty() || list.size() < AppConstant.PAGESIZE){
                            mView.showEndView();
                            return;
                        }

                    }
                }));
    }

    @Override
    public void getMoreKeySearchData(int page,String keyWord) {
        getKeySearchData(page,keyWord);
    }

    /** 搜索页 */
    @SuppressLint("CheckResult")
    @Override
    public void getHotWordsAndAd() {

        addSubscribe(RetrofitHelper.getApiService()
                .searchPage(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<SearchBean>(mView) {
                    @Override
                    public void onSuccess(SearchBean searchBean) {
                        List<IndexBean.HotSearch> hotSearches = searchBean.getHotSearch();
                        List<AdBean> adlist = searchBean.getAd();
                        mView.showHotWordsAndAd(hotSearches,adlist);
                    }
                }));
    }

    @Override
    public void getHistoryData() {
//        MyApplication.getDbManager().deleteAllHistoryBean();
        List<HistoryBean> historyBeanList = MyApplication.getDbManager().queryHistoryList();
        if(historyBeanList.isEmpty()){
            historyBeanList.add(new HistoryBean("奇葩说"));
            historyBeanList.add(new HistoryBean("猫妖传"));
            historyBeanList.add(new HistoryBean("吐槽大会"));
            historyBeanList.add(new HistoryBean("开心麻花"));
            MyApplication.getDbManager().insertHistoryList(historyBeanList);//插入
        }

        mView.showHistoryData(historyBeanList);
    }



//    @Override
//    public void getMoreRecommendData() {
//        //测试数据
//        List<MulAdBean> list = new ArrayList<>();
//        MulAdBean mulAdBean;
//        ApiAdBean apiAdBean;
//        AdBean adBean;
//
//        List<AdBean> adBeanList = setFakeData();
//
//
//        for (int i = 0; i < 19; i++) {
//            adBean = adBeanList.get(i);
//            if( adBean.getType().equals("3")){//广告
//                apiAdBean = new ApiAdBean();
//                apiAdBean.setName("我是广告");
//                mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO,MulAdBean.AD_SPAN_SIZE,apiAdBean);
//            }else{//正常
//                mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
//            }
//            list.add(mulAdBean);
//        }
//        mView.showMoreRecommendData(list);
//
//    }



    private List<AdBean> setFakeData() {
        List<AdBean> adBeanList = new ArrayList<>();
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("0","0","广告","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","1","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("2","0","动态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","广告","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","1","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));

        adBeanList.add(new AdBean("3","api广告","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));

        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
        adBeanList.add(new AdBean("1","0","静态壁纸","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));

        return adBeanList;
    }



}
