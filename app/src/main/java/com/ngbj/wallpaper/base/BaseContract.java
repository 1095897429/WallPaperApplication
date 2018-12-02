package com.ngbj.wallpaper.base;

/***
 * 基础契约类
 */

public interface BaseContract {

    interface BasePresenter<T>  {
        void attachView(T view); /** 绑定 */
        void detachView(); /** 解绑 */
    }

    interface BaseView {
        void showError(String msg); /** 请求出错 */
        void complete(); /** 请求完成 */
    }

}
