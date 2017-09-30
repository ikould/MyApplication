package com.aliu.myapplication.pathicon;

import com.mypopsy.drawable.util.Bezier;

/**
 * Created by ALiu on 2017/7/13.
 */

public class MoreModel2 {

    public final Bezier topLeftLine;
    public final Bezier topRightLine;
    public final Bezier bottomLeftLine;
    public final Bezier bottomRightLine;

    public MoreModel2(float length, float gapSize, boolean top, float stroke, float single) {

        this.topRightLine = Bezier.line(0.0F, 0.0F, length, 0.0F);
        this.topRightLine.offset(-stroke / 4.0F, 0.0F);
        this.topRightLine.rotate(0.0F, 0.0F, top ? single : -single);
        this.topRightLine.offset(0.0F, gapSize);

        this.topLeftLine = Bezier.line(0.0F, 0.0F, length, 0.0F);
        this.topLeftLine.offset(-stroke / 4.0F, 0.0F);
        this.topLeftLine.rotate(0.0F, 0.0F, top ? 180.f - single : single - 180.0f);
        this.topLeftLine.offset(0.0F, gapSize);


        this.bottomRightLine = Bezier.line(0.0F, 0.0F, length, 0.0F);
        this.bottomRightLine.offset(-stroke / 4.0F, 0.0F);
        this.bottomRightLine.rotate(0.0F, 0.0F, top ? single : -single);
        this.bottomRightLine.offset(0.0F, -gapSize);

        this.bottomLeftLine = Bezier.line(0.0F, 0.0F, length, 0.0F);
        this.bottomLeftLine.offset(-stroke / 4.0F, 0.0F);
        this.bottomLeftLine.rotate(0.0F, 0.0F, top ? 180.f - single : single - 180.0f);
        this.bottomLeftLine.offset(0.0F, -gapSize);

    }
}
