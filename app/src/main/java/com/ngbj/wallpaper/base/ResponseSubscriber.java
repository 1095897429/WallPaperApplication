package com.ngbj.wallpaper.base;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ngbj.wallpaper.bean.entityBean.AllData;
import com.ngbj.wallpaper.bean.entityBean.HttpResponse;
import com.ngbj.wallpaper.utils.encry.AesUtils;
import com.socks.library.KLog;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

//T 指的是 ResponseBody  R 指的是 具体泛型
public abstract class ResponseSubscriber<T extends ResponseBody>
        extends ResourceSubscriber<T> {


    private BaseContract.BaseView mView;


    /** 通过构造方法注入*/
    public ResponseSubscriber(BaseContract.BaseView mView){
        this.mView = mView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        KLog.d("显示对话框"); // 判断网络
    }

    @Override
    public void onNext(T response) {

        String result;
        Gson gson = new Gson();
        try {
            result = response.string();

            AllData allData = gson.fromJson(result,AllData.class);

            result = allData.getData();

//            KLog.d("返回的原始数据：" + result);

            //TODO 测试
            String dectry =  AesUtils.decrypt(result,AesUtils.SECRETKEY,AesUtils.IV);
//            KLog.d("解密出的数据 =  " +  dectry);

            HttpResponse httpResponse = gson.fromJson(dectry,HttpResponse.class);

            mView.complete();
            onSuccess(httpResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public abstract void onSuccess(HttpResponse response);


    @Override

    public void onComplete() {
        KLog.d("数据返回信息 正常结束对话框");
    }


    /** 连接超时 域名不正确等等 */
    @Override
    public void onError(Throwable t) {
        KLog.d("错误信息 结束对话框");
        KLog.d("t : " + t.getMessage());
        mView.showError(t.getMessage());
        Toast.makeText(MyApplication.getInstance(), "网络异常", Toast.LENGTH_SHORT).show();
        if(t instanceof HttpException){ //   HTTP错误
            onException(BaseObjectSubscriber.ExceptionReason.BAD_NETWORK);
        }else if(t instanceof ConnectException
                || t instanceof UnknownHostException){//  连接错误
            onException(BaseObjectSubscriber.ExceptionReason.CONNECT_ERROR);
        }else if(t instanceof InterruptedException){//  连接超时
            onException(BaseObjectSubscriber.ExceptionReason.CONNECT_TIMEOUT);
        } else if(t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException){ //  解析错误
            onException(BaseObjectSubscriber.ExceptionReason.PARSE_ERROR);
        }else if(t instanceof SocketException){//  服务器响应超时
            onException(BaseObjectSubscriber.ExceptionReason.RESPONSE_TIMEOUT);
        }else{
            onException(BaseObjectSubscriber.ExceptionReason.UNKNOWN_ERROR);//未知错误
        }
    }

    public void onException(BaseObjectSubscriber.ExceptionReason reason) {
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