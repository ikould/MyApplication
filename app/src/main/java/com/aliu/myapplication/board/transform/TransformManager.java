package com.aliu.myapplication.board.transform;

import android.graphics.Canvas;

import com.aliu.myapplication.board.view.GraffitiView;

import java.util.List;

/**
 * describe
 * Created by liudong on 2017/10/5.
 */

public class TransformManager {

    // ====== 单例 ======
    private static TransformManager instance;

    public static TransformManager getInstance() {
        if (instance == null)
            synchronized (TransformManager.class) {
                if (instance == null)
                    instance = new TransformManager();
            }
        return instance;
    }

    private TransformManager() {
    }

    // ====== 操作 ======
    private boolean isDoTransform;

    public boolean getIsDoTransform() {
        return isDoTransform;
    }

    public void setDoTransform(boolean isDoTransform) {
        this.isDoTransform = isDoTransform;
    }

    public void onDraw(List<GraffitiView.DrawPath> drawPathList, Canvas canvas) {
       /* Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }*/
    }
}
