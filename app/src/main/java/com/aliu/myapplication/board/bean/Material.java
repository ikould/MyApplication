package com.aliu.myapplication.board.bean;

/**
 * 素材
 *
 * @author ikould on 2017/11/17.
 */

public class Material {

    // 主键
    private int    id;
    // 素材id
    private String materialId;
    // 标题
    private String title;
    // 描述
    private String describe;
    // 作者
    private String author;
    // 素材文件夹地址
    private String fileDirs;
    // 创建时间
    private long   createTime;
    // 保存时间
    private long   saveTime;

    public Material() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileDirs() {
        return fileDirs;
    }

    public void setFileDirs(String fileDirs) {
        this.fileDirs = fileDirs;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", materialId='" + materialId + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", author='" + author + '\'' +
                ", fileDirs='" + fileDirs + '\'' +
                ", createTime=" + createTime +
                ", saveTime=" + saveTime +
                '}';
    }
}
