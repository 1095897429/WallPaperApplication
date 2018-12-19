package com.ngbj.wallpaper.module.fragment;



import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.my.MyFragmentAdapter;
import com.ngbj.wallpaper.base.BaseFragment;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.dialog.HeadAlertDialog;
import com.ngbj.wallpaper.dialog.ReportAlertDialog;
import com.ngbj.wallpaper.dialog.ShareAlertDialog;
import com.ngbj.wallpaper.module.app.ReleaseActivity;
import com.ngbj.wallpaper.module.app.SettingActivity;
import com.ngbj.wallpaper.mvp.contract.fragment.MyContract;
import com.ngbj.wallpaper.mvp.presenter.fragment.IndexPresenter;
import com.ngbj.wallpaper.mvp.presenter.fragment.MyPresenter;
import com.ngbj.wallpaper.utils.common.Base64;
import com.ngbj.wallpaper.utils.common.PicPathHelper;
import com.ngbj.wallpaper.utils.common.SDCardHelper;
import com.ngbj.wallpaper.utils.common.SPHelper;
import com.ngbj.wallpaper.utils.common.ToastHelper;
import com.ngbj.wallpaper.utils.widget.GlideCircleTransform;
import com.socks.library.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;


public class MyFragment extends BaseFragment<MyPresenter>
        implements MyContract.View {

    @BindView(R.id.tablayout)
    SlidingTabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.iv_blur)
    ImageView iv_blur;

    @BindView(R.id.default_head)
    ImageView default_head;


    String headUrl = "";

    MyFragmentAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    List<String> list_Title = new ArrayList<>();//标题

    public static MyFragment getInstance(){
        return new MyFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initPresenter() {
       mPresenter = new MyPresenter();
    }

    @Override
    protected void initData() {
        setBlur();
        getData();
        initIndicator();
    }

    @SuppressLint("NewApi")
    private void setBlur() {
        if(!TextUtils.isEmpty(headUrl)){
            Glide.with(this)
                    .load(headUrl)
                    .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                    .into(iv_blur);
        }else
          Glide.with(this)
                .load(R.mipmap.default_head)
                .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                .into(iv_blur);
    }

    /** 填充vp的数据源 */
    @SuppressLint("NewApi")
    private void getData() {
        fragments.add(UploadHistoryFragment.getInstance());
        fragments.add(CreateFragment.getInstance("2"));
        fragments.add(CreateFragment.getInstance("3"));
        fragments.add(CreateFragment.getInstance("1"));
        list_Title.add("创作");
        list_Title.add("收藏");
        list_Title.add("分享");
        list_Title.add("下载");
        pagerAdapter = new MyFragmentAdapter(getChildFragmentManager(),fragments,list_Title);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                KLog.d("position :" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator() {
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }



    @OnClick(R.id.settting)
    public void Setting(){
       startActivity(new Intent(getActivity(),SettingActivity.class));
    }


    @OnClick(R.id.upload_fl)
    public void UploadFl(){
        startActivity(new Intent(getActivity(),ReleaseActivity.class));
    }


    /**  ------------- 图片选择部分 开始 -------------*/

    public static final int CHOOSE_PHOTO = 1;

    public static final int TAKE_PHOTO = 2;

    public static final int EDIT_PHOTO = 3;


    String path = "";//图片返回的路径

    Uri imageUri;//图片的uri


    @OnClick(R.id.default_head)
    public void DefaultHead(){

        List<String> temps = new ArrayList<>();
        temps.add("拍照");
        temps.add("手机相册");
        temps.add("取消");

        HeadAlertDialog headAlertDialog =  new HeadAlertDialog(mContext)
                .builder()
                .setHeadBeanList(temps);
        headAlertDialog.setOnDialogItemClickListener(new HeadAlertDialog.OnDialogItemClickListener() {
            @Override
            public void func(int position) {
                switch (position){
                    case 0:
                        KLog.d("拍照");
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
                        }else {
                            openTakePhoto();
                        }
                        break;
                    case 1:
                        KLog.d("手机相册");
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                        }else {
                            openAlbum();
                        }
                        break;
                }
            }
        });
        headAlertDialog.show();

    }




    /** 裁剪保存的图片文件 */
    public File getOutputEditImageFile() {
        return  new File(SDCardHelper.getSDCardPrivateCacheDir(getActivity()),"headedit.png");
    }

    /** 拍照的图片文件 */
    public File getOutputHeadImageFile() {
        return  new File(SDCardHelper.getSDCardPrivateCacheDir(getActivity()),"output_image.png");
    }




    private void openTakePhoto() {
        File outputImage = getOutputHeadImageFile();

        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(getActivity(),
                    "com.ngbj.wallpaper.provider",outputImage);//content://com.ngbj.wallpaper.provider/path/output_image.jpg
        }else {
            imageUri = Uri.fromFile(outputImage);//file:///storage/sdcard/Android/data/com.ngbj.wallpaper/cache/output_image.jpg
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }


    //此为打开系统相册
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
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
                    ToastHelper.customToastView(getActivity(),"相机权限拒绝，请先开启权限");
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    ToastHelper.customToastView(getActivity(),"存储卡权限拒绝，请先开启权限");
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    editPicture(imageUri);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    if(data != null){}
                    //获取图片的uri
                    Uri uri = data.getData();
                    editPicture(uri);
                }
                break;
            case EDIT_PHOTO:
                uploadHeadImg();
                break;
            default:
                break;
        }
    }


    //TODO 头像 + 拍照 -- 裁剪到一个File中，上传时，获取裁剪File即可
    public void editPicture(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);//输出X方向的像素
        intent.putExtra("outputY", 200);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getOutputEditImageFile()));
        intent.putExtra("return-data", true);//设置为不返回数据
        startActivityForResult(intent, EDIT_PHOTO);
    }


    private void uploadHeadImg()  {
        String accessToken = (String) SPHelper.get(mContext,AppConstant.ACCESSTOKEN,"");
        if(TextUtils.isEmpty(accessToken)){
            accessToken = "7v72FRobjPBvOFD6udGGq2UgRNPANUrv";
        }
        File editFile = getOutputEditImageFile();
        KLog.d("path : " + editFile.getAbsolutePath());


        String name = SDCardHelper.getSDCardPrivateCacheDir(getActivity()) + "/" + "headedit.png";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        KLog.d("name: " + name);

        if(null == bitmap){
            return;
        }
        String base64Img = "data:image/png;base64," + Bitmap2StrByBase64(bitmap);
        KLog.d("base64Img： " + base64Img);
        mPresenter.getUploadHeadData(accessToken,base64Img);

    }



    public String Bitmap2StrByBase64(Bitmap bit) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, bos);//参数100表示不压缩
            bos.flush();
            bos.close();
            byte[] bytes = bos.toByteArray();
            Base64 base64 = new Base64();
            String dataSS = base64.encodeToString(bytes);
            return dataSS;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**  ------------- 图片选择部分 结束 -------------*/


    @Override
    public void showUploadHistory(List<AdBean> list) {
        KLog.d("这里啥都没做~");
    }


    @Override
    public void showUploadHeadData(LoginBean loginBean) {
        Glide.with(mContext)
                .load(loginBean.getHead_img())
                .placeholder(R.mipmap.default_head)
                .transform(new GlideCircleTransform(mContext))
                .into(default_head);

        Glide.with(this)
                .load(loginBean.getHead_img())
                .bitmapTransform(new BlurTransformation(getActivity(), 25), new CenterCrop(getActivity()))
                .into(iv_blur);
    }

    @Override
    public void showRecord(List<AdBean> list) {

    }


}
