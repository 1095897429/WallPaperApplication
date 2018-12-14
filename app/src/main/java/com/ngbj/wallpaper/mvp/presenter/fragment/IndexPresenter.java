package com.ngbj.wallpaper.mvp.presenter.fragment;

import com.ngbj.wallpaper.base.BaseObjectSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.InitUserBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.mvp.contract.fragment.IndexContract;
import com.ngbj.wallpaper.network.helper.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IndexPresenter extends RxPresenter<IndexContract.View>
        implements IndexContract.Presenter<IndexContract.View> {

    @Override
    public void getAdData(int page) {

        addSubscribe(RetrofitHelper.getApiService()
                .index(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<IndexBean>(mView) {
                    @Override
                    public void onSuccess(IndexBean indexBean) {
                        List<IndexBean.HotSearch> hotSearches = indexBean.getHotSearch();
                        List<IndexBean.Banner> banners = indexBean.getBanner();
                        List<IndexBean.Navigation> navigations = indexBean.getNavigation();
                        List<AdBean> recommendList = indexBean.getRecommend();
                        mView.showAdData(hotSearches,banners,navigations);
                        /** 这里转换一下 */
                        if(!recommendList.isEmpty()){
                            AdBean adBean;
                            ApiAdBean apiAdBean;
                            MulAdBean mulAdBean;
                            List<MulAdBean> list = new ArrayList<>();
                            for (int i = 0; i < recommendList.size(); i++) {
                                adBean = recommendList.get(i);

                                if( adBean.getType().equals(AppConstant.API_AD)){//广告
                                    apiAdBean = new ApiAdBean();
                                    apiAdBean.setName(adBean.getTitle());
                                    apiAdBean.setImgUrl(adBean.getThumb_img_url());
                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO,MulAdBean.AD_SPAN_SIZE,apiAdBean);
                                }else{//正常
                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
                                }
                                list.add(mulAdBean);
                            }
                            mView.showRecommendData(list);
                        }
                    }
                }));
    }


    @Override
    public void getMoreRecommendData(int page) {
        addSubscribe(RetrofitHelper.getApiService()
                .index(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObjectSubscriber<IndexBean>(mView) {
                    @Override
                    public void onSuccess(IndexBean indexBean) {
                        List<AdBean> recommendList = indexBean.getRecommend();
                        /** 这里转换一下 */
                        if(!recommendList.isEmpty()){
                            AdBean adBean;
                            ApiAdBean apiAdBean;
                            MulAdBean mulAdBean;
                            List<MulAdBean> list = new ArrayList<>();
                            for (int i = 0; i < recommendList.size(); i++) {
                                adBean = recommendList.get(i);
                                if( adBean.getType().equals("3")){//广告
                                    apiAdBean = new ApiAdBean();
                                    apiAdBean.setName(adBean.getTitle());
                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO,MulAdBean.AD_SPAN_SIZE,apiAdBean);
                                }else{//正常
                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
                                }
                                list.add(mulAdBean);
                            }
                            mView.showMoreRecommendData(list);
                        }
                    }
                }));
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


    @Override
    public void getRecommendData() {
        //测试数据 -- 后台模拟19条数据
        List<MulAdBean> list = new ArrayList<>();
        MulAdBean mulAdBean;
        ApiAdBean apiAdBean;
        AdBean adBean;
        List<AdBean> adBeanList = setFakeData();


        for (int i = 0; i < 19; i++) {
            adBean = adBeanList.get(i);
            if( adBean.getType().equals("3")){//广告
                apiAdBean = new ApiAdBean();
                apiAdBean.setName("我是广告");
                mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO,MulAdBean.AD_SPAN_SIZE,apiAdBean);
            }else{//正常
                mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
            }
            list.add(mulAdBean);
        }
        mView.showRecommendData(list);

    }

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
