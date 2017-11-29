package com.aliu.myapplication.board.bean;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
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
    // 绘制Path、Paint集合
    private List<Draw>   drawList = new ArrayList<>();
    // 引入的图片集合
    private List<Bitmap> imgList  = new ArrayList<>();

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

    public List<Bitmap> getImgList() {
        return imgList;
    }

    public void setImgList(List<Bitmap> imgList) {
        this.imgList = imgList;
    }

    public static class Draw {
        // 当前的画笔
        private Paint  paint;
        // 当前的Path
        private Path   path;
        // 当前的Bitmap
        private Bitmap bitmap;
        // 范围
        private RectF  rectF;
        // 当前的矩阵
        private Matrix matrix;

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

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public RectF getRectF() {
            return rectF;
        }

        public void setRectF(RectF rectF) {
            this.rectF = rectF;
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public void setMatrix(Matrix matrix) {
            this.matrix = matrix;
        }

        @Override
        public String toString() {
            return "Draw{" +
                    "paint=" + paint +
                    ", path=" + path +
                    ", bitmap=" + bitmap +
                    ", rectF=" + rectF +
                    ", matrix=" + matrix +
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
                ", imgList=" + imgList +
                '}';
    }
}
