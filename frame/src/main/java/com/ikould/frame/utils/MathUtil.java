package com.ikould.frame.utils;

import android.graphics.Point;

/**
 * 一些简单数学公式的计算
 *
 * @author ikould on 2017/12/5.
 */

public class MathUtil {

    /**
     * 获取两条线相交点
     */
    private float[] getIntersectionPoint(float x1, float y1, float x2, float y2,
                                         float x3, float y3, float x4, float y4) {
        float[] ints = new float[2];
        //求两直线的交点，斜率相同的话res=u.a
        float t = ((x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3))
                / ((x1 - x2) * (y4 - y3) - (y1 - y2) * (x4 - x3));
        ints[0] = x1 + (x2 - x1) * t;
        ints[1] = y1 + (y2 - y1) * t;
        return ints;
    }

    /**
     * 判断是否相交
     */
    public boolean intersect3(Point aa, Point bb, Point cc, Point dd) {
        double delta = determinant(bb.x - aa.x, cc.x - dd.x, bb.y - aa.y, cc.y - dd.y);
        if (delta <= (1e-6) && delta >= -(1e-6)) {  // delta=0，表示两线段重合或平行
            return false;
        }
        double namenda = determinant(cc.x - aa.x, cc.x - dd.x, cc.y - aa.y, cc.y - dd.y) / delta;
        if (namenda > 1 || namenda < 0) {
            return false;
        }
        double miu = determinant(bb.x - aa.x, cc.x - aa.x, bb.y - aa.y, cc.y - aa.y) / delta;
        if (miu > 1 || miu < 0) {
            return false;
        }
        return true;
    }

    private double determinant(double v1, double v2, double v3, double v4) { // 行列式
        return (v1 * v3 - v2 * v4);
    }
}
