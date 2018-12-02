package com.ngbj.wallpaper.utils.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/***
 * SD卡的帮助类
 *  存储一般是
 *      外部私有：storage/sdcard/Android/data/包名/files
 *      外部公有：storage/sdcard/DICM
 *      内部私有：data/data/包名/files
 */
public class SDCardHelper {

    //判断SD卡是否被挂载
    public static boolean isSDCardMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取SD卡的根目录 一般为：storage/sdcard/0
    public static String getSDCardBaseDir(){
        if(isSDCardMounted()){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    //获取SD卡公有目录的路径 type:DICM
    public static String getSDCardPublicDir(String type){
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    // 获取SD卡私有Cache目录的路径 一般为 storage/sdcard/0/Android/data/包名/cache
    public static String getSDCardPrivateCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    /***
     *  保存bitmap图片到SDCard的私有Cache目录
     * @param bitmap
     * @param fileName 比如:Zl.png
     * @param context
     * @return
     */
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap, String fileName, Context context){
        BufferedOutputStream bos = null;
        if(isSDCardMounted()){
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                if(fileName != null && ( fileName.contains(".png") || fileName.contains(".PNG"))){
                    bitmap.compress(Bitmap.CompressFormat.PNG,80,bos);//保留图片80%的质量
                }else if(fileName != null && ( fileName.contains(".jpg"))){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
                }
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /***
     * 往SD卡的私有Cache目录下保存文件
     * @param data 字节数组
     * @param fileName
     * @param context
     * @return
     */
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    //从SD卡中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath){
        byte[] data = loadFileFromSDCard(filePath);
        if(data != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            if(bitmap != null)
                return bitmap;
        }
        return null;
    }

    /***
     * 从SD卡获取文件
     * @param fileDir 比如：storage/sdcard/0/DICM/zl.png
     * @return
     */
    public static byte[] loadFileFromSDCard(String fileDir){
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(isSDCardMounted()){
            try {
                bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
                int c ;
                byte [] buffer = new byte[1024 * 8];
                while ((c = bis.read(buffer)) != -1){
                    bos.write(buffer, 0,c);//将buffer数据写到流中
                    bos.flush();
                }
                return bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

                try {
                    bos.close();
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    /***
     * 从sdcard中删除文件
     * @param filePath 比如：storage/sdcard/0/DICM/zl.png
     * @return
     */
    public static boolean removeFileFromSDCard(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
            return true;
        }else{
            return false;
        }
    }




}
