package com.aliu.myapplication.board.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.paint.PaintManager;

import java.util.List;

/**
 * 图层View
 */
public class LayerView extends View {

    private Layer      mLayer;
    private Layer.Draw currentPathDraw;
    private Bitmap     layerBitmap;
    private Canvas     canvasTemp;

    public LayerView(Context context) {
        super(context);
    }

    public LayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayer(Layer layer) {
        this.mLayer = layer;
    }

    public Layer getLayer() {
        return mLayer;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mLayer != null) {
            Bitmap bitmap = mLayer.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }
    }

    /**
     * 清除最后一个Draw
     */
    public Layer.Draw removeDraw() {
        Layer.Draw draw = null;
        if (mLayer != null) {
            List<Layer.Draw> drawList = mLayer.getDrawList();
            if (drawList != null && drawList.size() > 0) {
                draw = drawList.remove(drawList.size() - 1);
            }
        }
        return draw;
    }


    // ======== 引入图片 ========

    /**
     * 添加一个Bitmap
     */
    public void createDraw(Bitmap bitmap) {
        if (mLayer != null) {
            Layer.Draw pathDraw = new Layer.Draw();
            pathDraw.setBitmap(bitmap);
            RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            pathDraw.setRectF(rectF);
            Canvas canvas = new Canvas(mLayer.getBitmap());
            canvas.drawColor(Color.TRANSPARENT);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    // ======== Path操作 ========

    /**
     * 添加本次绘制效果
     * 保留之前的Bitmap，重新设置一个Bitmap，绘制时先将原先的Bitmap绘制上去，再将目前的Path也绘制上去
     */
    public void createDraw(Path path) {
        if (mLayer != null) {
            layerBitmap = mLayer.getBitmap();
            Bitmap drawBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            canvasTemp = new Canvas(drawBitmap);
            canvasTemp.drawColor(Color.TRANSPARENT);
            mLayer.setBitmap(drawBitmap);
            currentPathDraw = new Layer.Draw();
            Paint paint = PaintManager.getInstance().getPaint();
            currentPathDraw.setPaint(paint);
            currentPathDraw.setPath(path);
        }
    }

    /**
     * 绘制Path
     */
    public void drawPath() {
        if (mLayer != null && currentPathDraw != null) {
            canvasTemp.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvasTemp.drawBitmap(layerBitmap, 0, 0, null);
            canvasTemp.drawPath(currentPathDraw.getPath(), currentPathDraw.getPaint());
            invalidate();
        }
    }

    /**
     * 绘制Path
     */
    public void drawOver() {
        // 检测当前Path占据的RectF信息
        RectF rectF = new RectF(0, 0, 0, 0);
        currentPathDraw.setRectF(rectF);
        if (layerBitmap != null && !layerBitmap.isRecycled()) {
            layerBitmap.recycle();
        }
    }

    // ======== 矩阵操作 ========
    // 当前Path下方的所有PathDraw的集合
    private Bitmap belowBitmap;
    // 当前Path上方的所有Bitmap
    private Bitmap overBitmap;
    // 当前的选择的Bitmap
    private Bitmap currentBitmap;

    /**
     * 选择的PathDraw
     */
    public void choosePathDraw(Layer.Draw pathDraw) {
        currentPathDraw = pathDraw;
        List<Layer.Draw> pathDrawList = mLayer.getDrawList();
        int index = pathDrawList.indexOf(pathDraw);
        layerBitmap = mLayer.getBitmap();
        if (index > 0) { // 底部的Bitmap
            belowBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(belowBitmap);
            canvas.drawColor(Color.TRANSPARENT);
            for (int i = 0; i < index; i++) {
                Layer.Draw pathDraw1 = pathDrawList.get(i);
                canvas.drawPath(pathDraw1.getPath(), pathDraw1.getPaint());
            }
        }
        if (index < pathDrawList.size() - 1) { // 顶部的Bitmap
            overBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overBitmap);
            canvas.drawColor(Color.TRANSPARENT);
            for (int i = index + 1; i < pathDrawList.size(); i++) {
                Layer.Draw pathDraw1 = pathDrawList.get(i);
                canvas.drawPath(pathDraw1.getPath(), pathDraw1.getPaint());
            }
        }
        // 当前的Bitmap
        currentBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(currentBitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(pathDraw.getPath(), pathDraw.getPaint());
        // 当前的Canvas
        Bitmap drawBitmap = Bitmap.createBitmap(layerBitmap.getWidth(), layerBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvasTemp = new Canvas(drawBitmap);
        canvasTemp.drawColor(Color.TRANSPARENT);
        mLayer.setBitmap(drawBitmap);
    }

//    private void add  TODO

    /**
     * 绘制Path
     */
    public void drawMatrixPath() {
        if (mLayer != null && currentPathDraw != null) {
            canvasTemp.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            Matrix matrix = currentPathDraw.getMatrix();
            if (matrix == null) {
                return;
            }
            canvasTemp.drawBitmap(belowBitmap, 0, 0, null);
            canvasTemp.drawBitmap(currentBitmap, matrix, null);
            canvasTemp.drawBitmap(overBitmap, 0, 0, null);
            invalidate();
        }
    }

    /**
     * 绘制Path
     */
    public void drawMatrixOver() {
        if (layerBitmap != null && !layerBitmap.isRecycled()) {
            layerBitmap.recycle();
        }
        if (belowBitmap != null && !belowBitmap.isRecycled()) {
            belowBitmap.recycle();
        }
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            currentBitmap.recycle();
        }
        if (overBitmap != null && !overBitmap.isRecycled()) {
            overBitmap.recycle();
        }
    }
}