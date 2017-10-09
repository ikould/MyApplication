package com.aliu.myapplication.board.shape;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;


/**
 * 形状管理类
 * <p>
 * Created by liudong on 2017/9/28.
 */

public class ShapeManager {

    // 默认
    public static final int SHAPE_DEFAULT = 0x01;
    // 长方形
    public static final int SHAPE_RECT = 0x02;
    // 正方形
    public static final int SHAPE_SQUARE = 0x03;
    // 五角星
    public static final int SHAPE_FIVE_POINTED_STAR = 0x04;
    // 圆形
    public static final int SHAPE_CIRCULAR = 0x05;
    // 椭圆
    public static final int SHAPE_ELLIPSE = 0x06;
    // 弧度
    public static final int SHAPE_RADIAN = 0x07;
    // 箭头
    public static final int SHAPE_ARROW = 0x08;
    // 自定义图形
    public static final int SHAPE_CUSTOM = 0x09;

    // 方向，默认顺时针
    private Path.Direction direction = Path.Direction.CW;

    // ====== 单例 ======
    private static ShapeManager instance;

    public static ShapeManager getInstance() {
        if (instance == null)
            synchronized (ShapeManager.class) {
                if (instance == null)
                    instance = new ShapeManager();
            }
        return instance;
    }

    private ShapeManager() {
    }

    // ====== 操作 ======

    // 类型
    private int type = SHAPE_DEFAULT;
    // 画弧度的角度
    private float arcAngle = 120;

    // ========== 公开方法 ==========

    public void setShapeType(int type) {
        this.type = type;
    }

