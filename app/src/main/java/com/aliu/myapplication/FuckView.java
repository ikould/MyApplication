package com.aliu.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALiu on 2017/7/14.
 */

public class FuckView extends FrameLayout {

    private Canvas                      canvas;
    private Paint                       paint;
    private Paint                       atinpaint;
    private View                        childView;
    private ImageView                   mIvleft;
    private ImageView                   mIvRight;
    private int                         defalutTop;
    private RelativeLayout.LayoutParams leftlayout;
    private RecyclerView                rv;

    private float frameWidth;
    private float frameHeight;

    private boolean atTopArea      = false;
    private boolean atTopAreaFirst = false;
    private boolean rvAtTop        = false;
    private boolean atButtomArea   = false;

    private List<String> mDatas;

    public FuckView(Context context) {
        this(context, null);
    }

    public FuckView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FuckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        setWillNotDraw(false);
        childView = View.inflate(context, R.layout.main2, null);
        mIvleft = (ImageView) childView.findViewById(R.id.iv_left);
        mIvRight = (ImageView) childView.findViewById(R.id.iv_right);
        rv = (RecyclerView) childView.findViewById(R.id.rv);
        leftlayout = (RelativeLayout.LayoutParams) mIvleft.getLayoutParams();
        defalutTop = leftlayout.topMargin;
        addView(childView);

        initData();
        init();
    }

    private void initData() {
        mDatas = new ArrayList();
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
        mDatas.add("");
    }


    private void init() {
        frameWidth = 1080;
        frameHeight = 1920;
        atinpaint = new Paint();
        atinpaint.setAntiAlias(true);

        paint = new Paint();
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));


        //获取banner
        banner = BitmapFactory.decodeResource(getResources(), R.drawable.banner1, mOptions);
        bannerGaosi = BitmapFactory.decodeResource(getResources(), R.drawable.banner1gaosi, mOptions);
        jianbian = BitmapFactory.decodeResource(getResources(), R.drawable.jianbian4, mOptions);
        initBannerMatrix();
        initBannerGaosiMatrix();
        initBannerGaoFansiMatrix();


        frontBmp = Bitmap.createBitmap(bannerGaosi.getWidth(), bannerGaosi.getHeight(), Bitmap.Config.ARGB_8888);

        canvas = new Canvas(frontBmp);

        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(new HomeAdapter());

        move(0);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        if (!atTopArea) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);

        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getActionMasked() == MotionEvent.ACTION_UP) {
            if (!atTopArea){
                atTopAreaFirst = false;
            }
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            lastY = ev.getY();
        }

        //判断rv是否能向上滑动
        rvAtTop = rv.canScrollVertically(-1);

        if (atTopArea && !rvAtTop) {
            float y = lastY - ev.getY();
            if (y < 0) {
                //下滑趋势
                return super.dispatchTouchEvent(ev);
            }else{
                //上滑趋势
                if (!atTopAreaFirst) {
                    atTopAreaFirst = true;
                    rv.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, rv.getLeft() + 5, rv.getTop() + 5, 0));
                }
                rv.onTouchEvent(ev);
                lastY = ev.getY();
                return true;
            }
        }else{

            if (atTopArea) {
                if (!atTopAreaFirst) {
                    atTopAreaFirst = true;
                    rv.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, rv.getLeft() + 5, rv.getTop() + 5, 0));
                }
                rv.onTouchEvent(ev);
                lastY = ev.getY();
                return true;
            } else {
                return super.dispatchTouchEvent(ev);

            }

        }

    }

    private Matrix bannerSrc;
    private Matrix bannerGaosiSrc;
    private Matrix bannerGaosiFanSrc;

    private Matrix matrix;
    private Matrix matrix2;
    private Bitmap banner;
    private Bitmap frontBmp;
    private Bitmap bannerGaosi;
    private Bitmap jianbian;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bannerGaosi, bannerGaosiFanSrc, atinpaint);
        canvas.drawBitmap(banner, bannerSrc, atinpaint);
        canvas.drawBitmap(frontBmp, bannerGaosiSrc, atinpaint);


//        canvas.drawBitmap(frontBmp, matrix, null);
    }


    private float lastY;
    private float topy = -145;


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:

                lastY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                move(y - lastY);
                lastY = y;
                break;


        }
        return true;
    }

    private long mark1;
    private long mark2;
    private long mark3;
    private long mark4;

    private void move(float y) {

        topy += y;
        if (topy >= -145) {
            topy = -145;
            atButtomArea = true;
        } else {
            atButtomArea = false;
        }


        if (topy <= -1030) {
            topy = -1030;
            atTopArea = true;
        } else {
            atTopArea = false;
        }

        leftlayout.topMargin = (int) (defalutTop - (-145 - topy));
        mIvleft.setLayoutParams(leftlayout);
        float v = (-topy - 145) * 1.0f / 600; //默认885
        v = v > 1 ? 1 : v;
        mIvleft.setAlpha(1 - v);
        mIvRight.setAlpha(1 - v);


        canvas.drawBitmap(bannerGaosi, 0, 0, null);
        canvas.drawBitmap(jianbian, 0, topy, paint);


        invalidate();


    }


    private void initBannerMatrix() {
        bannerSrc = new Matrix();
        float v = 1140 * 1.0f / banner.getWidth();
        bannerSrc.postScale(v, v);
        bannerSrc.postTranslate(-(1140 - 1080) / 2, -(1040 - 980) / 2);
    }

    private void initBannerGaoFansiMatrix() {
        bannerGaosiFanSrc = new Matrix();
        float v = 1140 * 1.0f / bannerGaosi.getWidth();
        bannerGaosiFanSrc.postScale(v, -v);
        bannerGaosiFanSrc.postTranslate(-(1140 - 1080) / 2, 1040 * 2 - (1040 - 980) / 2);
    }


    private void initBannerGaosiMatrix() {
        bannerGaosiSrc = new Matrix();
        float v = 1140 * 1.0f / bannerGaosi.getWidth();
        bannerGaosiSrc.postScale(v, v);
        bannerGaosiSrc.postTranslate(-(1140 - 1080) / 2, -(1040 - 980) / 2);
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView mIv;

            public MyViewHolder(View view) {
                super(view);
                mIv = (ImageView) view.findViewById(R.id.iv_item);
            }
        }
    }
}
