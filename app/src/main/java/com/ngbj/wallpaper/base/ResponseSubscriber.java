package com.ngbj.wallpaper.base;

import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.socks.library.KLog;


import io.reactivex.subscribers.ResourceSubscriber;

public abstract class ResponseSubscriber<T>
        extends ResourceSubscriber<T> {



    @Override
    protected void onStart() {
        super.onStart();
        // 判断网络
        KLog.d("显示对话框");

    }


    @Override
    public void onNext(T response) {

        onSuccess(response);

    }


    public abstract void onSuccess(T t);


    @Override

    public void onComplete() {
        KLog.d("数据返回信息 结束对话框");
    }


    @Override
    public void onError(Throwable t) {

    }
}