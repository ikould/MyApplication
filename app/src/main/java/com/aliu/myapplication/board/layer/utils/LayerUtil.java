package com.aliu.myapplication.board.layer.utils;

import android.graphics.Bitmap;

import com.aliu.myapplication.board.bean.Layer;

/**
 * 图层操作工具类
 *
 * @author ikould on 2017/11/29.
 */

public class LayerUtil {

    // 当前创建图层下标量
    private static int nowCreateIndex;

    /**
     * 创建图层
     */
    public static Layer createLayer(String materialId, int layerWidth, int layerHeight) {
        Layer layer = new Layer();
        layer.setMaterialId(materialId);
        Bitmap bitmap = Bitmap.createBitmap(layerWidth, layerHeight, Bitmap.Config.ARGB_8888);
        layer.setBitmap(bitmap);
        layer.setTitle("图层" + nowCreateIndex++);
        return layer;
    }
}
