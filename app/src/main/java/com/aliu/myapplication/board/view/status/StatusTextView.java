package com.aliu.myapplication.board.view.status;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aliu.myapplication.R;


/**
 * describe
 * Created by liudong on 2017/12/06.
 */

public class StatusTextView extends AppCompatTextView {

    private float   touchAlpha;
    private float   disabledAlpha;
    private boolean isUseEnable;
    private boolean isEnable = true;

    public StatusTextView(Context context) {
        super(context);
        init(context, null);
    }

    public StatusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StatusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        touchAlpha = attrArray.getFloat(R.styleable.StatusView_TouchAlpha, 0.4f);
        disabledAlpha = attrArray.getFloat(R.styleable.StatusView_DisabledAlpha, 0.2f);
        isUseEnable = attrArray.getBoolean(R.styleable.StatusView_UseEnable, true);
        attrArray.recycle();
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        isEnable = b;
        if (isUseEnable) {
            if (isEnable) {
                setAlpha(1.0f);
            } else {
                setAlpha(disabledAlpha);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                setAlpha(touchAlpha);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            // 手指离开屏幕时将临时值还原
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setAlpha(1.0f);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
