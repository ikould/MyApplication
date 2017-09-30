package com.aliu.myapplication.pathicon;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;

import com.aliu.myapplication.R;
import com.mypopsy.drawable.ToggleDrawable;

/**
 * Created by ALiu on 2017/7/13.
 */

public class DrawerArrowDrawable extends ToggleDrawable {

    public DrawerArrowDrawable(Context context) {
        this(context, R.attr.drawerArrowDrawableStyle, R.style.DrawerArrowDrawable);
    }

    public DrawerArrowDrawable(Context context, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, defStyleAttr, defStyleRes);
        mSpin = false;
        DrawerModel2 drawer = new DrawerModel2(48, 18);
        ArrowModel2 arrow = new ArrowModel2(42, 34, getStrokeWidth());

//        add(drawer.topLine, arrow.topHead);
//        add(drawer.middleLine, arrow.body);
//        add(drawer.bottomLine, arrow.bottomHead);

        add(drawer.topLine, arrow.topHead);
        add(drawer.middleLine, arrow.body);
        add(drawer.bottomLine, arrow.bottomHead);
    }
}