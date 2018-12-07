package com.ngbj.wallpaper.utils.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.socks.library.KLog;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {


    /** 获取友盟集成测试设备的设备号和mac */
    public static String[] getTestDeviceInfo(Context context){
        String[] deviceInfo = new String[2];
        try {
            if(context != null){
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e){}
        KLog.d("device_id  " + deviceInfo[0] + "  mac " +  deviceInfo[1]);
        return deviceInfo;
    }


    /** 获取当前日期 和 点击时的日期*/
    public static String[] getDate(Context context){
        String[] dateInfo = new String[2];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());  //获取当前时间毫秒数
        String currentYear_Month_Day = simpleDateFormat.format(date);

        String str2 = (String) SPHelper.get(context,"last_today_time","");

        dateInfo[0] = currentYear_Month_Day;
        dateInfo[1] = str2;
        KLog.d("当前日期  " + dateInfo[0] + "  之前记录的最后日期 " +  dateInfo[1]);
        return  dateInfo;
    }


    /** 尺寸转换 */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

}
