package com.ngbj.wallpaper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
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
import com.ngbj.wallpaper.bean.entityBean.UploadTagBean;
import com.ngbj.wallpaper.eventbus.TagPositionEvent;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;


/**
 * hashmap<integer,UploadTagBean></>
 */
public class BottomAlertDialog2 {
    private Context mContext;
    private Dialog dialog;
    private Display display;
    private TextView done;
    private RecyclerView tag_recyclerView;
    private UploadTagAdapter uploadTagAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<UploadTagBean> uploadTagBeanList ;




    public BottomAlertDialog2(Context context, List<UploadTagBean> list){
        this.uploadTagBeanList = list;
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }


    public BottomAlertDialog2 builder(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_tag_dialog,null); // 获取Dialog布局
        // 获取自定义Dialog布局中的控件
        tag_recyclerView = view.findViewById(R.id.tag_recyclerView);
        done = view.findViewById(R.id.done);
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
        uploadTagAdapter = new UploadTagAdapter(uploadTagBeanList);
        gridLayoutManager = new GridLayoutManager(mContext,4);
        tag_recyclerView.setLayoutManager(gridLayoutManager);
        tag_recyclerView.setAdapter(uploadTagAdapter);
    }



    public BottomAlertDialog2 setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }



    public BottomAlertDialog2 setCanceledOnTouchOutside(boolean b) {
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
                EventBus.getDefault().post(new TagPositionEvent(map));
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


    static HashMap<Integer,UploadTagBean> map = new HashMap<>();
    // ① 创建Adapter
    public static class UploadTagAdapter extends RecyclerView.Adapter<UploadTagAdapter.VH> {

            private List<UploadTagBean> mDatas;


            public UploadTagAdapter(List<UploadTagBean> mDatas){
                this.mDatas = mDatas;
                initData();
            }

            private void initData() {
                for (int i = 0; i < mDatas.size(); i++) {
                    map.put(i,mDatas.get(i));
                }
            }

            //② 创建ViewHolder
            public static class VH extends RecyclerView.ViewHolder{
                public final TextView title;
                public VH(View v) {
                    super(v);
                    title =  v.findViewById(R.id.upload_tag_name);
                }
            }

            //③ 在Adapter中实现3个方法
            @Override
            public void onBindViewHolder(final UploadTagAdapter.VH holder, final int position) {
                holder.title.setText(mDatas.get(position).getName());

                if (map.get(position).isSelect()) {
                    holder.title.setTextColor(Color.parseColor("#4558E6"));
                }else {
                    holder.title.setTextColor(Color.WHITE);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //item 点击事件
                        UploadTagBean uploadTagBean = map.get(position);

                        if (!uploadTagBean.isSelect()) {
                            uploadTagBean.setSelect(true);
                            holder.title.setTextColor(Color.parseColor("#4558E6"));
                        }else {
                            uploadTagBean.setSelect(false);
                            holder.title.setTextColor(Color.WHITE);
                        }
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_tag_item, parent, false);
                return new UploadTagAdapter.VH(v);
            }

        }

    /** --------------  适配器 -----------------*/

}
