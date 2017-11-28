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
import android.widget.Toast;

import com.aliu.myapplication.board.adapter.LayerAdapter;
import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.layer.GraffitiViewGroup;
import com.aliu.myapplication.board.layer.LayerManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.aliu.myapplication.view.helper.SimpleItemTouchHelperCallback;
import com.ikould.frame.utils.ScreenUtils;

import java.util.List;

/**
 * 图层协助类
 *
 * @author ikould on 2017/11/28.
 */

public class LayerAssist {

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

    private GraffitiViewGroup graffitiViewGroup;
    private RecyclerView      rvLayer;
    private Activity          mActivity;
    private List<Layer>       layerList;
    private LayerAdapter      layerAdapter;
    // 触摸事件触发点，downX，downY，currentX，currentY
    private float[] eventPoints = new float[4];
    private Path mPath;

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
        layerList = null;
    }

    // ========== 私有方法 ==========

    /**
     * 初始化
     */
    private void init() {
        layerList = LayerManager.getInstance().getLayerList();
        if (layerList != null && layerList.size() == 0) {
            addLayer(mActivity);  // 默认加一个图层
        }
        graffitiViewGroup.setLayerList(layerList);
        initRecyclerView(mActivity);
        initListener();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(Activity activity) {
        layerAdapter = new LayerAdapter(activity);
        layerAdapter.setFooterView(getAddTextView(activity));
        rvLayer.setAdapter(layerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvLayer.setLayoutManager(linearLayoutManager);
        layerAdapter.setLayerList(layerList);
        layerAdapter.setOnItemClickListener(new LayerAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position, int type) {
                switch (type) {
                    case LayerAdapter.TYPE_HEADER:
                        break;
                    case LayerAdapter.TYPE_FOOTER:
                        addLayer(mActivity);
                        break;
                    case LayerAdapter.TYPE_NORMAL:
                        // 切换到当前图层
                        LayerManager.getInstance().switchLayer(position);
                        layerAdapter.setCurrentIndex(position);
                        if (graffitiViewGroup != null)
                            graffitiViewGroup.setCurrentIndex(position);
                        break;
                }
            }

            @Override
            public void onLongClickListener(int position, int type) {
                switch (type) {
                    case LayerAdapter.TYPE_NORMAL:
                        // 删除选择的图层
                        deleteLayer(position);
                        break;
                }
            }
        });
        //绑定移动监听
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(layerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rvLayer);
        layerAdapter.setOnDragListener(new LayerAdapter.OnDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }

            @Override
            public void onDelete(int index) {

            }
        });
    }

    private ItemTouchHelper mItemTouchHelper;

    /**
     * 初始化监听
     */
    private void initListener() {
        graffitiViewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eventPoints[2] = motionEvent.getX();
                eventPoints[3] = motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("GraffitiView2", "onTouchEvent: ACTION_DOWN");
                        // 每次down下去重新new一个Draw
                        mPath = new Path();
                        LayerManager.getInstance().createDraw(mPath);
                        mPath.moveTo(eventPoints[2], eventPoints[3]);
                        eventPoints[0] = eventPoints[2];
                        eventPoints[1] = eventPoints[3];
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d("GraffitiView2", "onTouchEvent: ACTION_MOVE");
                        ShapeManager.getInstance().onDraw(mPath, eventPoints);
                        LayerManager.getInstance().drawPath();
                        if (graffitiViewGroup != null)
                            graffitiViewGroup.refreshView();
                        if (layerAdapter != null)
                            layerAdapter.notifyCurrentIndex();
                        break;
                    case MotionEvent.ACTION_UP:
                        LayerManager.getInstance().drawOver();
                        if (graffitiViewGroup != null)
                            graffitiViewGroup.refreshView();
                        if (layerAdapter != null)
                            layerAdapter.notifyCurrentIndex();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 添加一个图层
     */
    private void addLayer(Activity activity) {
        try {
            int resultIndex = LayerManager.getInstance().createLayer(ScreenUtils.getScreenWidth(activity), ScreenUtils.getScreenHeight(activity));
            refreshUi(resultIndex);
        } catch (Error | Exception e) {
            Toast.makeText(activity, "根据您当前手机性能，图层已达创建上限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除一个图层
     */
    private void deleteLayer(int index) {
        int resultIndex = LayerManager.getInstance().deleteLayer(index);
        refreshUi(resultIndex);
    }

    /**
     * 刷新UI
     */
    private void refreshUi(int currentIndex) {
        // 切换图层
        if (layerAdapter != null)
            layerAdapter.setCurrentIndex(currentIndex);
        // 刷新画布
        if (graffitiViewGroup != null) {
            graffitiViewGroup.setCurrentIndex(currentIndex);
            graffitiViewGroup.refreshAllView();
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

}
