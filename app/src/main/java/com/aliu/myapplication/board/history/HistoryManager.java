package com.aliu.myapplication.board.history;

import android.text.TextUtils;

import com.aliu.myapplication.board.bean.History;
import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.bean.history.DrawHistory;
import com.aliu.myapplication.board.bean.history.DriftHistory;
import com.aliu.myapplication.board.bean.history.LayerHistory;
import com.aliu.myapplication.board.material.MaterialManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史管理器
 *
 * @author ikould on 2017/11/17.
 */

public class HistoryManager {

    // 图层内部绘制操作
    public static final int DRAW_TYPE         = 0;
    // 图层内部矩阵操作
    public static final int DRIFT_TYPE        = 1;
    // 创建图层
    public static final int CREATE_LAYER_TYPE = 2;
    // 删除图层
    public static final int DELETE_LAYER_TYPE = 3;

    // ====== 单例 ======

    private static HistoryManager instance;

    public static HistoryManager getInstance() {
        if (instance == null) {
            synchronized (HistoryManager.class) {
                if (instance == null) {
                    instance = new HistoryManager();
                }
            }
        }
        return instance;
    }

    private HistoryManager() {
    }

    // ====== 操作 ======

    private List<History> saveHistory;
    private List<History> deleteHistory;


    // ========== 公开方法 ==========

    /**
     * 添加绘制的历史记录
     *
     * @param draw       绘制具体操作
     * @param layerIndex 当前图层下标
     */
    public void addHistory(Layer.Draw draw, int layerIndex) {
        DrawHistory drawHistory = new DrawHistory();
        drawHistory.setDraw(draw);
        drawHistory.setLayerIndex(layerIndex);
        addHistory(drawHistory);
    }

    /**
     * 添加矩阵操作的历史记录
     *
     * @param drift      矩阵具体操作
     * @param layerIndex 当前图层下标
     */
    public void addHistory(Layer.Drift drift, int layerIndex) {
        DriftHistory driftHistory = new DriftHistory();
        driftHistory.setDrift(drift);
        driftHistory.setLayerIndex(layerIndex);
        addHistory(driftHistory);
    }

    /**
     * 创建或者销毁图层操作的历史记录
     *
     * @param layerIndex  操作图层的下标
     * @param addOrRemove 0: 添加 1:销毁
     */
    public void addHistory(int layerIndex, int addOrRemove) {
        LayerHistory layerHistory = new LayerHistory();
        layerHistory.setLayerIndex(layerIndex);
        layerHistory.setType(addOrRemove);
    }

    /**
     * 图层顺序变动
     *
     * @param layerIndex 当前图层下标
     * @param fromIndex  开始位置
     * @param toIndex    结束位置
     */
    public void addHistory(int layerIndex, int fromIndex, int toIndex) {

    }

    // ========== 私有方法 ==========

    /**
     * 当前具体操作添加到历史纪录
     */
    private void addHistory(Object object) {
        String materialId = MaterialManager.getInstance().getMaterialId();
        if (TextUtils.isEmpty(materialId))
            return;
        History history = new History();
        history.setMaterialId(materialId);
        history.setObject(object);
        if (saveHistory == null) {
            saveHistory = new ArrayList<>();
        }
        history.setPos(saveHistory.size());
        history.setAddTime(System.currentTimeMillis());
        saveHistory.add(history);
        // 清除被删除的历史记录
        if (deleteHistory != null && deleteHistory.size() > 0) {
            deleteHistory.clear();
        }
    }

    /**
     * 添加历史记录
     */
    public void addHistory(Layer.Draw draw, Layer.Drift drift, int layerIndex, int type) {

    }

    /**
     * 删除最后一条历史记录
     */
    public History deleteLastHistory() {
        History history = null;
        if (saveHistory != null && saveHistory.size() > 0) {
            history = saveHistory.remove(saveHistory.size() - 1);
            if (deleteHistory == null) {
                deleteHistory = new ArrayList<>();
            }
            deleteHistory.add(history);
        }
        return history;
    }

    /**
     * 恢复最后一次撤销的历史记录
     */
    public History recoverHistory() {
        History history = null;
        if (deleteHistory != null && deleteHistory.size() > 0) {
            history = deleteHistory.remove(saveHistory.size() - 1);
            if (saveHistory != null) {
                saveHistory.add(history);
            }
        }
        return history;
    }
}
