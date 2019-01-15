package com.ngbj.wallpaper.mvp.presenter.fragment;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.fragment.CategoryContract;
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

public class CategoryPresenter extends RxPresenter<CategoryContract.View>
        implements CategoryContract.Presenter<CategoryContract.View> {

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
                            mView.showError(response.getMessage());
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



    /** 兴趣爱好中的数据 */
    @Override
    public void getCategories() {

        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .categories(OkHttpHelper.getRequestBody(new HashMap<String, Object>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            Type type = new TypeToken<List<InterestBean>>(){}.getType();
                            List<InterestBean> interestBeanList = gson.fromJson(result, type);

                            mView.showCategories(interestBeanList);
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .categories(OkHttpHelper.getRequestBody(new HashMap<String, Object>()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<InterestBean>(mView) {
//                    @Override
//                    public void onSuccess(List<InterestBean> interestBeanList) {
//                        mView.showCategories(interestBeanList);
//                    }
//                }));
    }

    /** 壁纸列表页中的数据 */
    @Override
    public void getRecommendData(final int page, String category, String order) {
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

                            /** 根据 page判断是否是第一页  */
                            if(page == 1){
                                mView.showRecommendData(list);
                            }else
                                mView.showMoreRecommendData(list);


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
//                        /** 根据 page判断是否是第一页  */
//                        if(page == 1){
//                            mView.showRecommendData(list);
//                        }else
//                            mView.showMoreRecommendData(list);
//
//
//                        if(list.isEmpty() ){
//                            mView.showEndView();
//                            return;
//                        }
//
//
//                        }
//                }));
    }


    @Override
    public void getMoreRecommendData(int page,String catogory,String order) {
        getRecommendData(page,catogory,order);
    }



    /** 壁纸列表页中的数据 */
    @Override
    public void getData(final int page, String catogory, String order) {

//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("categoryId",catogory);
//        hashMap.put("order",order);
//        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);
//
//        addSubscribe(RetrofitHelper.getApiService()
//                .wallpagerList(page,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> adBeanList) {
//
//                        //模拟数据
//                        List<AdBean> topList = new ArrayList<>();
//                        topList.add(new AdBean("推荐",""));
//                        topList.add(new AdBean("风景建筑",""));
//                        topList.add(new AdBean("明星写真",""));
//                        topList.add(new AdBean("开心麻花",""));
//                        topList.add(new AdBean("体育竞技",""));
//                        topList.add(new AdBean("明星写真",""));
//                        topList.add(new AdBean("开心麻花",""));
//
//                        List<AdBean> recommendList = adBeanList;
//                        /** 这里转换一下 */
//                        if(!recommendList.isEmpty()){
//                            AdBean adBean;
//                            ApiAdBean apiAdBean;
//                            MulAdBean mulAdBean;
//                            List<MulAdBean> list = new ArrayList<>();
//                            for (int i = 0; i < recommendList.size(); i++) {
//                                adBean = recommendList.get(i);
//                                if( adBean.getType().equals("3")){//广告
//                                    apiAdBean = new ApiAdBean();
//                                    apiAdBean.setName(adBean.getTitle());
//                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO,MulAdBean.AD_SPAN_SIZE,apiAdBean);
//                                }else{//正常
//                                    mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE,MulAdBean.ITEM_SPAN_SIZE,adBean);
//                                }
//                                list.add(mulAdBean);
//                            }
//
//                            /** 根据 page判断是否是第一页  */
//                            if(page == 1){
//                                mView.showData(topList,list);
//                            }else
//                                mView.showMoreRecommendData(list);
//
//                        }
//                    }
//                }));
    }





    /** ----------------------------  以下是测试数据 ------------------------------------------ */

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

    //测试数据
    public void testMore(){
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

    public void test(){
        //模拟数据
        List<AdBean> topList = new ArrayList<>();
        topList.add(new AdBean("推荐",""));
        topList.add(new AdBean("风景建筑",""));
        topList.add(new AdBean("明星写真",""));
        topList.add(new AdBean("开心麻花",""));
        topList.add(new AdBean("体育竞技",""));
        topList.add(new AdBean("明星写真",""));
        topList.add(new AdBean("开心麻花",""));


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


        mView.showData(topList,list);
    }

}
