package com.ngbj.wallpaper;


import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.utils.common.PicPathHelper;
import com.ngbj.wallpaper.utils.qiniu.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UploadPicActivity extends BaseActivity {
    private static String AccessKey = "xtQbV26TBeJlFUR2skOQPywctgxlIcm8Xd_ZQK47";//此处填你自己的AccessKey
    private static String SecretKey = "4Y5wq6L5PfkmdTWydcZzgLj70mG_bBN5j9kDuVPq";//此处填你自己的SecretKey
    private static final String TAG = "UploadPicActivity";

    @BindView(R.id.imageView)
    ImageView imageView;
    String imagePath = "";
    private static final int REQUEST_CAPTURE = 2;//相机
    private static final int GALLERY_ACTIVITY_CODE = 9;//图库


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        methodRequiresTwoPermission();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }


    @AfterPermissionGranted(1)//添加注解，是为了首次执行权限申请后，回调该方法
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //已经申请过权限，直接调用相机
        } else {
            EasyPermissions.requestPermissions(this, "需要获取权限",
                    1, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAPTURE:
                    break;
                case GALLERY_ACTIVITY_CODE:
                    imagePath = PicPathHelper.handleSelectedPhoto(this,data);
                    diaplayImage(imagePath);
                    break;
            }
        }
    }

    private void diaplayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get iamge",Toast.LENGTH_SHORT).show();
        }
    }



    //建立保存头像的路径及名称
    private File getPicFile() {
        return new File(imagePath);
    }


    @OnClick(R.id.home_pic)
    public void home_pic() {
        setLockScreenWallpaper();
    }

    //设置锁屏壁纸
    private void setLockScreenWallpaper() {
         WallpaperManager mWallManager = WallpaperManager.getInstance(this);
         try {
             Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
             mWallManager.setBitmap(bitmap);
             Toast.makeText(UploadPicActivity.this, "壁纸设置成功", Toast.LENGTH_SHORT) .show();
         } catch (IOException e) {
             e.printStackTrace();
         }

    }


    @OnClick(R.id.carame)
    public void carame() {

    }


    @OnClick(R.id.select_img)
    public void select_img() {
        Intent gallery_Intent = new Intent(Intent.ACTION_PICK, null);
        gallery_Intent.setType("image/*");
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    @OnClick(R.id.upload_img)
    public void upload_img() {
        uploadImg2QiNiu();
    }


    private void uploadImg2QiNiu() {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        String picPath = getPicFile().toString();
        KLog.d(TAG, "picPath: " + picPath);
        //Auth.create(AccessKey, SecretKey).uploadToken("zhongshan-avatar")，这句就是生成token
        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken("wallpaper"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    KLog.i(TAG, "token ===" + Auth.create(AccessKey, SecretKey).uploadToken("wallpaper"));
                    String headpicPath = "http://phcsxfrh8.bkt.clouddn.com/" + key;
                    KLog.i(TAG, "图片的地址: " + headpicPath);
                    String thumbPath = "http://phcsxfrh8.bkt.clouddn.com/" + key + "?imageView2/1/w/108/h/192";
                    KLog.i(TAG, "缩略图的地址: " + thumbPath);

                }
            }
        }, null);

    }

}
