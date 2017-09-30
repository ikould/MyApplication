package com.aliu.myapplication.board.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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

import com.ikould.frame.config.BaseAppConfig;
import com.ikould.frame.utils.ScreenUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * View实现涂鸦、撤销以及重做功能
 */
public class GraffitiView extends View {

    private Context context;
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;// 画布的画笔
    private Paint   mPaint;// 真实的画笔
    private float   downX, downY;// 临时点坐标
    private float distanceX, distanceY; // 临时相差的距离
    private static final float TOUCH_TOLERANCE = 4;
    // 保存Path路径的集合
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    // 记录Path路径的对象
    private        DrawPath       dp;
    private        int            screenWidth, screenHeight;
    private int currentColor = Color.RED;
    private int currentSize  = 5;
    private int currentStyle = 1;
    private int[] paintColor;//颜色集合

    private Path.Direction direction = Path.Direction.CW; // 顺时针

    private int arcAngle = 210;

    // 类型
    private int type = 8; // 0 默认，1 长方形 ，2 正方形 ， 3 五角星 ，4 圆形 ， 5 椭圆 ,6 弧度 ，7 箭头， 8 自定义图形
    private Bitmap bitmap;
    private int distance = 30; // 图片间隙

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

    private class DrawPath {
        public Path  path;// 路径
        public Paint paint;// 画笔
    }

