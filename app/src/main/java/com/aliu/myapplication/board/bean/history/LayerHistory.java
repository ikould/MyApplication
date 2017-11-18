package com.aliu.myapplication.board.bean.history;

/**
 * 图层添加或者移除历史记录
 *
 * @author ikould on 2017/11/18.
 */

public class LayerHistory {

    private int id;
    // 0: 添加 1:移除
    private int type;
    // 图层下标
    private int layerIndex;

    public LayerHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    @Override
    public String toString() {
        return "LayerHistory{" +
                "id=" + id +
                ", type=" + type +
                ", layerIndex=" + layerIndex +
                '}';
    }
}
