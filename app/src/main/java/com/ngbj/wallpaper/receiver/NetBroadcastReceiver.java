package com.ngbj.wallpaper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/***
 * 检测网络变化广播类
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    private NetChangeListener mNetChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        //A change in network connectivity has occurred.
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netWorkState = getNetWorkState(context);
            if(null != mNetChangeListener){
                mNetChangeListener.onChangeListener(netWorkState);
            }
        }
    }

    public void setNetChangeListener(NetChangeListener netChangeListener) {
        mNetChangeListener = netChangeListener;
    }

    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener(int status);
    }


    /** 没有网络 */
    private static final int NETWORK_NONE = -1;
    /** 移动网络 */
    private static final int NETWORK_MOBILE = 0;
    /** 无线网络 */
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        //得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //如果网络连接，判断该网络类型
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;//wifi
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;//mobile
            }
        } else { //网络异常
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

}
