package com.aliu.myapplication.board.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aliu.myapplication.board.paint.PaintManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.ikould.frame.config.BaseAppConfig;
import com.ikould.frame.utils.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.bitmap;
import static android.R.attr.type;
import static android.R.attr.x;
import static android.R.attr.y;
import static android.os.Build.VERSION_CODES.M;

/**
 * View实现涂鸦、撤销以及重做功能
 */
public class GraffitiView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    private Paint mPaint;// 真实的画笔
    private Matrix mMatrix;// 用于各path内部的矩阵变换
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path路径的对象
    private DrawPath dp;
    private int screenWidth, screenHeight;

    private Path.Direction direction = Path.Direction.CW; // 顺时针

    // 触摸事件触发点，downX，downY，currentX，currentY
    private float[] eventPoints = new float[4];

    // 是否使用Path变换
    private boolean isUsePathTransform;

    public GraffitiView(Context context) {
        super(context);
        init(context);
    }

    public GraffitiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GraffitiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
        public Matrix matrix;
    }

    private void init(Context context) {
        screenWidth = ScreenUtils.getScreenWidth(context);
        screenHeight = ScreenUtils.getScreenHeight(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public void initCanvas() {
        mPaint = PaintManager.getInstance().getPaint();
        mMatrix = new Matrix();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        Log.d("GraffitiView", "onDraw: mPath = " + mPath);
        if (mPath != null && !isUsePathTransform) {
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 开始触摸
     */
    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        eventPoints[0] = x;
        eventPoints[1] = y;
    }

    /**
     * 触摸完成
     */
    private void touchUp() {
        mCanvas.drawPath(mPath, mPaint);
        //将一条完整的路径保存下来
        savePath.add(dp);
        mPath = null;// 重新置空
    }

    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            redrawOnBitmap();
        }
    }

    /**
     * 重做
     */
    public void redo() {
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }
    }

    /**
     * 重新绘制Path上的路径
     */
    private void redrawOnBitmap() {
        initCanvas();
        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }
        invalidate();// 刷新
    }

    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
     */
    public void recover() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        eventPoints[2] = event.getX();
        eventPoints[3] = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                mPaint = PaintManager.getInstance().getPaint();
                mMatrix = new Matrix();
                dp.path = mPath;
                dp.paint = mPaint;
                dp.matrix = mMatrix;
                touchStart(eventPoints[2], eventPoints[3]);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!ShapeManager.getInstance().onDraw(mPath, eventPoints)) {
                    float dx = Math.abs(eventPoints[2] - eventPoints[0]);
                    float dy = Math.abs(eventPoints[3] - eventPoints[1]);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
                        mPath.quadTo(eventPoints[0], eventPoints[1], (eventPoints[2] + eventPoints[0]) / 2, (eventPoints[3] + eventPoints[1]) / 2);
                        eventPoints[0] = eventPoints[2];
                        eventPoints[1] = eventPoints[3];
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
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
        mBitmap.compress(CompressFormat.PNG, 100, fos);
        //发送Sd卡的就绪广播,要不然在手机图库中不存在  
       /* Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
        intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
        context.sendBroadcast(intent);*/
        Log.e("TAG", "图片已保存");
    }
}