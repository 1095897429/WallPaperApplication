package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.IndexBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.List;


public interface SearchContract {

    interface View extends BaseContract.BaseView{

        void showHistoryData(List<HistoryBean> historys);
        void showRecommendData(List<MulAdBean> recommendList);

        /** -------------------------------------- */
        void showEndView();

        void showKeySearchData(List<MulAdBean> list);
        void showMoreKeySearchData(List<MulAdBean> recommendList);

        void showNavigationData(List<MulAdBean> list);
        void showMoreNavigationData(List<MulAdBean> list);

        void showHotSearchData(List<MulAdBean> list);
        void showMoreHotSearchData(List<MulAdBean> list);

        void showHotWordsAndAd(List<IndexBean.HotSearch> hotWords, List<AdBean> ads);

        void showRecordData();
        void showDeleteCollection();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{

        void getHistoryData();

        //关键字搜索
        void getMoreKeySearchData(int page,String keyWord);
        void getKeySearchData(int page,String keyWord);

        //酷站导航
        void getNavigationData(int page,String navigation);
        void getMoreNavigationData(int page,String navigation);

        //热搜壁纸搜索
        void getHotSearchData(int page,String hotSearchTag);
        void getMoreHotSearchData(int page,String hotSearchTag);

        //搜索页
        void getHotWordsAndAd();

        //喜好
        void getRecordData(String wallpaperId, String type);
        void getDeleteCollection(String wallpaperId);
    }
}