    public boolean onDraw(Path path, float[] eventPoints) {
        Log.d("ShapeManager", "onDraw:  type = " + type);
        switch (type) {
            case SHAPE_DEFAULT:
                return false;
            case SHAPE_RECT: // 长方形
                onDrawRect(path, eventPoints);
                break;
            case SHAPE_SQUARE:  // 正方形
                onDrawSquare(path, eventPoints);
                break;
            case SHAPE_FIVE_POINTED_STAR:  // 五角星
                onDrawFivePointStar(path, eventPoints);
                break;
            case SHAPE_CIRCULAR:  // 圆形
                onDrawCircular(path, eventPoints);
                break;
            case SHAPE_ELLIPSE:    // 椭圆
                onDrawEllipse(path, eventPoints);
                break;
            case SHAPE_RADIAN: // 弧度
                onDrawRadian(path, eventPoints);
                break;
            case SHAPE_ARROW:  // 箭头
                onDrawArrow(path, eventPoints);
                break;
            case SHAPE_CUSTOM:   // 自定义图形
                onDrawCustom(path, eventPoints);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * 画长方形
     */
    private void onDrawRect(Path path, float[] eventPoints) {
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        path.reset();
        path.addRect(eventPoints[0] - distanceX, eventPoints[1] - distanceY, eventPoints[0] + distanceX, eventPoints[1] + distanceY, direction);
    }

    /**
     * 画正方形
     */
    private void onDrawSquare(Path path, float[] eventPoints) {
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        float max = Math.max(distanceX, distanceY);
        path.reset();
        path.addRect(eventPoints[0] - max, eventPoints[1] - max, eventPoints[0] + max, eventPoints[1] + max, direction);
    }

    /**
     * 画五角星
     */
    private void onDrawFivePointStar(Path path, float[] eventPoints) {
        float downX = eventPoints[0];
        float downY = eventPoints[1];
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        float outR = Math.max(distanceX, distanceY);
        float inR = outR * sin(18) / sin(180 - 36 - 18);
        path.reset();
        path.moveTo(outR * cos(72 * 0 - 90) + downX, outR * sin(72 * 0 - 90) + downY);
        path.lineTo(inR * cos(72 * 0 + 36 - 90) + downX, inR * sin(72 * 0 + 36 - 90) + downY);
        path.lineTo(outR * cos(72 * 1 - 90) + downX, outR * sin(72 * 1 - 90) + downY);
        path.lineTo(inR * cos(72 * 1 + 36 - 90) + downX, inR * sin(72 * 1 + 36 - 90) + downY);
        path.lineTo(outR * cos(72 * 2 - 90) + downX, outR * sin(72 * 2 - 90) + downY);
        path.lineTo(inR * cos(72 * 2 + 36 - 90) + downX, inR * sin(72 * 2 + 36 - 90) + downY);
        path.lineTo(outR * cos(72 * 3 - 90) + downX, outR * sin(72 * 3 - 90) + downY);
        path.lineTo(inR * cos(72 * 3 + 36 - 90) + downX, inR * sin(72 * 3 + 36 - 90) + downY);
        path.lineTo(outR * cos(72 * 4 - 90) + downX, outR * sin(72 * 4 - 90) + downY);
        path.lineTo(inR * cos(72 * 4 + 36 - 90) + downX, inR * sin(72 * 4 + 36 - 90) + downY);
        path.lineTo(outR * cos(72 * 0 - 90) + downX, outR * sin(72 * 0 - 90) + downY);
    }

    /**
     * 画圆形
     */
    private void onDrawCircular(Path path, float[] eventPoints) {
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        float r = Math.max(distanceX, distanceY);
        path.reset();
        path.addCircle(eventPoints[0], eventPoints[1], r, direction);
    }

    /**
     * 画椭圆
     */
    private void onDrawEllipse(Path path, float[] eventPoints) {
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        path.reset();
        path.addOval(new RectF(eventPoints[0] - distanceX, eventPoints[1] - distanceY, eventPoints[0] + distanceX, eventPoints[1] + distanceY), direction);
    }

    /**
     * 画弧度
     */
    private void onDrawRadian(Path path, float[] eventPoints) {
        float distanceX = Math.abs(eventPoints[0] - eventPoints[2]);
        float distanceY = Math.abs(eventPoints[1] - eventPoints[3]);
        float max = Math.max(distanceX, distanceY);
        float angle = (float) (Math.atan2(eventPoints[3] - eventPoints[1], eventPoints[2] - eventPoints[0]) / Math.PI * 180);
        Log.d("GraffitiView", "touchMove: angle = " + angle);
        float startAngle = angle - arcAngle / 2;
        float endAngle = angle + arcAngle / 2;
        Log.d("GraffitiView", "touchMove: startAngle = " + startAngle + " endAngle = " + endAngle);
        path.reset();
        path.addArc(new RectF(eventPoints[0] - max, eventPoints[1] - max, eventPoints[0] + max, eventPoints[1] + max), startAngle, arcAngle);
    }

    /**
     * 画箭头
     */
    private void onDrawArrow(Path path, float[] eventPoints) {
        // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
        int angle = (int) (Math.atan2(eventPoints[3] - eventPoints[1], eventPoints[2] - eventPoints[0]) / Math.PI * 180);
        path.reset();
        path.moveTo(eventPoints[0], eventPoints[1]);
        path.lineTo(eventPoints[2], eventPoints[3]);
        float length = (float) Math.sqrt(Math.pow(eventPoints[2] - eventPoints[0], 2) + Math.pow(eventPoints[3] - eventPoints[1], 2));
        Log.d("GraffitiView", "addArrow: angle = " + angle);
        if (length < 100)
            return;
        float lengthArrow = length * 0.25f;
        int slideAngle = 45;
        int arrowAngle = 30;
        float slide = (float) (Math.tan(arrowAngle * 1.0f / 180 * Math.PI) * lengthArrow);
        float slideLength = slide / cos(slideAngle);
        path.lineTo(cos(360 - (90 - angle + slideAngle)) * slideLength + eventPoints[2], sin(360 - (90 - angle + slideAngle)) * slideLength + eventPoints[3]);
        path.lineTo(cos(angle) * lengthArrow + eventPoints[2], sin(angle) * lengthArrow + eventPoints[3]);
        path.lineTo(cos(90 + slideAngle + angle) * slideLength + eventPoints[2], sin(90 + slideAngle + angle) * slideLength + eventPoints[3]);
        path.lineTo(eventPoints[2], eventPoints[3]);
    }

    /**
     * 画自定义图像
     */
    private void onDrawCustom(Path path, float[] eventPoints) {

    }
    // ========== 私有方法 ==========

    private float cos(int num) {
        return (float) Math.cos(num * Math.PI / 180);
    }

    private float sin(int num) {
        return (float) Math.sin(num * Math.PI / 180);
    }

}
