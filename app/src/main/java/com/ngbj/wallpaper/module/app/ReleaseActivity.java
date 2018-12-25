package com.ngbj.wallpaper.module.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaesLogicActivity;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.InterestBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.bean.entityBean.UploadTokenBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.BottomAlertDialog;
import com.ngbj.wallpaper.dialog.HeadAlertDialog;
import com.ngbj.wallpaper.eventbus.TagPositionEvent;
import com.ngbj.wallpaper.mvp.contract.app.ReleaseContract;
import com.ngbj.wallpaper.mvp.presenter.app.ReleasePresenter;
import com.ngbj.wallpaper.utils.common.PicPathHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.qiniu.Auth;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.socks.library.KLog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;



/***
 * 上传作品页
 */

public class ReleaseActivity extends BaseActivity<ReleasePresenter>
        implements ReleaseContract.View {

    private static final String TAG = "ReleaseActivity";

    @BindView(R.id.tagflowlayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.upload_text)
    EditText upload_text;


    List<UploadTagBean> uploadTagBeanList = new ArrayList<>();//后台返回的数据
    List<UploadTagBean> temps = new ArrayList<>();//临时数据
    List<UploadTagBean> tags = new ArrayList<>();
    List<String> categoryIdList = new ArrayList<>();//上传tagId部分

    String token;//上传的token


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ReleasePresenter();

    }

    private void initTags(List<UploadTagBean> tags) {
        tagFlowLayout.setAdapter(new TagAdapter<UploadTagBean>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, UploadTagBean uploadTagBean) {
                TextView tag_text = (TextView) LayoutInflater.from(ReleaseActivity.this).inflate(R.layout.hot_item,tagFlowLayout,false);
                tag_text.setText(uploadTagBean.getName());
                return tag_text;
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mPresenter.getUploadToken();
        mPresenter.getInterestData();
    }


    @Override
    public void showInterestData(List<InterestBean> interestBeanList) {
        UploadTagBean tagBean;
        for (int i = 0; i < interestBeanList.size(); i++) {
            tagBean = new UploadTagBean();
            tagBean.setName(interestBeanList.get(i).getName());
            tagBean.setTagId(interestBeanList.get(i).getId());
            uploadTagBeanList.add(tagBean);
        }
        temps.addAll(uploadTagBeanList);
    }


    @OnClick(R.id.add_tag)
    public void AddTag(){
       new BottomAlertDialog(this,temps)
                .builder()
                .show();
    }

    @OnClick(R.id.makedone)
    public void Makedone(){
        KLog.d("选中的标签是：" + (tags.isEmpty()?0:tags.size()));
        KLog.d("文本内容：" + upload_text.getText().toString().trim());
        uploadImg2QiNiu();
    }


    /**  ------------- 七牛云部分 开始 -------------*/

    @Override
    public void shwoUploadToken(UploadTokenBean uploadTokenBean) {
        KLog.d("token: " + uploadTokenBean.getToken());
        token = uploadTokenBean.getToken();
    }

    @Override
    public void showUploadWallpaper() {

    }


    private void uploadImg2QiNiu() {
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "icon_" + sdf.format(new Date());
        String picPath = getPicFile().toString();
        final String yuming = "pjb68wj3e.bkt.clouddn.com/";
        KLog.d(TAG, "picPath: " + picPath);
        uploadManager.put(picPath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    String headpicPath = "http://" + yuming + key;
//                    KLog.i(TAG, "图片的地址: " + headpicPath);
//                    String thumbPath = "http://" + yuming + key + "?imageView2/1/w/108/h/192";
//                    KLog.i(TAG, "缩略图的地址: " + thumbPath);

                    String accessToken = (String) SPHelper.get(ReleaseActivity.this,AppConstant.ACCESSTOKEN,"");
                    if(TextUtils.isEmpty(accessToken)){
                        accessToken = "7v72FRobjPBvOFD6udGGq2UgRNPANUrv";
                    }

                    Map<String,Object> map = new HashMap<>();

                    map.put("title","标题");
                    map.put("imgUrl",headpicPath);
                    map.put("movieUrl","null");
                    map.put("resolution","标题");
                    map.put("categoryId",categoryIdList);
                    map.put("type","1");//1静态壁纸 2动态壁纸
                    mPresenter.uploadWallpaper(accessToken,map);

                }
            }
        }, null);

    }

    //建立上传图片的路径及名称
    private File getPicFile() {
        return new File(path);
    }


    /**  ------------- 七牛云部分 结束 -------------*/



    @OnClick(R.id.back)
    public void Back(){
        finish();
    }


    /**  ------------- 图片选择部分 开始 -------------*/

    public static final int CHOOSE_PHOTO = 1;

    public static final int TAKE_PHOTO = 2;


    String path = "";//图片返回的路径

    Uri imageUri;//图片的uri

    @BindView(R.id.picture)
    ImageView picture;

    @BindView(R.id.upload_image)
    FrameLayout upload_image;


    @OnClick(R.id.picture_delete)
    public void PictureDelete(){
        upload_image.setVisibility(View.GONE);
        path = "";
        imageUri = null;
    }


    @OnClick(R.id.upload_file)
    public void UploadFile(){
        List<String> temps = new ArrayList<>();
        temps.add("拍照");
        temps.add("手机相册");
        temps.add("取消");

        HeadAlertDialog headAlertDialog =  new HeadAlertDialog(this)
                .builder()
                .setHeadBeanList(temps);
        headAlertDialog.setOnDialogItemClickListener(new HeadAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("拍照");
                        if (ContextCompat.checkSelfPermission(ReleaseActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(ReleaseActivity.this,new String[]{Manifest.permission.CAMERA},1);
                        }else {
                            openTakePhoto();
                        }
                        break;
                    case 1:
                        KLog.d("手机相册");
                        if (ContextCompat.checkSelfPermission(ReleaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(ReleaseActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                        }else {
                            openAlbum();
                        }
                        break;
                }
            }
        });
        headAlertDialog.show();
    }

    //此为打开系统相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }


    //权限请求的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openTakePhoto();
                }else {
                    ToastHelper.customToastView(this,"相机权限拒绝，请先开启权限");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    ToastHelper.customToastView(this,"存储卡权限拒绝，请先开启权限");
                }
                break;
            default:
                break;
        }
    }

    private void openTakePhoto() {
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(this,
                    "com.ngbj.wallpaper.provider",outputImage);//content://com.ngbj.wallpaper.provider/path/output_image.jpg
        }else {
            imageUri = Uri.fromFile(outputImage);//file:///storage/sdcard/Android/data/com.ngbj.wallpaper/cache/output_image.jpg
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        diaplayImage(bitmap);
                        path = PicPathHelper.getRealFilePath(this,imageUri);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    path = PicPathHelper.handleSelectedPhoto(this,data);
                    KLog.d("图片路径为：" + path);
                    if(null != path){
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        diaplayImage(bitmap);
                    }else{
                        KLog.d("图片路径获取失败");
                    }
                }
                break;
            default:
                break;
        }
    }



    private void diaplayImage(Bitmap bitmap){
        picture.setImageBitmap(bitmap);
        upload_image.setVisibility(View.VISIBLE);
    }

    /**  ------------- 图片选择部分 结束 -------------*/


    /** --------------  底部标签 EventBus 开始-----------------*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /** 记录选中的类型 */
    @Subscribe
    public void onUploadTagEvent(TagPositionEvent event){
        Map<Integer,UploadTagBean> hashMap = event.getMap();

        temps.clear();
        tags.clear();
        categoryIdList.clear();

        for (UploadTagBean value : hashMap.values()) {
            if(value.isSelect()){
                tags.add(value);
                categoryIdList.add(value.getTagId());
            }
            temps.add(value);//更新数据源
        }

        initTags(tags);//更新显示
    }


    /** --------------   底部标签 EventBus 结束 -----------------*/


}
