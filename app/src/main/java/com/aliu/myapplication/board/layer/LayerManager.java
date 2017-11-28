package com.aliu.myapplication.board.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.Log;

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
    private List<Layer> layerList = new ArrayList<>();
    // 当前使用的图层
    private Layer mLayer;
    // 当前下标
    private int   currentIndex;
    private int   nowCreateIndex;

    /**
     * 创建图层
     */
    public int createLayer(int width, int height) {
        String materialId = MaterialManager.getInstance().getMaterialId();
        if (TextUtils.isEmpty(materialId)) {
            return currentIndex;
        }
        if (layerList.size() == MAX_LAYER_NUM) {
            return currentIndex;
        }
        Layer layer = new Layer();
        layer.setMaterialId(materialId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        layer.setBitmap(bitmap);
        List<Layer.Draw> drawList = new ArrayList<>();
        layer.setDrawList(drawList);
        List<Layer.Drift> driftList = new ArrayList<>();
        layer.setDriftList(driftList);
        layerList.add(layer);
        currentIndex = layerList.size() - 1;
        layer.setTitle("图层" + nowCreateIndex++);
        mLayer = layer;
        // 添加历史
        HistoryManager.getInstance().addHistory(currentIndex, 0);
        return currentIndex;
    }

    /**
     * 删除图层
     */
    public int deleteLayer(int index) {
        if (layerList != null) {
            if (index >= 0 && index < layerList.size()) {
                layerList.remove(index);
                // 切换到下一个图层
                index++;
                switchLayer(index);
            }
        }
        return currentIndex;
    }

    /**
     * 切换图层
     */
    public void switchLayer(int index) {
        Log.d("LayerManager", "switchLayer: index = " + index + " size = " + layerList.size());
        if (layerList != null && layerList.size() > 0) {
            if (index < 0)
                index = 0;
            if (index > layerList.size() - 1)
                index = layerList.size() - 1;
            currentIndex = index;
            mLayer = layerList.get(index);
        } else {
            currentIndex = -1;
            mLayer = null;
        }
    }

    public List<Layer> getLayerList() {
        return layerList;
    }

    // ======== 图层内部操作 ========

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

    private Layer.Draw draw;
    private Bitmap     layerBitmap;
    private Canvas     canvasTemp;

    /**
     * 添加本次绘制效果
     * 保留之前的Bitmap，重新设置一个Bitmap，绘制时先将原先的Bitmap绘制上去，再将目前的Path也绘制上去
     */
    public void createDraw(Path path) {
        if (mLayer != null) {
            layerBitmap = mLayer.getBitmap();
            Bitmap drawBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            canvasTemp = new Canvas(drawBitmap);
            canvasTemp.drawColor(Color.TRANSPARENT);
            mLayer.setBitmap(drawBitmap);
            draw = new Layer.Draw();
            Paint paint = PaintManager.getInstance().getPaint();
            draw.setPaint(paint);
            draw.setPath(path);
            // 添加到历史记录
        }
    }

    /**
     * 绘制Path
     */
    public void drawPath() {
        if (mLayer != null && draw != null) {
            canvasTemp.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvasTemp.drawBitmap(layerBitmap, 0, 0, null);
            canvasTemp.drawPath(draw.getPath(), draw.getPaint());
        }
    }

    /**
     * 绘制Path
     */
    public void drawOver() {
        if (layerBitmap != null && !layerBitmap.isRecycled()) {
            layerBitmap.recycle();
        }
    }

    /**
     * 创建位移信息
     */
    public void createDrift(Matrix matrix) {
        Layer.Drift drift = new Layer.Drift();
        drift.setMatrix(matrix);
        if (mLayer != null)
            mLayer.getDriftList().add(drift);
        // 添加到历史记录
    }

    /**
     * 创建历史记录
     */
    private void createHistory(Layer.Draw draw, Layer.Drift drift, int index, int type) {
        // HistoryManager.getInstance().addHistory(draw, drift, index, type);
    }
}
