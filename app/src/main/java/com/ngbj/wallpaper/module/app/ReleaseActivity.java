package com.ngbj.wallpaper.module.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.BaseActivity;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.dialog.BottomAlertDialog2;
import com.ngbj.wallpaper.eventbus.TagPositionEvent;
import com.socks.library.KLog;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


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
            tagBean = new UploadTagBean("#风景建筑#" + i);
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
       new BottomAlertDialog2(this,temps)
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

    @OnClick(R.id.upload_file)
    public void UploadFile(){

    }





    /** --------------  EventBus -----------------*/
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
    /** --------------  EventBus -----------------*/


}
