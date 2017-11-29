package com.aliu.myapplication.board.material;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.layer.utils.LayerUtil;

import java.util.ArrayList;
import java.util.List;

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

    private List<Layer> layerList;
    private String      materialId;

    // ========== 公开方法 ==========

    /**
     * 初始化
     */
    public void initMaterial(String filePath, int layerWidth, int layerHeight) {
        // ... TODO 文件解析
        // 打开默认清空模板
        materialId = generalMaterialId("00");
        initLayerList(null, materialId, layerWidth, layerHeight);
    }

    /**
     * 初始化LayerList
     *
     * @param layerList 原先的数据
     */
    public void initLayerList(List<Layer> layerList, String materialId, int layerWidth, int layerHeight) {
        if (layerList == null) {
            layerList = new ArrayList<>();
        }
        if (layerList.size() == 0) {
            // 创建一个默认的图层
            Layer layer = LayerUtil.createLayer(materialId, layerWidth, layerHeight);
            layerList.add(layer);
        }
        this.layerList = layerList;
    }

    /**
     * 返回当前的LayerList
     */
    public List<Layer> getLayerList() {
        return layerList;
    }

    public String getMaterialId() {
        return materialId;
    }

    // ========== 私有方法 ==========

    // 生成素材id
    private String generalMaterialId(String userId) {
        return userId + "_" + String.valueOf(System.currentTimeMillis());
    }


}
