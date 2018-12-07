package com.ngbj.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.ReportAdapter;
import com.ngbj.wallpaper.adapter.detail.ShareAdapter;
import com.ngbj.wallpaper.bean.entityBean.ShareBean;
import com.ngbj.wallpaper.utils.widget.CustomDecoration;

import java.util.ArrayList;
import java.util.List;




public class ShareAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private RecyclerView recyclerView;
    private ShareAdapter mShareAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<ShareBean> mShareBeanList = new ArrayList<>();



    public ShareAlertDialog(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public ShareAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_share_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        recyclerView = view.findViewById(R.id.recyclerView);
        dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(view);
        //默认设置
        dialog.setCanceledOnTouchOutside(false);
        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 1.0f);
        lp.gravity = Gravity.BOTTOM;
//        lp.y = (int) (display.getWidth() * 0.0f);//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);

        return this;
    }

    private void initRecycleView() {
        mShareAdapter = new ShareAdapter(mShareBeanList);
        mGridLayoutManager = new GridLayoutManager(mContext,5);
        //设置布局管理器
        recyclerView.setLayoutManager(mGridLayoutManager);
        //设置Adapter
        recyclerView.setAdapter(mShareAdapter);
    }

    public ShareAlertDialog seShareBeanList(List<ShareBean> list){
        this.mShareBeanList = list;
        initRecycleView();
        return this;
    }


    public ShareAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }



    public ShareAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }


    private void setLayuot(){
        setData();
        setEvent();
    }

    private void setEvent() {

    }

    private void setData(){

    }

    public void show(){
        setLayuot();
        dialog.show();
    }



}
