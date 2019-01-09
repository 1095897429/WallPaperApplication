package com.ngbj.wallpaper.utils.downfile;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/***
 * 下载apk：
 */

public class DownManager {

	private Context context;
	private String apkUrl;
	private int progress;
	private String destinationUri;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	int contentLength;
	private DownListener mDownListener;


	public void setDownListener(DownListener downListener) {
		mDownListener = downListener;
	}

	public interface DownListener{
		void fun();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
//				KLog.d("进度： " + progress);
				break;
			case DOWN_OVER:
//				Toast.makeText(context, "下载完毕", Toast.LENGTH_SHORT).show();
				File dir = new File(SDCardHelper.getSDCardBaseDir(), "BSLWallpaper");
				ToastHelper.customToastViewLong(context,"图片保存到" + dir.getAbsolutePath() + "目录下");
				if(null != mDownListener)
					mDownListener.fun();
				break;
			default:
				break;
			}
		};
	};

	public DownManager(Context context, String apkUrl,String destinationUri) {
		this.context = context;
		this.apkUrl = apkUrl;
		this.destinationUri = destinationUri;
	}



	public void downloadApk() {
		Thread downLoadThread = new Thread(mdownApk1);
		downLoadThread.start();
	}

	private Runnable mdownApk1 = new Runnable() {
		@Override
		public void run() {
			 URL url;
			try {
				url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				//设置超时间为3秒
				conn.setConnectTimeout(3 * 1000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				//打开连接
				conn.connect();
				//获取内容长度
				contentLength = conn.getContentLength();
				//得到输入流
				InputStream inputStream = conn.getInputStream();
				//获取自己数组
				byte[] getData = readInputStream(inputStream);

				File file = new File(destinationUri);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(getData);
				if (fos != null) {
					fos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mHandler.sendEmptyMessage(DOWN_OVER);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	//判断文件是否存在
	public boolean fileIsExists(String strFile) {
		try {
			File f=new File(strFile);
			if(!f.exists())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}


	/**
	 * 从输入流中获取字节数组
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public  byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len ;
		int count = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
			count += len;
			progress = (int) (((float) count / contentLength) * 100);
			// 更新进展
			mHandler.sendEmptyMessage(DOWN_UPDATE);

		}
		bos.close();
		return bos.toByteArray();
	}





}
