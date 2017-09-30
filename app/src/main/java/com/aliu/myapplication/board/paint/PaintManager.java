package com.aliu.myapplication.board.paint;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * 画笔管理类
 * <p>
 * Created by liudong on 2017/9/28.
 */

public class PaintManager {

    /**
     * 默认画笔颜色组合
     */
    private final static int[] PAINT_DEFAULT_COLOR = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.GRAY, Color.CYAN};

    // ====== 单例 ======
    private static PaintManager instance;

    public static PaintManager getInstance() {
        if (instance == null)
            synchronized (PaintManager.class) {
                if (instance == null)
                    instance = new PaintManager();
            }
        return instance;
    }

    private PaintManager() {
        init();
    }

    // ====== 操作 ======
    // 画笔
    private Paint mPaint;
    private int   mColor;
    private int   mSize;
    private int   mState; // 0 表示正常模式 1 表示橡皮擦

    // ========== 公开方法 ==========

    /**
     * 获取画笔
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 设置画笔颜色
     */
    public void setPaintColorIndex(int colorIndex) {
        if (colorIndex < 0 || colorIndex >= PAINT_DEFAULT_COLOR.length)
            return;
        this.mColor = PAINT_DEFAULT_COLOR[colorIndex];
        mPaint.setColor(mColor);
    }

    public void setPaintColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    /**
     * 设置是否是橡皮擦
     */
    public void setIsEraser(boolean isEraser) {
        if (isEraser) {
            mState = 1;
        } else {
            mState = 0;
        }
        setState();
    }

    /**
     * 设置画笔大小
     */
    public void setPaintSize(int size) {
        this.mSize = size;
        mPaint.setStrokeWidth(mSize);
    }

    // ========== 私有方法 ==========

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setPathEffect(new DashPathEffect(new float[]{1, 2, 4, 8}, 1));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mSize);
        setState();
    }

    /**
     * 设置状态
     */
    private void setState() {
        if (mState == 0) {
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            mPaint.setAlpha(255);
            mPaint.setColor(mColor);
        } else {//橡皮擦
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setAlpha(0);
            mPaint.setColor(Color.TRANSPARENT);
        }
    }
}
