package com.aliu.myapplication.board.bean;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

/**
 * 图层
 *
 * @author ikould on 2017/11/17.
 */

public class Layer {

    // 主键
    private int         id;
    // 素材id
    private String      materialId;
    // 标题
    private String      title;
    // 预览图
    private Bitmap      bitmap;
    // 绘制Path、Paint集合
    private List<Draw>  drawList;
    // 位移Matrix集合
    private List<Drift> driftList;

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

    public void setDrawList(List<Draw> drawList) {
        this.drawList = drawList;
    }

    public List<Drift> getDriftList() {
        return driftList;
    }

    public void setDriftList(List<Drift> driftList) {
        this.driftList = driftList;
    }

    public static class Draw {
        private Paint paint;
        private Path  path;

        public Draw() {
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

        @Override
        public String toString() {
            return "Draw{" +
                    "paint=" + paint +
                    ", path=" + path +
                    '}';
        }
    }

    public static class Drift {
        private Matrix matrix;

        public Drift() {
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public void setMatrix(Matrix matrix) {
            this.matrix = matrix;
        }

        @Override
        public String toString() {
            return "Drift{" +
                    "matrix=" + matrix +
                    '}';
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
                ", driftList=" + driftList +
                '}';
    }
}
