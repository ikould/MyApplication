package com.aliu.myapplication.board.bean.history;

import com.aliu.myapplication.board.bean.Layer;

/**
 * 图层移动历史记录
 *
 * @author ikould on 2017/11/18.
 */

public class DriftHistory {

    private int         id;
    private Layer.Drift drift;
    // 图层下标
    private int         layerIndex;

    public DriftHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Layer.Drift getDrift() {
        return drift;
    }

    public void setDrift(Layer.Drift drift) {
        this.drift = drift;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    @Override
    public String toString() {
        return "DriftHistory{" +
                "id=" + id +
                ", drift=" + drift +
                ", layerIndex=" + layerIndex +
                '}';
    }
}
