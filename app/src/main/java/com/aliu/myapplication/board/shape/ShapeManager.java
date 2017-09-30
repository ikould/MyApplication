package com.aliu.myapplication.board.shape;

/**
 * 形状管理类
 * <p>
 * Created by liudong on 2017/9/28.
 */

public class ShapeManager {

    public static final int DEFAULT = 0x01;
    public static final int RECT = 0x02;
    public static final int SQUARE = 0x03;

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
    private int type = 8; // 0 默认，1 长方形 ，2 正方形 ， 3 五角星 ，4 圆形 ， 5 椭圆 ,6 弧度 ，7 箭头， 8 自定义图形

    // ========== 公开方法 ==========

    // ========== 私有方法 ==========


}
