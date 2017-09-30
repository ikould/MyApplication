package com.aliu.myapplication.pathicon;

import com.mypopsy.drawable.util.Bezier;

/**
 * Created by ALiu on 2017/7/13.
 */

public class DrawerModel2 {

    public final Bezier topLine;
    public final Bezier middleLine;
    public final Bezier bottomLine;

    public DrawerModel2(float length, float gapSize) {
        this.topLine = Bezier.line(-length / 2.0F, 0.0F, length / 2.0F, 0.0F);
        this.topLine.offset(0.0F, -gapSize);
        this.middleLine = Bezier.line(-length / 2.0F, 0.0F, length / 2.0F * 9.0f / 24.0f, 0.0F);
        this.bottomLine = Bezier.line(-length / 2.0F, 0.0F, length / 2.0F, 0.0F);
        this.bottomLine.offset(0.0F, gapSize);
    }
}
