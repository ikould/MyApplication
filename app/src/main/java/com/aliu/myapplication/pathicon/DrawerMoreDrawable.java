package com.aliu.myapplication.pathicon;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.StyleRes;

import com.aliu.myapplication.R;
import com.mypopsy.drawable.ToggleDrawable;

/**
 * Created by ALiu on 2017/7/13.
 */

public class DrawerMoreDrawable extends ToggleDrawable {

    public DrawerMoreDrawable(Context context) {
        this(context, R.attr.drawerArrowDrawableStyle, R.style.DrawerArrowDrawable);
    }

    public DrawerMoreDrawable(Context context, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, defStyleAttr, defStyleRes);
        mSpin = false;
        MoreModel2 top = new MoreModel2(28, 12, true, getStrokeWidth(), 29.0f);
        MoreModel2 bottom = new MoreModel2(28, 12, false, getStrokeWidth(), 29.0f);


        add(top.topLeftLine, bottom.topLeftLine);
        add(top.topRightLine, bottom.topRightLine);
        add(top.bottomLeftLine, bottom.bottomLeftLine);
        add(top.bottomRightLine, bottom.bottomRightLine);
    }
}