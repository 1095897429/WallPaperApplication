package com.ngbj.wallpaper.mvp.contract.app;

import com.ngbj.wallpaper.base.BaseContract;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;

import java.util.List;

/***
 * 壁纸详情页契约类
 */
public interface DetailContract {

    interface View extends BaseContract.BaseView{
        void showDetailData(AdBean adBean);

        void showReportData();
        void showRecordData();

        void showDeleteCollection();

        void showMoreRecommendData(List<MulAdBean> recommendList);

        void showMoreKeySearchData(List<MulAdBean> recommendList);
        void showMoreNavigationData(List<MulAdBean> list);
        void showMoreHotSearchData(List<MulAdBean> list);

        void showIndexRecommendData(List<MulAdBean> list);

        void showAlertAd(AdBean adBean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T>{
        //明细
        void getDetailData(String wallpaperId);

        void getReportData(String wallpaperId,String type);
        void getRecordData(String wallpaperId,String type);

        void getDeleteCollection(String wallpaperId);

        void getMoreRecommendData(int page,String category,String order);//默认加载下一页数据 -- 主页

        void getIndexRecommendData(int page);//主界面

        //关键字搜索 -- 搜索页
        void getMoreKeySearchData(int page,String keyWord);

        //酷站导航 -- 搜索页
        void getMoreNavigationData(int page,String navigation);

        //热搜壁纸搜索 -- 搜索页
        void getMoreHotSearchData(int page,String hotSearchTag);


        void getAlertAd();
    }
}
