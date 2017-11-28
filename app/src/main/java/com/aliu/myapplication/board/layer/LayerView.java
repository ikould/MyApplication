package com.aliu.myapplication.board.layer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.ikould.frame.config.BaseAppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 图层View
 */
public class LayerView extends View {

    private Layer layer;

    public LayerView(Context context) {
        super(context);
        init();
    }

    public LayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (layer != null) {
            Matrix matrix = null;
            List<Layer.Drift> driftList = layer.getDriftList();
            if (driftList != null && driftList.size() > 0) {
                matrix = driftList.get(driftList.size() - 1).getMatrix();
            }
            Bitmap bitmap = layer.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                if (matrix != null) {
                    canvas.drawBitmap(bitmap, matrix, null);
                } else {
                    canvas.drawBitmap(bitmap, 0, 0, null);
                }
            }
        }
    }
}