package com.ngbj.wallpaper.mvp.presenter.app;

import android.widget.Toast;

import com.google.gson.Gson;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.BannerDetailBean;
import com.ngbj.wallpaper.bean.entityBean.BannerListBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.app.SpecialContract;
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

public class SpecialPresenter extends RxPresenter<SpecialContract.View>
        implements SpecialContract.Presenter<SpecialContract.View> {


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
    public void getRecommendData(String bannerId) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("bannerId",bannerId);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .banner(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            BannerListBean bannerListBean = gson.fromJson(result,BannerListBean.class);
                            BannerDetailBean bannerDetailBean = bannerListBean.getInfo();
                            List<AdBean> recommendList = bannerListBean.getList();
                            List<MulAdBean> list = new ArrayList<>();//重新构造的函数
                            if(!recommendList.isEmpty()){
                                AdBean adBean;
                                MulAdBean mulAdBean;

                                for (int i = 0; i < recommendList.size(); i++) {
                                    adBean = recommendList.get(i);
                                    //正常
                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
                                    list.add(mulAdBean);
                                }
                            }
                            mView.showRecommendData(bannerDetailBean,list);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .banner(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<BannerListBean>(mView){
//                    @Override
//                    public void onSuccess(BannerListBean bannerListBean) {
//                        BannerDetailBean bannerDetailBean = bannerListBean.getInfo();
//                        List<AdBean> recommendList = bannerListBean.getList();
//                        List<MulAdBean> list = new ArrayList<>();//重新构造的函数
//                        if(!recommendList.isEmpty()){
//                            AdBean adBean;
//                            MulAdBean mulAdBean;
//
//                            for (int i = 0; i < recommendList.size(); i++) {
//                                adBean = recommendList.get(i);
//                                //正常
//                                mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
//                                list.add(mulAdBean);
//                            }
//                        }
//                        mView.showRecommendData(bannerDetailBean,list);
//                    }
//                }));
    }



//    @Override
//    public void getRecommendData() {
//        //测试数据 -- 后台模拟19条数据
//        List<MulAdBean> list = new ArrayList<>();
//        MulAdBean mulAdBean;
//        ApiAdBean apiAdBean;
//        AdBean adBean;
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
//        mView.showRecommendData(list);
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



    @Override
    public void getMoreRecommendData() {
        //测试数据
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
        mView.showMoreRecommendData(list);

    }
}
