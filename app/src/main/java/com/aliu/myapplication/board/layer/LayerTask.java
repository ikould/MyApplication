package com.aliu.myapplication.board.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.Log;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.history.HistoryManager;
import com.aliu.myapplication.board.layer.utils.LayerUtil;
import com.aliu.myapplication.board.material.MaterialManager;
import com.aliu.myapplication.board.paint.PaintManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图层管理
 *
 * @author ikould on 2017/10/27.
 */

public class LayerTask {

    /**
     * 最大图层数目
     */
    public static final int MAX_LAYER_NUM = 10;

    // ====== 单例 ======

    private static LayerTask instance;

    public static LayerTask getInstance() {
        if (instance == null) {
            synchronized (LayerTask.class) {
                if (instance == null) {
                    instance = new LayerTask();
                }
            }
        }
        return instance;
    }

    private LayerTask() {
    }

    // ====== 操作 ======

    // 图层列表
    private List<Layer> layerList;
    // 当前使用的图层
    private Layer       mLayer;

    /**
     * 设置数据
     */
    public void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
    }

    /**
     * 创建图层
     */
    public Layer createLayer(int width, int height) {
        String materialId = MaterialManager.getInstance().getMaterialId();
        if (TextUtils.isEmpty(materialId) || layerList == null || layerList.size() == MAX_LAYER_NUM) {
            return mLayer;
        }
        Log.d("LayerTask", "createLayer: size = " + layerList.size());
        Layer layer = LayerUtil.createLayer(materialId, width, height);
        layerList.add(layer);
        mLayer = layer;
        // 添加历史
        //  HistoryManager.getInstance().addHistory(currentIndex, 0);
        return mLayer;
    }

    /**
     * 删除图层
     */
    public Layer deleteLayer(Layer layer) {
        Log.d("LayerTask", "deleteLayer: layer = " + layer);
        if (layerList != null) {
            int index = layerList.indexOf(layer);
            Log.d("LayerTask", "deleteLayer: index = " + index);
            layerList.remove(layer);
            if (mLayer == layer) { // 删除的Layer是当前mLayer，需要重新选择
                // 选择下一个Layer
                index++;
                if (layerList.size() > 0) {
                    if (index > layerList.size() - 1) {
                        index = layerList.size() - 1;
                    }
                    mLayer = layerList.get(index);
                }
            }
            Log.d("LayerTask", "deleteLayer: resultLayer = " + mLayer);
        }
        return mLayer;
    }

    /**
     * 切换位置
     */
    public void swapLayer(int fromPosition, int toPosition) {
        Collections.swap(layerList, fromPosition, toPosition);
    }

    /**
     * 切换图层
     */
    public void switchLayer(Layer layer) {
        mLayer = layer;
    }
}
