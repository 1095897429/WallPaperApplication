package com.ngbj.wallpaper;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;
import android.widget.Toast;

import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.qiniu.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UserInfoActivity extends BaseActivity {
    private static String AccessKey = "xtQbV26TBeJlFUR2skOQPywctgxlIcm8Xd_ZQK47";//此处填你自己的AccessKey
    private static String SecretKey = "4Y5wq6L5PfkmdTWydcZzgLj70mG_bBN5j9kDuVPq";//此处填你自己的SecretKey
    private static final String TAG = "UserInfoActivity";

    @BindView(R.id.imageView)
    ImageView imageView;
    private Uri imageUri;
    private Uri localUri = null;
    private static final int REQUEST_CAPTURE = 2;//相机
    private static final int RESULT_CROP = 7;//裁剪
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
                    if (null != imageUri) {
                        localUri = imageUri;
                        performCrop(localUri);
                    }
                    break;
                case RESULT_CROP:
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    //判断返回值extras是否为空，为空则说明用户截图没有保存就返回了，此时应该用上一张图，
                    //否则就用用户保存的图
                    if (extras == null) {
                    } else {
                        imageView.setImageBitmap(selectedBitmap);
                        storeImage(selectedBitmap);
                    }
                    break;
                case GALLERY_ACTIVITY_CODE:
                    localUri = data.getData();
                    performCrop(localUri);
                    break;
            }
        }
    }



    //建立保存头像的路径及名称
    private File getOutputMediaFile() {
        String cachePath =  SDCardHelper.getSDCardPrivateCacheDir(this);
        File mediaFile;
        String mImageName = "avatar.png";
        mediaFile = new File(cachePath + File.separator + mImageName);
        return mediaFile;
    }



    @OnClick(R.id.carame)
    public void carame() {
        openCamera();
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

    //保存图像
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            KLog.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            KLog.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            KLog.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }


    //裁剪图片
    private void performCrop(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                grantUriPermission("com.android.camera", uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile().toString());
            startActivityForResult(intent, RESULT_CROP);
        } catch (ActivityNotFoundException anfe) {
            String errorMessage = "你的设备不支持裁剪行为！";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void openCamera() {  //调用相机拍照
        Intent intent = new Intent();
        File file = getOutputMediaFile(); //工具类稍后会给出
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);//通过FileProvider创建一个content类型的Uri，进行封装
        } else { //7.0以下，如果直接拿到相机返回的intent值，拿到的则是拍照的原图大小，很容易发生OOM，所以我们同样将返回的地址，保存到指定路径，返回到Activity时，去指定路径获取，压缩图片
            imageUri = Uri.fromFile(file);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);//启动拍照
    }

    //从固定的文件中上传头像
    private void uploadImg2QiNiu() {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        String picPath = getOutputMediaFile().toString();
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
                    String thumbPath = "http://phcsxfrh8.bkt.clouddn.com/" + key + "?imageView2/1/w/200/h/200";
                    KLog.i(TAG, "缩略图的地址: " + thumbPath);

                }
            }
        }, null);

    }

}
