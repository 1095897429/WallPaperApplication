package com.ngbj.wallpaper.utils.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;

import com.socks.library.KLog;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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


    /** 从包名中获取渠道名 */
    public static String getChannelFromApk(Context context, String channelKey) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        return channel;
    }

}
