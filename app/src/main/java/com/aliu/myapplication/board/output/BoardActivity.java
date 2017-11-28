package com.aliu.myapplication.board.output;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;

import com.aliu.myapplication.R;
import com.aliu.myapplication.board.layer.GraffitiViewGroup;
import com.aliu.myapplication.board.material.MaterialManager;
import com.aliu.myapplication.board.output.assist.LayerAssist;
import com.aliu.myapplication.board.output.assist.SkinAssist;
import com.aliu.myapplication.board.paint.PaintManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.activity.OnPermissionResultListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * describe
 * Created by liudong on 2017/9/26.
 */

public class BoardActivity extends BaseActivity {

    @BindView(R.id.graffitiView)
    GraffitiViewGroup    graffitiViewGroup;
    @BindView(R.id.last_step)
    Button               lastStep;
    @BindView(R.id.reset)
    Button               reset;
    @BindView(R.id.paintSize)
    Button               paintSize;
    @BindView(R.id.save)
    Button               save;
    @BindView(R.id.seekbar)
    SeekBar              seekbar;
    @BindView(R.id.recover)
    Button               recover;
    @BindView(R.id.toggle_paint)
    Button               togglePaint;
    @BindView(R.id.ll_bottom)
    HorizontalScrollView llBottom;
    @BindView(R.id.paint_color)
    Button               paintColor;
    @BindView(R.id.rv_layer)
    RecyclerView         rvLayer;

    private boolean isShow;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        Log.d("BoardActivity", "onBaseCreate: ");
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new OnPermissionResultListener() {
            @Override
            public void permissionResult(boolean isSuccess) {
                if (isSuccess) {
                    initConfig();
                    initListener();
                }
            }
        });
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        MaterialManager.getInstance().setMaterialId("XXOO");
        LayerAssist.getInstance().bindView(this, graffitiViewGroup, rvLayer);
        SkinAssist.getInstance().initSkin(this, llBottom);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        seekbar.setMax(100);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    PaintManager.getInstance().setPaintSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int nowColorIndex = 0;
    private int nowShapeIndex = 0;
    private boolean isEraser;

    @OnClick({R.id.last_step, R.id.reset, R.id.paintSize, R.id.save, R.id.recover, R.id.toggle_paint, R.id.paint_color, R.id.shape_choose, R.id.path_transform})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.last_step:
                graffitiViewGroup.undo();
                break;
            case R.id.recover:
                graffitiViewGroup.recover();
                break;
            case R.id.reset:
                graffitiViewGroup.redo();
                break;
            case R.id.paintSize:
                seekbar.setVisibility(isShow ? View.GONE : View.VISIBLE);
                isShow = !isShow;
                break;
            case R.id.save:
                graffitiViewGroup.saveToSDCard();
                break;
            case R.id.toggle_paint:
                isEraser = !isEraser;
                PaintManager.getInstance().setIsEraser(isEraser);
                break;
            case R.id.paint_color:
                nowColorIndex = (++nowColorIndex) % 7;
                PaintManager.getInstance().setPaintColorIndex(nowColorIndex);
                break;
            case R.id.shape_choose:
                nowShapeIndex++;
                ShapeManager.getInstance().setShapeType(shapes[nowShapeIndex % shapes.length]);
                break;
            case R.id.path_transform:
                //graffitiViewGroup.setToTransform();
                break;
        }
    }

    private int[] shapes = new int[]{
            ShapeManager.SHAPE_DEFAULT, ShapeManager.SHAPE_RECT, ShapeManager.SHAPE_SQUARE,
            ShapeManager.SHAPE_FIVE_POINTED_STAR, ShapeManager.SHAPE_CIRCULAR, ShapeManager.SHAPE_ELLIPSE,
            ShapeManager.SHAPE_RADIAN, ShapeManager.SHAPE_ARROW, ShapeManager.SHAPE_CUSTOM
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LayerAssist.getInstance().clearAll();
    }
}
