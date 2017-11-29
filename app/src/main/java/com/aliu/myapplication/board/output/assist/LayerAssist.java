package com.aliu.myapplication.board.output.assist;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.aliu.myapplication.board.adapter.LayerAdapter;
import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.layer.GraffitiViewGroup;
import com.aliu.myapplication.board.layer.LayerManager;
import com.aliu.myapplication.board.layer.LayerTask;
import com.aliu.myapplication.board.material.MaterialManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.aliu.myapplication.view.helper.SimpleItemTouchHelperCallback;

import java.util.List;

/**
 * 图层协助类
 *
 * @author ikould on 2017/11/28.
 */

public class LayerAssist implements LayerManager.OnOperateListener {

    // ====== 单例 ======

    private static LayerAssist instance;

    public static LayerAssist getInstance() {
        if (instance == null) {
            synchronized (LayerAssist.class) {
                if (instance == null) {
                    instance = new LayerAssist();
                }
            }
        }
        return instance;
    }

    private LayerAssist() {
    }

    // ====== 操作 ======

    // View
    private GraffitiViewGroup graffitiViewGroup;
    private RecyclerView      rvLayer;
    private Activity          mActivity;

    private LayerAdapter    layerAdapter;
    private ItemTouchHelper mItemTouchHelper;

    // ========== 公开方法 ==========

    /**
     * 绑定View
     */
    public void bindView(Activity activity, GraffitiViewGroup graffitiViewGroup, RecyclerView rvLayer) {
        this.mActivity = activity;
        this.rvLayer = rvLayer;
        this.graffitiViewGroup = graffitiViewGroup;
        init();
    }

    /**
     * 清除所有
     */
    public void clearAll() {
        graffitiViewGroup = null;
        rvLayer = null;
        layerAdapter = null;
        mActivity = null;
        LayerManager.getInstance().removeOperateListener(this);
    }

    // ========== 私有方法 ==========

    /**
     * 初始化
     */
    private void init() {
        // 初始化图层操作监听
        LayerManager.getInstance().addOperateListener(this);
        List<Layer> layerList = MaterialManager.getInstance().getLayerList();
        initRecyclerView(mActivity);
        LayerManager.getInstance().setLayerList(layerList);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(Activity activity) {
        layerAdapter = new LayerAdapter(activity);
        layerAdapter.setFooterView(getAddTextView(activity));
        rvLayer.setAdapter(layerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        rvLayer.setLayoutManager(linearLayoutManager);
        //绑定移动监听
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(layerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvLayer);
    }

    /**
     * 刷新UI
     */
    private void refreshUi(Layer currentLayer) {
        // 切换图层
        if (layerAdapter != null)
            layerAdapter.setCurrentLayer(currentLayer);
        // 刷新画布
        if (graffitiViewGroup != null) {
            graffitiViewGroup.refreshAllView();
            graffitiViewGroup.setCurrentLayer(currentLayer);
        }
    }

    /**
     * 创建TextView
     */
    private TextView getAddTextView(Activity activity) {
        TextView textView = new TextView(activity);
        textView.setText("+");
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);
        return textView;
    }

    @Override
    public void onOperate(int type, Object param1, Object param2) {
        switch (type) {
            case LayerManager.LAYER_DATA_SET:
                List<Layer> layerList = (List<Layer>) param1;
                graffitiViewGroup.setLayerList(layerList);
                layerAdapter.setLayerList(layerList);
                break;
            case LayerManager.LAYER_ADD:
            case LayerManager.LAYER_DELETE:
            case LayerManager.LAYER_CHOOSE:
                refreshUi((Layer) param1);
                break;
            case LayerManager.LAYER_SWAP:
                int fromPosition = (int) param1;
                int toPosition = (int) param2;
                layerAdapter.notifyItemMoved(fromPosition, toPosition);
                break;
            case LayerManager.PATH_ADD:
                break;
            case LayerManager.PATH_DRAW:
                if (layerAdapter != null)
                    layerAdapter.notifyCurrentIndex();
                break;
            case LayerManager.PATH_DRAW_OVER:
                break;
        }
    }
}
