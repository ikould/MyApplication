package com.aliu.myapplication.board.bean;

/**
 * 历史
 *
 * @author ikould on 2017/11/17.
 */

public class History {

    // 主键
    private int         id;
    // 素材id
    private String      materialId;
    // 图层下标
    private int         layerIndex;
    // 类型 0：表示操作绘图 1：表示操作矩阵
    private int         type;
    // 当前绘图记录
    private Layer.Drift drift;
    // 当前矩阵操作记录
    private Layer.Draw  draw;
    // 序列
    private int         pos;
    // 添加的时间
    private long        addTime;

    public History() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Layer.Drift getDrift() {
        return drift;
    }

    public void setDrift(Layer.Drift drift) {
        this.drift = drift;
    }

    public Layer.Draw getDraw() {
        return draw;
    }

    public void setDraw(Layer.Draw draw) {
        this.draw = draw;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", materialId='" + materialId + '\'' +
                ", layerIndex=" + layerIndex +
                ", type=" + type +
                ", drift=" + drift +
                ", draw=" + draw +
                ", pos=" + pos +
                ", addTime=" + addTime +
                '}';
    }
}
