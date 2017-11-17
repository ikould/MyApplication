package com.aliu.myapplication.board.layer;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.material.MaterialManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 图层管理
 *
 * @author ikould on 2017/10/27.
 */

public class LayerManager {

    /**
     * 最大图层数目
     */
    public static final int MAX_LAYER_NUM = 10;

    // ====== 单例 ======

    private static LayerManager instance;

    public static LayerManager getInstance() {
        if (instance == null) {
            synchronized (LayerManager.class) {
                if (instance == null) {
                    instance = new LayerManager();
                }
            }
        }
        return instance;
    }

    private LayerManager() {
    }

    // ====== 操作 ======

    private List<Layer> layerList;
    private Layer       mLayer;

    /**
     * 创建图层
     */
    public Layer createLayer(int width, int height) {
        String materialId = MaterialManager.getInstance().getMaterialId();
        if (TextUtils.isEmpty(materialId)) {
            return null;
        }
        if (layerList == null)
            layerList = new ArrayList<>();
        if (layerList.size() == MAX_LAYER_NUM) {
            return mLayer;
        }
        mLayer = new Layer();
        mLayer.setMaterialId(materialId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mLayer.setBitmap(bitmap);
        List<Layer.Draw> drawList = new ArrayList<>();
        mLayer.setDrawList(drawList);
        List<Layer.Drift> driftList = new ArrayList<>();
        mLayer.setDriftList(driftList);
        layerList.add(mLayer);
        return mLayer;
    }

    /**
     * 删除图层
     */
    public Layer deleteLayer() {
        if (layerList != null && layerList.contains(mLayer)) {
            int index = layerList.indexOf(mLayer);
            layerList.remove(mLayer);
            mLayer = null;
            if (layerList.size() > 0 && --index >= 0) {
                mLayer = layerList.get(index);
            }
        }
        return mLayer;
    }

    // ======== 图层内部操作 ========

    /**
     * 添加Draw
     */
    public void addDraw(Layer.Draw draw) {
        if (mLayer == null)
            return;
        List<Layer.Draw> drawList = mLayer.getDrawList();
        drawList.add(draw);
    }

    /**
     * 清除最后一个Draw
     */
    public Layer.Draw removeDraw() {
        Layer.Draw draw = null;
        if (mLayer != null) {
            List<Layer.Draw> drawList = mLayer.getDrawList();
            if (drawList != null && drawList.size() > 0) {
                draw = drawList.remove(drawList.size() - 1);
            }
        }
        return draw;
    }

    /**
     * 添加Drift
     */
    public void addDrift(Layer.Drift drift) {
        if (mLayer == null)
            return;
        List<Layer.Drift> driftList = mLayer.getDriftList();
        driftList.add(drift);
    }

    /**
     * 清除最后一个Drift
     */
    public Layer.Drift removeDrift() {
        Layer.Drift drift = null;
        if (mLayer != null) {
            List<Layer.Drift> driftList = mLayer.getDriftList();
            if (driftList != null && driftList.size() > 0) {
                drift = driftList.remove(driftList.size() - 1);
            }
        }
        return drift;
    }

}
