package com.ngbj.wallpaper.base;

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


}
