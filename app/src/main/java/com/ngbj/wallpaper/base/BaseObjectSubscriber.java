package com.ngbj.wallpaper.base;


import com.google.gson.JsonParseException;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.socks.library.KLog;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.subscribers.ResourceSubscriber;
import retrofit2.HttpException;

/****
 * subscribeWith 返回的是方法参数类型，接着ResourceSubscriber 是 实现Disposable的，方便绑定Activity的生命周期
 * @param <T>
 */

public abstract class BaseObjectSubscriber<T>
        extends ResourceSubscriber<HttpResponse<T>>{

    private BaseContract.BaseView mView;

    /** 通过构造方法注入*/
    public BaseObjectSubscriber(BaseContract.BaseView mView){
        this.mView = mView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 判断网络
        KLog.d("显示对话框");
    }

    /** 连接成功，后台返回数据,接下来走onComplete方法*/
    @Override
    public void onNext(HttpResponse<T> response) {
        if(response.getCode() == 200){
            if(response.getData() != null){
                onSuccess(response.getData());
            }
        }else{
            onFailure(response.getCode(),response.getMessage());
        }
    }

    /** 服务器返回数据，但响应码不为200*/
    public void onFailure(int code, String message) {
        KLog.d("code : ", code + " " + message);
    }


    /** 连接超时 域名不正确等等 */
    @Override
    public void onError(Throwable t) {
        KLog.d("数据返回错误信息 结束对话框");
        KLog.d("t : " + t.getMessage().toString());
        if(t instanceof HttpException){ //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        }else if(t instanceof ConnectException
                    || t instanceof UnknownHostException){//  连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        }else if(t instanceof InterruptedException){//  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if(t instanceof JsonParseException
                    || t instanceof JSONException
                    || t instanceof ParseException){ //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        }else if(t instanceof SocketException){//  服务器响应超时
            onException(ExceptionReason.RESPONSE_TIMEOUT);
        }else{
            onException(ExceptionReason.UNKNOWN_ERROR);//未知错误
        }
    }

    @Override

    public void onComplete() {
        KLog.d("数据返回信息 结束对话框");
    }


    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                KLog.d("连接超时");
                break;

            case CONNECT_TIMEOUT:
                KLog.d("连接错误");
                break;

            case BAD_NETWORK:
                KLog.d("HTTP错误");
                break;

            case PARSE_ERROR:
                KLog.d("解析错误");
                break;

            case UNKNOWN_ERROR:
                KLog.d("未知错误");
            default:
                break;
        }
    }


    /** --------------------- 抽象方法 -------------------- */
    public abstract void onSuccess(T t);

    /** --------------------- 枚举 -------------------- */

    public enum ExceptionReason {
        PARSE_ERROR,   /** 解析数据失败 */
        BAD_NETWORK, /** 网络问题 */
        CONNECT_ERROR, /** 连接错误 */
        CONNECT_TIMEOUT, /** 连接超时 */
        RESPONSE_TIMEOUT, /** 响应超时 */
        UNKNOWN_ERROR, /** 未知错误 */
    }



}

