package com.ngbj.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.base.MyApplication;
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.eventbus.TagListPositionEvent;
import com.ngbj.wallpaper.utils.common.SPHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * hashmap<integer,UploadTagBean></>
 */
public class BottomListAlertDialog {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private TextView done;
    private TextView cancel;
    private RecyclerView tag_recyclerView;
    private UploadTagAdapter uploadTagAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<UploadTagBean> uploadTagBeanList ;

    private int myPs;


    public BottomListAlertDialog(Context context, List<UploadTagBean> list,int position){
        this.myPs = position;
        this.uploadTagBeanList = list;
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public BottomListAlertDialog builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_list_tag_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        tag_recyclerView = view.findViewById(R.id.tag_recyclerView);
        done = view.findViewById(R.id.done);
        cancel = view.findViewById(R.id.cancel);
        dialog = new Dialog(mContext, R.style.MyDialog);
        dialog.setContentView(view);
        //默认设置
        dialog.setCanceledOnTouchOutside(false);
        // 调整dialog背景大小
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 1.0f);
        lp.gravity = Gravity.BOTTOM;
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        initRecycleView();
        return this;
    }

    private void initRecycleView() {
        uploadTagAdapter = new UploadTagAdapter(uploadTagBeanList,myPs);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tag_recyclerView.setLayoutManager(mLinearLayoutManager);
        tag_recyclerView.setAdapter(uploadTagAdapter);
    }



    public BottomListAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }



    public BottomListAlertDialog setCanceledOnTouchOutside(boolean b) {
        dialog.setCanceledOnTouchOutside(b);
        return this;
    }


    private void setLayuot(){
        setData();
        setEvent();
    }

    private void setEvent() {

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void setData(){

    }



    public void show(){
        setLayuot();
        dialog.show();
    }


    public void dismiss(){
        dialog.dismiss();
    }



    /** --------------  适配器 -----------------*/



    // ① 创建Adapter
    public static class UploadTagAdapter extends RecyclerView.Adapter<UploadTagAdapter.VH> {

            private List<UploadTagBean> mDatas;

            private int mposition ;

            public UploadTagAdapter(List<UploadTagBean> mDatas,int myPs){
                this.mDatas = mDatas;
                mposition = myPs;
            }


            //② 创建ViewHolder
            public static class VH extends RecyclerView.ViewHolder{
                public final TextView title;
                public VH(View v) {
                    super(v);
                    title =  v.findViewById(R.id.upload_list_tag_name);
                }
            }

            //③ 在Adapter中实现3个方法
            @Override
            public void onBindViewHolder(final UploadTagAdapter.VH holder, final int position) {
                holder.title.setText(mDatas.get(position).getName());

                if(position == mposition){
                    holder.title.setTextColor(Color.parseColor("#4558E6"));
                }else
                    holder.title.setTextColor(Color.WHITE);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mposition = holder.getAdapterPosition();
                        SPHelper.put(MyApplication.getInstance(),"clearPosition",mposition);
                        notifyDataSetChanged();
                    }
                });

            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }

            @Override
            public UploadTagAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
                //LayoutInflater.from指定写法
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_list_tag_item, parent, false);
                return new UploadTagAdapter.VH(v);
            }

        }

    /** --------------  适配器 -----------------*/

}
