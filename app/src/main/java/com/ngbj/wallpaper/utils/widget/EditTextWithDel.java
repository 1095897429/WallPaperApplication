package com.ngbj.wallpaper.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.ngbj.wallpaper.R;
import com.ngbj.wallpaper.utils.common.AppHelper;
import com.ngbj.wallpaper.utils.common.StringUtils;

/**
 * 自定义EditText：自带删除按钮
 */
@SuppressLint("AppCompatCustomView")
public class EditTextWithDel extends EditText {
    private Drawable icDelete;
    private Context mContext;

    public EditTextWithDel(Context context) {
        this(context, null);
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        icDelete = ContextCompat.getDrawable(mContext, R.mipmap.edit_delete_icon);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setImage();
            }
        });
        setImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (icDelete != null && event.getAction() == MotionEvent.ACTION_UP) {
            //得到手指离开EditText时的X Y坐标
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            //创建一个长方形
            Rect rect = new Rect();
            //让长方形的宽等于edittext的宽，让长方形的高等于edittext的高
            getGlobalVisibleRect(rect);
            //把长方形缩短至右边30dp，约等于（padding+图标分辨率）
            rect.left = rect.right - AppHelper.dp2px(mContext, 30);
            //如果x和y坐标在长方形当中，说明你点击了右边的xx图片,清空输入框
            if (rect.contains(x, y)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private void setImage() {
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, icDelete, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }


}