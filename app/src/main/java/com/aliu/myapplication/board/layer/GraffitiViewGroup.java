package com.aliu.myapplication.board.layer;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.aliu.myapplication.board.transform.TransformManager;
import com.ikould.frame.config.BaseAppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * View实现涂鸦、撤销以及重做功能
 */
public class GraffitiViewGroup extends FrameLayout {

    // 数据
    private List<Layer>     layerList;
    // 数据对应的View
    private List<LayerView> layerViewList;
    private LayerView       currentLayerView;
    // 触摸事件触发点，downX，downY，currentX，currentY
    private float[] eventPoints = new float[4];
    private Path    mPath;
    // 是否是矩阵操作
    private boolean isDoTransform;

    public GraffitiViewGroup(Context context) {
        super(context);
        init();
    }

    public GraffitiViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraffitiViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
    }

    /**
     * 设置图层集合
     */
    public void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
        if (layerList != null && layerList.size() > 0) {
            if (layerViewList == null)
                layerViewList = new ArrayList<>();
            for (Layer layer : layerList) {
                LayerView layerView = createLayerView(getContext(), layer);
                addView(layerView);
                layerViewList.add(layerView);
            }
        } else {
            removeAllViews();
            layerViewList = null;
        }
    }

    /**
     * 设置当前的Layer
     */
    public void setCurrentLayer(Layer layer) {
        int position = layerList.indexOf(layer);
        Log.d("GraffitiViewGroup", "setCurrentLayer: position = " + position);
        if (position >= 0 && position < getChildCount()) {
            this.currentLayerView = (LayerView) getChildAt(position);
        }
    }

    /**
     * 实时刷新所有的View
     */
    public void refreshAllView() {
        // 调整View层级关系
        removeAllViews();
        if (layerList != null && layerViewList != null && layerViewList.size() > 0 && layerList.size() > 0) {
            // 重新排序
            List<LayerView> layerViewListTemp = new ArrayList<>();
            for (Layer layer : layerList) {
                LayerView layerView = null;
                for (LayerView view : layerViewList) {
                    if (layer == view.getLayer()) {
                        layerView = view;
                        break;
                    }
                }
                if (layerView == null)
                    layerView = createLayerView(getContext(), layer);
                layerViewListTemp.add(layerView);
            }
            layerViewList = layerViewListTemp;
            // 重新添加到ViewGroup中
            for (LayerView layerView : layerViewList) {
                addView(layerView);
            }
        }
    }

    /**
     * 创建图层View
     */
    private LayerView createLayerView(Context context, Layer layer) {
        LayerView layerView = new LayerView(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layerView.setLayer(layer);
        layerView.setLayoutParams(layoutParams);
        return layerView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        eventPoints[2] = event.getX();
        eventPoints[3] = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("GraffitiView2", "onTouchEvent: ACTION_DOWN");
                isDoTransform = TransformManager.getInstance().getIsDoTransform();
                if (isDoTransform) { // 矩阵操作
                    addTransDown();
                } else {
                    addDrawDown();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("GraffitiView2", "onTouchEvent: ACTION_MOVE");
                ShapeManager.getInstance().onDraw(mPath, eventPoints);
                currentLayerView.drawPath();
                LayerManager.getInstance().renderPathDraw();
                break;
            case MotionEvent.ACTION_UP:
                currentLayerView.drawOver();
                LayerManager.getInstance().renderOverPathDraw();
                break;
        }
        return true;
    }

    private void addTransDown() {
        // 确定是哪一个PathDraw

    }

    private void addDrawDown() {
        // 每次down下去重新new一个Draw
        mPath = new Path();
        LayerManager.getInstance().addPathDraw(mPath);
        currentLayerView.createDraw(mPath);
        mPath.moveTo(eventPoints[2], eventPoints[3]);
        eventPoints[0] = eventPoints[2];
        eventPoints[1] = eventPoints[3];
    }

    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
    }

    /**
     * 重做
     */
    public void redo() {
    }

    /**
     * 重新绘制Path上的路径
     */
    private void redrawOnBitmap() {
    }

    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
     */
    public void recover() {
    }

    //保存到sd卡
    public void saveToSDCard() {
        //获得系统当前时间，并以该时间作为文件名  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
        String str = formatter.format(curDate) + "paint.png";
        File file = new File(BaseAppConfig.TEMP_DIR + File.separator + str);
        FileOutputStream fos = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("GraffitiView", "saveToSDCard: fos = " + fos);
        //mBitmap.compress(CompressFormat.PNG, 100, fos);
        //发送Sd卡的就绪广播,要不然在手机图库中不存在  
       /* Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        context.sendBroadcast(intent);*/
        Log.e("TAG", "图片已保存");
    }
}