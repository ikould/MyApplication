package com.aliu.myapplication.board.bean;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图层
 *
 * @author ikould on 2017/11/17.
 */

public class Layer {

    // 主键
    private int    id;
    // 素材id
    private String materialId;
    // 标题
    private String title;
    // 预览图
    private Bitmap bitmap;
    // 绘制集合 Draw 可以为 BitmapDraw/PathDraw
    private List<Draw> drawList = new ArrayList<>();

    public Layer() {
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<Draw> getDrawList() {
        return drawList;
    }

    public static class BitmapDraw extends Draw {
        private Bitmap bitmap;
        private List<float[]> positionInfoList = new ArrayList<>();

        public BitmapDraw() {
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public List<float[]> getPositionInfoList() {
            return positionInfoList;
        }

        public void setPositionInfoList(List<float[]> positionInfoList) {
            this.positionInfoList = positionInfoList;
        }

        @Override
        public String toString() {
            return "BitmapDraw{" +
                    "bitmap=" + bitmap +
                    ", positionInfoList=" + positionInfoList +
                    '}';
        }
    }

    public static class PathDraw extends Draw {
        // 当前的画笔
        private Paint paint;
        // 当前的Path
        private Path  path;

        public PathDraw() {
            super();
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
        }

        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }
    }

    public static abstract class Draw {

        private Point[] positionPoints;

        public Draw() {
            positionPoints = new Point[4];
            for (int i = 0; i < positionPoints.length; i++) {
                positionPoints[i] = new Point();
            }
        }

        public Point[] getPositionPoints() {
            return positionPoints;
        }

        public void setPositionPoints(Point[] positionPoints) {
            this.positionPoints = positionPoints;
        }
    }

    @Override
    public String toString() {
        return "Layer{" +
                "id=" + id +
                ", materialId='" + materialId + '\'' +
                ", title='" + title + '\'' +
                ", bitmap=" + bitmap +
                ", drawList=" + drawList +
                '}';
    }
}
