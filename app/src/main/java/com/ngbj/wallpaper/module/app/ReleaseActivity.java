package com.ngbj.wallpaper.module.app;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.dialog.BottomAlertDialog;
import com.ngbj.wallpaper.dialog.HeadAlertDialog;
import com.ngbj.wallpaper.eventbus.TagPositionEvent;
import com.ngbj.wallpaper.utils.common.PicPathHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.socks.library.KLog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/***
 * 上传作品页
 */

public class ReleaseActivity extends BaseActivity{

    @BindView(R.id.tagflowlayout)
    TagFlowLayout tagFlowLayout;

    @BindView(R.id.upload_text)
    EditText upload_text;

    List<UploadTagBean> uploadTagBeanList = new ArrayList<>();//后台返回的数据
    List<UploadTagBean> temps = new ArrayList<>();//临时数据
    List<UploadTagBean> tags = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_release;
    }

    @Override
    protected void initPresenter() {
        UploadTagBean tagBean;
        for (int i = 0; i < 15; i++) {
            tagBean = new UploadTagBean("#风景建筑");
            uploadTagBeanList.add(tagBean);
        }
        temps.addAll(uploadTagBeanList);
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
    }


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
                    ToastHelper.customToastView(this,"存储卡权限拒绝，请先开启权限");
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
                    "com.ngbj.wallpaper.provider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        //将拍摄的照片显示出来 content://com.ngbj.wallpaper.provider/path/output_image.jpg
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        upload_image.setVisibility(View.VISIBLE);
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
                        picture.setImageBitmap(bitmap);
                        upload_image.setVisibility(View.VISIBLE);
                    }else{
                        KLog.d("图片路径获取失败");
                    }

                }
                break;
            default:
                break;
        }
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


    @Subscribe
    public void onUploadTagEvent(TagPositionEvent event){
        Map<Integer,UploadTagBean> hashMap = event.getMap();

        temps.clear();
        tags.clear();

        for (UploadTagBean value : hashMap.values()) {
            if(value.isSelect()){
                tags.add(value);
            }
            temps.add(value);//更新数据源
        }

        initTags(tags);//更新显示
    }
    /** --------------   底部标签 EventBus 结束 -----------------*/


}
