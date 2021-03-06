package com.ngbj.wallpaper.mvp.presenter.fragment;

import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.fragment.IndexContract;
import com.ngbj.wallpaper.network.helper.OkHttpHelper;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class IndexPresenter extends RxPresenter<IndexContract.View>
        implements IndexContract.Presenter<IndexContract.View> {

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


    @Override
    public void getAdData(final int page) {


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

                            /** 只有首页第一次加载才获取到上面的数据 */
                            if(page == 1){
                                List<IndexBean.HotSearch> hotSearches = indexBean.getHotSearch();
                                List<IndexBean.Banner> banners = indexBean.getBanner();
                                List<IndexBean.Navigation> navigations = indexBean.getNavigation();
                                mView.showAdData(hotSearches,banners,navigations);
                            }

                            /** 这里转换一下 */
                            List<MulAdBean> list = getMulAdBeanData(recommendList);

                            /** 根据 page判断是否是第一页  */
                            if(page == 1){
                                mView.showRecommendData(list);
                            }else
                                mView.showMoreRecommendData(list);


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
//                .index(page,OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<IndexBean>(mView) {
//                    @Override
//                    public void onSuccess(IndexBean indexBean) {
//
//                        List<AdBean> recommendList = indexBean.getRecommend();
//
//                        /** 只有首页第一次加载才获取到上面的数据 */
//                        if(page == 1){
//                            List<IndexBean.HotSearch> hotSearches = indexBean.getHotSearch();
//                            List<IndexBean.Banner> banners = indexBean.getBanner();
//                            List<IndexBean.Navigation> navigations = indexBean.getNavigation();
//                            mView.showAdData(hotSearches,banners,navigations);
//                        }
//
//                        /** 这里转换一下 */
//                        List<MulAdBean> list = getMulAdBeanData(recommendList);
//
//                        /** 根据 page判断是否是第一页  */
//                        if(page == 1){
//                            mView.showRecommendData(list);
//                        }else
//                            mView.showMoreRecommendData(list);
//
//
//                        if(list.isEmpty()){
//                            mView.showEndView();
//                            return;
//                        }
//                    }
//                }));
    }


    @Override
    public void getMoreRecommendData(int page) {
        getAdData(page);
    }

//    private void testMoreData() {
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
        adBeanList.add(new AdBean("2","0","live","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
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



//    private void testData() {
//        List<AdBean> list = new ArrayList<>();
//        list.add(new AdBean("奇葩说",""));
//        list.add(new AdBean("猫妖传",""));
//        list.add(new AdBean("吐槽大会",""));
//        list.add(new AdBean("开心麻花",""));
//
////        List<String> strings = new ArrayList<>();
////        strings.add("1");
////        strings.add("2");
////        strings.add("3");
////
////        Gson gson = new Gson();
////        String jsonString = gson.toJson(strings);
////        KLog.d("jsonString " + jsonString);
//
//        List<AdBean> bannerList = new ArrayList<>();
//        bannerList.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        bannerList.add(new AdBean("猫妖传","http://img.zcool.cn/community/018fdb56e1428632f875520f7b67cb.jpg"));
//        bannerList.add(new AdBean("吐槽大会","http://img.zcool.cn/community/01c8dc56e1428e6ac72531cbaa5f2c.jpg"));
//        bannerList.add(new AdBean("开心麻花","http://img.zcool.cn/community/01fd2756e142716ac72531cbf8bbbf.jpg"));
//        bannerList.add(new AdBean("开心麻花","http://img.zcool.cn/community/0114a856640b6d32f87545731c076a.jpg"));
//
//        List<AdBean> coolList = new ArrayList<>();
//        coolList.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("猫妖传","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("吐槽大会","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("奇葩说","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("猫妖传","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("吐槽大会","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//        coolList.add(new AdBean("开心麻花","http://img.zcool.cn/community/0166c756e1427432f875520f7cc838.jpg"));
//
//
//        mView.showAdData(list,bannerList,coolList);
//    }

}
