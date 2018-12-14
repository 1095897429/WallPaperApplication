package com.ngbj.wallpaper.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Gallery;

import com.ngbj.wallpaper.utils.common.ScreenHepler;

public class VerticalGallery extends Gallery {

    Context mContext;
    int height;

    public VerticalGallery(Context context) {
        super(context);
        this.mContext = context;
        height = ScreenHepler.getScreenHeight((Activity) context);
    }

    public VerticalGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        height = ScreenHepler.getScreenHeight((Activity) context);
    }



    public  void onDraw(Canvas canvas) {
        canvas.translate(00, height);
        canvas.rotate(-90f);
        super.onDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                return super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, event);
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, event);
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
