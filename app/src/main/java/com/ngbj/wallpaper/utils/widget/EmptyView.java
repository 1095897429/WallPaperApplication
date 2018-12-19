package com.ngbj.wallpaper.utils.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ngbj.wallpaper.R;

/***
 * 加载中，加载失败，没有数据(此状态交给adapter完成)
 */
public class EmptyView extends LinearLayout  {

    private ProgressBar animProgress;
    private ImageView img;
    private TextView tv;

    public static final int LOADING = 1; // 加载中
    public static final int NODATA = 2; // 没有数据
    public static final int ERROR = 3; // 网络错误
    public static final int HIDE = 4; // 隐藏


    private int mState = LOADING;//初始化为加载状态

    private OnClickListener mOnClickListener;

    public void setOnLayoutClickListener(OnClickListener listener){
        mOnClickListener = listener;
    }

    public EmptyView(Context context) {
        this(context,null);
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.empty_layout, null);
        animProgress =  view.findViewById(R.id.animProgress);
        img =  view.findViewById(R.id.img_error_layout);
        tv =  view.findViewById(R.id.tv_error_layout);

        //添加视图
        addView(view);

        //初始化设置
        setType(LOADING);

        //图片点击事件
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener != null){
                    mOnClickListener.onClick(v);
                }
            }
        });
    }


    public void setType(int type){
        mState = type;
        switch (mState){
            case LOADING:
                animProgress.setVisibility(VISIBLE);
                img.setVisibility(GONE);
                tv.setText("正在加载...");
                setVisibility(VISIBLE);
                break;
            case ERROR:
                animProgress.setVisibility(GONE);
                img.setVisibility(VISIBLE);
                tv.setText("点击屏幕，重新加载");
                setVisibility(VISIBLE);
                break;
            case HIDE:
                setVisibility(GONE);
                break;
        }

    }

}
