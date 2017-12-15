package com.aliu.myapplication.board.layer;

import android.graphics.Matrix;
import android.graphics.Path;
import android.util.Log;

import com.aliu.myapplication.board.bean.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * describe
 *
 * @author ikould on 2017/11/29.
 */

public class LayerManager {

    // 设置图层数据
    public static final int LAYER_DATA_SET  = 0x00;
    // 添加图层
    public static final int LAYER_ADD       = 0x01;
    // 删除图层
    public static final int LAYER_DELETE    = 0x02;
    // 选择图层
    public static final int LAYER_CHOOSE    = 0x03;
    // 图层顺序切换
    public static final int LAYER_SWAP      = 0x04;
    // Path添加
    public static final int PATH_ADD        = 0x05;
    // Path的矩阵添加
    public static final int PATH_MATRIX_ADD = 0x06;
    // Path删除
    public static final int PATH_DELETE     = 0x07;
    // Path渲染
    public static final int PATH_DRAW       = 0x08;
    // Path渲染结束
    public static final int PATH_DRAW_OVER  = 0x09;
    // Img添加
    public static final int IMG_ADD         = 0x0a;
    // Img删除
    public static final int IMG_DELETE      = 0x0b;
    // Img渲染
    public static final int IMG_DRAW        = 0x0c;
    // Img渲染结束
    public static final int IMG_DRAW_OVER   = 0x0d;

    // ====== 单例 ======

    private static LayerManager instance;

    public static LayerManager getInstance() {
        if (instance == null) {
            synchronized (LayerManager.class) {
                if (instance == null) {
                    instance = new LayerManager();
                }
            }
        }
        return instance;
    }

    private LayerManager() {
    }

    // ====== 操作 ======

    //  设置图层数据
    public void setLayerList(List<Layer> layerList) {
        LayerTask.getInstance().setLayerList(layerList);
        doOperateListener(LAYER_DATA_SET, layerList, null);// 参数一：当前图层数据
        if (layerList != null && layerList.size() > 0)
            doOperateListener(LAYER_CHOOSE, layerList.get(0), null);// 参数一：当前图层数据
    }

    // 添加图层
    public void addLayer(int layerWidth, int layerHeight) {
        Layer resultLayer = LayerTask.getInstance().createLayer(layerWidth, layerHeight);
        // 切换到当前图层
        doOperateListener(LAYER_ADD, resultLayer, null);
    }

    // 删除图层
    public void deleteLayer(Layer deleteLayer) {
        Layer resultLayer = LayerTask.getInstance().deleteLayer(deleteLayer);
        doOperateListener(LAYER_DELETE, resultLayer, null);
    }

    // 选择图层
    public void chooseLayer(Layer chooseLayer) {
        Log.d("LayerManager", "chooseLayer: chooseLayer = onTouch:" + chooseLayer);
        LayerTask.getInstance().switchLayer(chooseLayer);
        doOperateListener(LAYER_CHOOSE, chooseLayer, null);// 参数一：当前选择的图层下标
    }

    // 图层顺序切换
    public void swapLayer(int fromPosition, int toPosition) {
        LayerTask.getInstance().swapLayer(fromPosition, toPosition);
        doOperateListener(LAYER_SWAP, fromPosition, toPosition);
    }

    // Path添加
    public void addPathDraw(Path path) {
        doOperateListener(PATH_ADD, path, null);
    }

    // 添加Path矩阵
    public void addPathMatrix(Matrix matrix, Layer.PathDraw pathDraw) {
        pathDraw.setPositionInfo(matrix);
        doOperateListener(PATH_MATRIX_ADD, matrix, pathDraw);
    }

    // Path删除
    public void deletePathDraw() {

    }

    // Path渲染
    public void renderPathDraw() {
        doOperateListener(PATH_DRAW, null, null);
    }

    // Path渲染结束
    public void renderOverPathDraw() {
        doOperateListener(PATH_DRAW_OVER, null, null);
    }

    // Img添加
    public void addImgDraw() {

    }

    // Img删除
    public void deleteImgDraw() {

    }

    // Img渲染
    public void renderImgDraw() {

    }

    // Img渲染结束
    public void renderOverImgDraw() {

    }

    // ======== 监听 ========

    private List<OnOperateListener> operateListenerList;

    // 执行监听
    private void doOperateListener(int type, Object param1, Object param2) {
        if (operateListenerList != null) {
            for (OnOperateListener onOperateListener : operateListenerList) {
                onOperateListener.onOperate(type, param1, param2);
            }
        }
    }

    // 添加监听
    public void addOperateListener(OnOperateListener operateListener) {
        if (operateListenerList == null)
            operateListenerList = new ArrayList<>();
        operateListenerList.add(operateListener);
    }

    // 移除监听
    public void removeOperateListener(OnOperateListener operateListener) {
        if (operateListenerList != null)
            operateListenerList.remove(operateListener);
    }

    // 操作监听类
    public interface OnOperateListener {
        void onOperate(int type, Object param1, Object param2);
    }
}
