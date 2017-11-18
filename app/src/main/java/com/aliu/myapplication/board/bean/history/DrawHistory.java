package com.aliu.myapplication.board.bean.history;

import com.aliu.myapplication.board.bean.Layer;

/**
 * 图层绘制历史记录
 *
 * @author ikould on 2017/11/18.
 */

public class DrawHistory {

    private int        id;
    private Layer.Draw draw;
    // 图层下标
    private int layerIndex;

    public DrawHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Layer.Draw getDraw() {
        return draw;
    }

    public void setDraw(Layer.Draw draw) {
        this.draw = draw;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    @Override
    public String toString() {
        return "DrawHistory{" +
                "id=" + id +
                ", draw=" + draw +
                ", layerIndex=" + layerIndex +
                '}';
    }
}