    private void init(Context context) {
        this.context = context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        screenHeight = ScreenUtils.getScreenHeight(context);
        paintColor = new int[]{
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.GRAY, Color.CYAN
        };
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public void initCanvas() {
        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    //初始化画笔样式
    private void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setPathEffect(new DashPathEffect(new float[]{1, 2, 4, 8}, 1));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (currentStyle == 1) {
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        } else {//橡皮擦
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            if (bitmap != null){
                return;
            }
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 开始触摸
     */
    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        downX = x;
        downY = y;
    }

    /**
     * 移动到的位置
     */
    private void touchMove(float x, float y) {
        switch (type) {
            case 0: //  正常曲线
                float dx = Math.abs(x - downX);
                float dy = Math.abs(downY - y);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
                    mPath.quadTo(downX, downY, (x + downX) / 2, (y + downY) / 2);
                    downX = x;
                    downY = y;
                }
                break;
            case 1: // 长方形
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                mPath.reset();
                mPath.addRect(downX - distanceX, downY - distanceY, downX + distanceX, downY + distanceY, direction);
                break;
            case 2: // 正方形
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                float max = Math.max(distanceX, distanceY);
                mPath.reset();
                mPath.addRect(downX - max, downY - max, downX + max, downY + max, direction);
                break;
            case 3: // 五角星
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                float outR = Math.max(distanceX, distanceY);
                float inR = outR * sin(18) / sin(180 - 36 - 18);
                mPath.reset();
                mPath.moveTo(outR * cos(72 * 0 - 90) + downX, outR * sin(72 * 0 - 90) + downY);
                mPath.lineTo(inR * cos(72 * 0 + 36 - 90) + downX, inR * sin(72 * 0 + 36 - 90) + downY);
                mPath.lineTo(outR * cos(72 * 1 - 90) + downX, outR * sin(72 * 1 - 90) + downY);
                mPath.lineTo(inR * cos(72 * 1 + 36 - 90) + downX, inR * sin(72 * 1 + 36 - 90) + downY);
                mPath.lineTo(outR * cos(72 * 2 - 90) + downX, outR * sin(72 * 2 - 90) + downY);
                mPath.lineTo(inR * cos(72 * 2 + 36 - 90) + downX, inR * sin(72 * 2 + 36 - 90) + downY);
                mPath.lineTo(outR * cos(72 * 3 - 90) + downX, outR * sin(72 * 3 - 90) + downY);
                mPath.lineTo(inR * cos(72 * 3 + 36 - 90) + downX, inR * sin(72 * 3 + 36 - 90) + downY);
                mPath.lineTo(outR * cos(72 * 4 - 90) + downX, outR * sin(72 * 4 - 90) + downY);
                mPath.lineTo(inR * cos(72 * 4 + 36 - 90) + downX, inR * sin(72 * 4 + 36 - 90) + downY);
                mPath.lineTo(outR * cos(72 * 0 - 90) + downX, outR * sin(72 * 0 - 90) + downY);
                break;
            case 4: // 圆形
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                float r = Math.max(distanceX, distanceY);
                mPath.reset();
                mPath.addCircle(downX, downY, r, direction);
                break;
            case 5: // 椭圆
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                mPath.reset();
                mPath.addOval(new RectF(downX - distanceX, downY - distanceY, downX + distanceX, downY + distanceY), direction);
                break;
            case 6: // 弧度
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                float max1 = Math.max(distanceX, distanceY);
                float angle = (float) (Math.atan2(y - downY, x - downX) / Math.PI * 180);
                Log.d("GraffitiView", "touchMove: angle = " + angle);
                float startAngle = angle - arcAngle / 2;
                float endAngle = angle + arcAngle / 2;
                Log.d("GraffitiView", "touchMove: startAngle = " + startAngle + " endAngle = " + endAngle);
                mPath.reset();
                mPath.addArc(new RectF(downX - max1, downY - max1, downX + max1, downY + max1), startAngle, arcAngle);
                break;
            case 7: // 箭头
                distanceX = Math.abs(downX - x);
                distanceY = Math.abs(downY - y);
                // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也可以)
                int dir = (int) (Math.atan2(y - downY, x - downX) / Math.PI * 180);
                mPath.reset();
                mPath.moveTo(downX, downY);
                mPath.lineTo(x, y);
                float length = (float) Math.sqrt(Math.pow(x - downX, 2) + Math.pow(y - downY, 2));
                addArrow(mPath, dir, x, y, length);
                break;
        }
    }

    /**
     * 添加箭头
     */
    private void addArrow(Path path, int angle, float lastX, float lastY, float length) {
        Log.d("GraffitiView", "addArrow: angle = " + angle);
        if (length < 100)
            return;
        float lengthArrow = length * 0.25f;
        int slideAngle = 45;
        int arrowAngle = 30;
        float slide = (float) (Math.tan(arrowAngle * 1.0f / 180 * Math.PI) * lengthArrow);
        float slideLength = slide / cos(slideAngle);
        path.lineTo(cos(360 - (90 - angle + slideAngle)) * slideLength + lastX, sin(360 - (90 - angle + slideAngle)) * slideLength + lastY);
        path.lineTo(cos(angle) * lengthArrow + lastX, sin(angle) * lengthArrow + lastY);
        path.lineTo(cos(90 + slideAngle + angle) * slideLength + lastX, sin(90 + slideAngle + angle) * slideLength + lastY);
        path.lineTo(lastX, lastY);
    }

    float cos(int num) {
        return (float) Math.cos(num * Math.PI / 180);
    }

    float sin(int num) {
        return (float) Math.sin(num * Math.PI / 180);
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

    private void redrawOnBitmap() {
        /*mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
                Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);// 重新设置画布，相当于清空画布*/
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
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
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

    //以下为样式修改内容
    //设置画笔样式  
    public void togglePaintStyle() {
        if (currentStyle == 1) {
            currentStyle = 2;
        } else {
            currentStyle = 1;
        }
        setPaintStyle();   //当选择的是橡皮擦时，设置颜色为白色
    }

    //选择画笔大小
    public void selectPaintSize(int which) {
        //int size = Integer.parseInt(this.getResources().getStringArray(R.array.paintsize)[which]);  
        currentSize = which;
        setPaintStyle();
    }

    //设置画笔颜色
    public void selectPaintColor(int which) {
        currentColor = paintColor[which];
        setPaintStyle();
    }

    /**
     * 设置类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 设置Bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}  