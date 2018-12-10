package com.ngbj.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.adapter.detail.ReportAdapter;
import com.ngbj.wallpaper.utils.widget.CustomDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2018/8/9
 * 举报 弹出框
 */
public class ReportAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private RecyclerView recyclerView;
    private ReportAdapter mReportAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<String> mStrings = new ArrayList<>();

    /** 回调接口 开始 */
    public OnDialogItemClickListener mOnDialogItemClickListener;

    public interface OnDialogItemClickListener{
        void func(int position);
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener) {
        mOnDialogItemClickListener = onDialogItemClickListener;
    }

    /** 回调接口 结束 */



    public ReportAlertDialog(Context context){
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public ReportAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_report_dialog,null); // 获取Dialog布局
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
        mReportAdapter = new ReportAdapter(mStrings);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //设置Adapter
        recyclerView.setAdapter(mReportAdapter);
        //下划线
        recyclerView.addItemDecoration(new CustomDecoration(mContext,CustomDecoration.VERTICAL_LIST,R.drawable.divider,0));
    }

    public ReportAlertDialog setReportBeanList(List<String> list){
        this.mStrings = list;
        initRecycleView();
        return this;
    }


    public ReportAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }



    public ReportAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }


    private void setLayuot(){
        setData();
        setEvent();
    }

    private void setEvent() {
        mReportAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(null != mOnDialogItemClickListener){
                    mOnDialogItemClickListener.func(position);
                    dialog.dismiss();
                }
            }
        });
    }

    private void setData(){

    }

    public void show(){
        setLayuot();
        dialog.show();
    }



}
