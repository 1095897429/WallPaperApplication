package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.SearchBean;
import com.ngbj.wallpaper.mvp.contract.app.SearchContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class SearchPresenter extends RxPresenter<SearchContract.View>
                implements SearchContract.Presenter<SearchContract.View> {

    /** 取消收藏 */
    @Override
    public void getDeleteCollection(String wallpaperId) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

//        addSubscribe(RetrofitHelper.getApiService()
//                .deleteCollection(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String string) {
//                        mView.showDeleteCollection();
//                    }
//                }));

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .deleteCollection(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            mView.showDeleteCollection();
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }



    /** 记录用户下载 1下载 2收藏 3分享 */
    @Override
    public void getRecordData(String wallpaperId, String type) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("type",type);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .record(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            mView.showRecordData();
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .record(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String string) {
//                        mView.showRecordData();
//                    }
//                }));
    }




    /** 首页热搜请求壁纸数据 */
    @Override
    public void getHotSearchData(final int page, String hotSearchTag) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("hotSearchTag",hotSearchTag);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);



        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .hotSearchList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());

                            Type type = new TypeToken<List<AdBean>>() {}.getType();
                            List<AdBean> adBeanList = gson.fromJson(result, type);

                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(adBeanList);


                            /** 根据 page判断是否是第一页  */
                            if(page == 1){
                                mView.showHotSearchData(list);
                            }else
                                mView.showMoreHotSearchData(list);


                            if(list.isEmpty() ){
                                mView.showEndView();
                                return;
                            }

                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .hotSearchList(page,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(adBeanList);
//
//
//                        /** 根据 page判断是否是第一页  */
//                        if(page == 1){
//                            mView.showHotSearchData(list);
//                        }else
//                            mView.showMoreHotSearchData(list);
//
//
//                        if(list.isEmpty() ){
//                            mView.showEndView();
//                            return;
//                        }
//
//                    }
//                }));
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


        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .navigationList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());

                            Type type = new TypeToken<List<AdBean>>() {}.getType();
                            List<AdBean> adBeanList = gson.fromJson(result, type);

                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(adBeanList);

                            /** 根据 page判断是否是第一页  */
                            if(page == 1){
                                mView.showNavigationData(list);
                            }else
                                mView.showMoreNavigationData(list);

                            if(list.isEmpty()){
                                mView.showEndView();
                                return;
                            }


                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .navigationList(page,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(adBeanList);
//
//                        /** 根据 page判断是否是第一页  */
//                        if(page == 1){
//                            mView.showNavigationData(list);
//                        }else
//                            mView.showMoreNavigationData(list);
//
//                        if(list.isEmpty()){
//                            mView.showEndView();
//                            return;
//                        }
//
//                    }
//                }));
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



        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .search(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());

                            Type type = new TypeToken<List<AdBean>>() {}.getType();
                            List<AdBean> adBeanList = gson.fromJson(result, type);

                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(adBeanList);

                            /** 根据 page判断是否是第一页  */
                            if(page == 1){
                                mView.showKeySearchData(list);
                            }else
                                mView.showMoreKeySearchData(list);


                            if(list.isEmpty()){
                                mView.showEndView();
                                return;
                            }


                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .search(page,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(adBeanList);
//
//                        /** 根据 page判断是否是第一页  */
//                        if(page == 1){
//                            mView.showKeySearchData(list);
//                        }else
//                            mView.showMoreKeySearchData(list);
//
//
//                        if(list.isEmpty()){
//                            mView.showEndView();
//                            return;
//                        }
//
//                    }
//                }));
    }

    @Override
    public void getMoreKeySearchData(int page,String keyWord) {
        getKeySearchData(page,keyWord);
    }

    /** 搜索页 */
    @SuppressLint("CheckResult")
    @Override
    public void getHotWordsAndAd() {


        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .searchPage(OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            SearchBean searchBean = gson.fromJson(result,SearchBean.class);
                            List<IndexBean.HotSearch> hotSearches = searchBean.getHotSearch();
                            List<AdBean> adlist = searchBean.getAd();
                            mView.showHotWordsAndAd(hotSearches,adlist);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .searchPage(OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<SearchBean>(mView) {
//                    @Override
//                    public void onSuccess(SearchBean searchBean) {
//                        List<IndexBean.HotSearch> hotSearches = searchBean.getHotSearch();
//                        List<AdBean> adlist = searchBean.getAd();
//                        mView.showHotWordsAndAd(hotSearches,adlist);
//                    }
//                }));
    }

    @Override
    public void getHistoryData() {
//        MyApplication.getDbManager().deleteAllHistoryBean();
        List<HistoryBean> historyBeanList = MyApplication.getDbManager().queryHistoryList();
        if(historyBeanList.isEmpty()){
//            historyBeanList.add(new HistoryBean("奇葩说"));
//            historyBeanList.add(new HistoryBean("猫妖传"));
//            historyBeanList.add(new HistoryBean("吐槽大会"));
//            historyBeanList.add(new HistoryBean("开心麻花"));
//            MyApplication.getDbManager().insertHistoryList(historyBeanList);//插入
            return;
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
