package com.aliu.myapplication.board.bean;

/**
 * 历史记录
 *
 * @author ikould on 2017/11/17.
 */

public class History {

    // 主键
    private int    id;
    // 素材id
    private String materialId;
    // 具体历史对象
    private Object object;
    // 序列
    private int    pos;
    // 添加的时间
    private long   addTime;

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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
                ", object=" + object +
                ", pos=" + pos +
                ", addTime=" + addTime +
                '}';
    }
}
