package com.ngbj.wallpaper.mvp.contract.fragment;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;

public interface CategoryContract {

    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getData(int page,String category,String order);

        void getRecommendData(int page,String category,String order);//默认
        void getMoreRecommendData(int page,String category,String order);//更多
        void getCategories();//分类


        void getRecordData(String wallpaperId, String type);
        void getDeleteCollection(String wallpaperId);

    }

    interface View extends BaseContract.BaseView{

        void showData(List<AdBean> topList, List<MulAdBean> recommendList);


        void showEndView();
        void showRecommendData(List<MulAdBean> recommendList);
        void showMoreRecommendData(List<MulAdBean> recommendList);
        void showCategories(List<InterestBean> interestBeanList);

        void showRecordData();
        void showDeleteCollection();
    }
}
