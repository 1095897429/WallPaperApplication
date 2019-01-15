package com.ngbj.wallpaper.mvp.presenter.fragment;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.base.ResponseSubscriber;
import com.ngbj.wallpaper.base.RxPresenter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
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

public class MyPresenter extends RxPresenter<MyContract.View>
        implements MyContract.Presenter<MyContract.View> {



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




    /** 用户上传壁纸记录，必须登录 */
    @Override
    public void getUploadHistory(String accessToken) {

        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .uploadHistory(accessToken,OkHttpHelper.getRequestBody(null))
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

                            /** 只有首页第一次加载才获取到上面的数据 */
                            List<MulAdBean> mulAdBeanList = getMulAdBeanData(adBeanList);

                            mView.showUploadHistory(mulAdBeanList);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

//        addSubscribe(RetrofitHelper.getApiService()
//                .uploadHistory(accessToken,OkHttpHelper.getRequestBody(null))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> list) {
//
//                        List<MulAdBean> mulAdBeanList = getMulAdBeanData(list);
//
//                        mView.showUploadHistory(mulAdBeanList);
//                    }
//                }));
    }


    /** 修改用户头像 */
    @Override
    public void getUploadHeadData(String accessToken,String base64Img) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("base64Img",base64Img);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);


        // TODO 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .uploadHeadImage(accessToken,requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResponseSubscriber<ResponseBody>(mView) {
                    @Override
                    public void onSuccess(HttpResponse response) {
                        KLog.d(response.getCode());
                        if(response.getCode() == 200){
                            Gson gson = new Gson();
                            String result =  gson.toJson(response.getData());
                            LoginBean loginBean = gson.fromJson(result,LoginBean.class);
                            KLog.d("result: " + loginBean.getHead_img());
                            mView.showUploadHeadData(loginBean);
                        }else{
                            mView.showError(response.getMessage());
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .uploadHeadImage(accessToken,requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseObjectSubscriber<LoginBean>(mView) {
//                    @Override
//                    public void onSuccess(LoginBean loginBean) {
//                        KLog.d("result: " + loginBean.getHead_img());
//                        mView.showUploadHeadData(loginBean);
//                    }
//                }));
    }


    /** 用户下载、分享、收藏壁纸信息 */
    @Override
    public void getRecord(String type) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("type",type);
        RequestBody requestBody = OkHttpHelper.getRequestBody(hashMap);

        // TODO 2019.1.14 全方位解密测试
        addSubscribe(RetrofitHelper.getApiService()
                .getRecord(requestBody)
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
                            List<AdBean> list = gson.fromJson(result, type);

                            List<MulAdBean> mulAdBeanList = getMulAdBeanData(list);

                            mView.showRecord(mulAdBeanList);
                        }else{
                            Toast.makeText(MyApplication.getInstance(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


//        addSubscribe(RetrofitHelper.getApiService()
//                .getRecord(requestBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new BaseListSubscriber<AdBean>(mView) {
//                    @Override
//                    public void onSuccess(List<AdBean> list) {
//                        KLog.d("result: " + list.size());
//
//                        List<MulAdBean> mulAdBeanList = getMulAdBeanData(list);
//
//                        mView.showRecord(mulAdBeanList);
//                    }
//                }));
    }









//    @Override
//    public void getMoreData(String type) {
//        //模拟数据
//        List<AdBean> adBeanList = setFakeData();
//
//        mView.showMoreData(adBeanList);
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
