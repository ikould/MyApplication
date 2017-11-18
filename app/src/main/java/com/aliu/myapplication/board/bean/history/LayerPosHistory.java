package com.aliu.myapplication.board.bean.history;

/**
 * 图层顺序历史记录
 *
 * @author ikould on 2017/11/18.
 */

public class LayerPosHistory {

    private int id;
    // 图层下标
    private int layerIndex;
    // 开始位置
    private int fromIndex;
    // 结束位置
    private int toIndex;

    public LayerPosHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

    @Override
    public String toString() {
        return "LayerPosHistory{" +
                "id=" + id +
                ", fromIndex=" + fromIndex +
                ", toIndex=" + toIndex +
                '}';
    }
}
