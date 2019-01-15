package com.ngbj.wallpaper.base;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/***
 * 基础的Presenter
 */

public class RxPresenter<T extends BaseContract.BaseView>
            implements BaseContract.BasePresenter<T>{

    protected T mView;
    private CompositeDisposable mCompositeDisposable;

    /** 新增管理 */
    protected void addSubscribe(Disposable disposable){
        if(null == mCompositeDisposable){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /** 删除管理 */
    protected boolean removeSubscribe(Disposable disposable){
        return mCompositeDisposable != null && mCompositeDisposable.remove(disposable);
    }

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        unSubscribe();
    }

    /** 切断水管   */
    private void unSubscribe() {
        if(null != mCompositeDisposable){
            mCompositeDisposable.dispose();
        }
    }


    /** 将获取的数据 根据类型重构成新的Bean  -- 首页 + 搜索*/
    protected List<MulAdBean> getMulAdBeanData(List<AdBean> adBeanList) {
        List<AdBean> recommendList = adBeanList;
        List<MulAdBean> mulAdBeanList = new ArrayList<>();
        if (!recommendList.isEmpty()) {
            AdBean adBean;
            ApiAdBean apiAdBean;
            MulAdBean mulAdBean;
            for (int i = 0; i < recommendList.size(); i++) {
                adBean = recommendList.get(i);
                if (adBean.getType().equals("3")) {//占据两行广告
                    apiAdBean = new ApiAdBean();
                    apiAdBean.setAd_id(adBean.getAd_id());
                    apiAdBean.setLink(adBean.getLink());
                    apiAdBean.setImgUrl(adBean.getImg_url());
                    apiAdBean.setType(adBean.getType());
                    apiAdBean.setName(adBean.getTitle());
                    mulAdBean = new MulAdBean(MulAdBean.TYPE_TWO, MulAdBean.AD_SPAN_SIZE, apiAdBean);
                } else {//正常
                    mulAdBean = new MulAdBean(MulAdBean.TYPE_ONE, MulAdBean.ITEM_SPAN_SIZE, adBean);
                }
                mulAdBeanList.add(mulAdBean);
            }
        }
        return mulAdBeanList;
    }

}
