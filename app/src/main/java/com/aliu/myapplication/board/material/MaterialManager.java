package com.aliu.myapplication.board.material;

/**
 * 素材管理
 *
 * @author ikould on 2017/11/17.
 */

public class MaterialManager {

    // ====== 单例 ======

    private static MaterialManager instance;

    public static MaterialManager getInstance() {
        if (instance == null) {
            synchronized (MaterialManager.class) {
                if (instance == null) {
                    instance = new MaterialManager();
                }
            }
        }
        return instance;
    }

    private MaterialManager() {
    }

    // ====== 操作 ======

    private String materialId;

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialId() {
        return materialId;
    }

}
