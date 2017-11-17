package com.aliu.myapplication.board.history;

import android.text.TextUtils;

import com.aliu.myapplication.board.bean.History;
import com.aliu.myapplication.board.bean.Layer;
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

    /**
     * 添加历史记录
     */
    public void addHistory(Layer.Draw draw, Layer.Drift drift, int layerId, int type) {
        String materialId = MaterialManager.getInstance().getMaterialId();
        if (TextUtils.isEmpty(materialId))
            return;
        History history = new History();
        history.setMaterialId(materialId);
        history.setLayerIndex(layerId);
        switch (type) {
            case DRAW_TYPE:
                if (draw == null)
                    return;
                history.setDraw(draw);
                break;
            case DRIFT_TYPE:
                if (drift == null)
                    return;
                history.setDrift(drift);
                break;
            case CREATE_LAYER_TYPE:
            case DELETE_LAYER_TYPE:
                break;
            default:
                return;
        }
        history.setType(type);
        if (saveHistory == null) {
            saveHistory = new ArrayList<>();
        }
        saveHistory.add(history);
        // 清除被删除的历史记录
        if (deleteHistory != null && deleteHistory.size() > 0) {
            deleteHistory.clear();
        }
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
