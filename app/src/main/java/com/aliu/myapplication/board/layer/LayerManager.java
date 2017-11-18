package com.aliu.myapplication.board.layer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.text.TextUtils;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.history.HistoryManager;
import com.aliu.myapplication.board.material.MaterialManager;
import com.aliu.myapplication.board.paint.PaintManager;

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

    // 图层列表
    private List<Layer> layerList;
    // 当前使用的图层
    private Layer       mLayer;
    // 当前下标
    private int         currentIndex;

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
        Layer layer = new Layer();
        layer.setMaterialId(materialId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        layer.setBitmap(bitmap);
        List<Layer.Draw> drawList = new ArrayList<>();
        layer.setDrawList(drawList);
        List<Layer.Drift> driftList = new ArrayList<>();
        layer.setDriftList(driftList);
        layer.setIndex(layerList.size() - 1);
        layerList.add(layer);
        mLayer = layer;
        currentIndex = mLayer.getIndex();
        return layer;
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


    /**
     * 切换图层
     */
    public void switchLayer(int index) {
        if (layerList != null && index < layerList.size() - 1 && index > -0) {
            mLayer = layerList.get(index);
        }
    }

    /**
     * 添加本次绘制效果
     */
    private void createDraw(Path path) {
        Layer.Draw draw = new Layer.Draw();
        draw.setPaint(PaintManager.getInstance().getPaint());
        draw.setPath(path);
        if (mLayer != null)
            mLayer.getDrawList().add(draw);
        // 添加到历史记录
        createHistory(draw, null, HistoryManager.DRAW_TYPE);
    }

    /**
     * 创建位移信息
     */
    private void createDrift(Matrix matrix) {
        Layer.Drift drift = new Layer.Drift();
        drift.setMatrix(matrix);
        if (mLayer != null)
            mLayer.getDriftList().add(drift);
        // 添加到历史记录
        createHistory(null, drift, mLayer.getIndex(), HistoryManager.DRIFT_TYPE);
    }

    /**
     * 创建历史记录
     */
    private void createHistory(Layer.Draw draw, Layer.Drift drift, int index, int type) {
        HistoryManager.getInstance().addHistory(draw, drift, index, type);
    }
}
