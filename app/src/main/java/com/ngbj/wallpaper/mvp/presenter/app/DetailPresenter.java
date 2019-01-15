package com.ngbj.wallpaper.mvp.presenter.app;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.app.DetailContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/***
 * 传入具体的View,继承RxPresenter是为了防止重复写attachView detachView
 */
public class DetailPresenter extends RxPresenter<DetailContract.View>
                implements DetailContract.Presenter<DetailContract.View> {


    /** 下载广告 */
    @Override
    public void getAlertAd() {


        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .alertAd(OkHttpHelper.getRequestBody(null))
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

                            mView.showAlertAd(adBeanList.get(0));
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .alertAd(OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        mView.showAlertAd(adBeanList.get(0));
//
//                    }
//                }));
    }


    /** 主界面 */
    @Override
    public void getIndexRecommendData(int page) {

        addSubscribe(RetrofitHelper.getApiService()
                .index(page,OkHttpHelper.getRequestBody(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            IndexBean indexBean = gson.fromJson(result,IndexBean.class);

                            List<AdBean> recommendList = indexBean.getRecommend();
                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(recommendList);
                            mView.showIndexRecommendData(list);



                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .index(page,OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<IndexBean>(mView) {
//                    @Override
//                    public void onSuccess(IndexBean indexBean) {
//
//                        List<AdBean> recommendList = indexBean.getRecommend();
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(recommendList);
//                         mView.showIndexRecommendData(list);
//
//                    }
//                }));
    }


    /** 分类界面 */
    @Override
    public void getMoreRecommendData(final int page, String category, String order) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("categoryId",category);
        hashMap.put("order",order);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .wallpagerList(page,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());

                            Type type = new TypeToken<List<AdBean>>(){}.getType();
                            List<AdBean> adBeanList = gson.fromJson(result, type);

                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(adBeanList);

                            mView.showMoreRecommendData(list);

                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .wallpagerList(page,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(adBeanList);
//
//                        mView.showMoreRecommendData(list);
//
//                    }
//                }));
    }



    /*************************** 搜索 *******************************/

    @Override
    public void getMoreKeySearchData(int page, String keyWord) {
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

                            mView.showMoreKeySearchData(list);

                        }else{
                            mView.showError(response.getMessage());
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
//                        mView.showMoreKeySearchData(list);
//
//                    }
//                }));
    }

    @Override
    public void getMoreNavigationData(int page, String navigation) {
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

                            mView.showMoreNavigationData(list);

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
//                        mView.showMoreNavigationData(list);
//
//                    }
//                }));
    }

    @Override
    public void getMoreHotSearchData(int page, String hotSearchTag) {

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

                            mView.showMoreHotSearchData(list);

                        }else{
                            mView.showError(response.getMessage());
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
//                        mView.showMoreHotSearchData(list);
//                    }
//                }));
    }


    /**************************** 搜索 ******************************/

    /** 取消收藏 */
    @Override
    public void getDeleteCollection(String wallpaperId) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);



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
    }


    /** 记录用户举报 1色情低俗 2侵犯版权 3其他*/
    @Override
    public void getReportData(String wallpaperId, String type) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);
        hashMap.put("type",type);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .report(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            mView.showReportData();
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .report(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<String>(mView) {
//                    @Override
//                    public void onSuccess(String string) {
//                        mView.showReportData();
//                    }
//                }));
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
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
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


    /** 壁纸明细 */
    @SuppressLint("CheckResult")
    @Override
    public void getDetailData(final String wallpaperId) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("wallpaperId",wallpaperId);

        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);



        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .detail(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            AdBean adBean = gson.fromJson(result,AdBean.class);
                            mView.showDetailData(adBean);
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));



//        addSubscribe(RetrofitHelper.getApiService()
//                .detail(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(AdBean item) {
//
//                        mView.showDetailData(item);
//                    }
//                }));
    }

}
