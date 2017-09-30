package com.aliu.myapplication.pathicon;

import com.mypopsy.drawable.util.Bezier;

/**
 * Created by ALiu on 2017/7/13.
 */

public class ArrowModel2 {
    static final int HEAD_ANGLE = 45;
    public final Bezier topHead;
    public final Bezier bottomHead;
    public final Bezier body;

    public ArrowModel2(float length, float headLength, float stroke) {
        this.topHead = Bezier.line(0.0F, 0.0F, headLength, 0.0F);
        this.topHead.offset(-stroke / 2.0F, 0.0F);
        this.topHead.rotate(0.0F, 0.0F, -45.0F);
        this.topHead.offset(-length / 2.0F, 0.0F);
        this.body = Bezier.line(length / 2.0F, 0.0F, -length / 2.0F, 0.0F);
        this.bottomHead = Bezier.line(0.0F, 0.0F, headLength, 0.0F);
        this.bottomHead.offset(-stroke / 2.0F, 0.0F);
        this.bottomHead.rotate(0.0F, 0.0F, 45.0F);
        this.bottomHead.offset(-length / 2.0F, 0.0F);
    }
}
