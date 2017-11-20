package com.aliu.myapplication.board;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aliu.myapplication.PluginUtils;
import com.aliu.myapplication.R;
import com.aliu.myapplication.board.adapter.LayerAdapter;
import com.aliu.myapplication.board.bean.Layer;
import com.aliu.myapplication.board.layer.GraffitiView2;
import com.aliu.myapplication.board.layer.LayerManager;
import com.aliu.myapplication.board.material.MaterialManager;
import com.aliu.myapplication.board.paint.PaintManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.activity.OnPermissionResultListener;
import com.ikould.frame.utils.ScreenUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dalvik.system.DexClassLoader;

/**
 * describe
 * Created by liudong on 2017/9/26.
 */

public class BoardActivity extends BaseActivity {

    @BindView(R.id.graffitiView)
    GraffitiView2        graffitiView;
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

    private boolean     isVisiable;
    private List<Layer> layerList;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        Log.d("BoardActivity", "onBaseCreate: ");
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        initConfig();
        initView();
        initListener();
        checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, new OnPermissionResultListener() {
            @Override
            public void permissionResult(boolean isSuccess) {

            }
        });

        String skinName = "skin1.apk";
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + skinName;
        System.out.println("skinPath:" + skinPath);
        String skinPackageName = "com.ikould.skin1";
        File file = new File(skinPath);
        if (file.exists()) {
            Toast.makeText(this, "skin exists", Toast.LENGTH_SHORT).show();
        } else {
            // 网络下载皮肤逻辑
            try {
                InputStream inputStream = this.getAssets().open(
                        "skin1.apk");
                BufferedInputStream bis = new BufferedInputStream(
                        inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(skinPath));
                int len;
                byte[] bs = new byte[1024];
                while ((len = bis.read(bs)) != -1) {
                    bos.write(bs, 0, len);
                }
                Toast.makeText(this, "skin download finish",
                        Toast.LENGTH_SHORT).show();
                bis.close();
                bos.close();

            } catch (IOException e) {
                Log.d("BoardActivity", "onBaseCreate: e = " + e);
            }
        }
        try {
            // 获取插件Apk的AssetManager对象
            AssetManager assetManager = PluginUtils
                    .getPluginAssetManager(file);
            // 获取插件Apk的Resources对象
            Resources resources = PluginUtils.getPluginResources(
                    assetManager, this.getResources().getDisplayMetrics(),
                    this.getResources().getConfiguration());
            // 类加载器
            DexClassLoader dexClassLoader = new DexClassLoader(
                    file.getAbsolutePath(), this.getDir(skinName, Context.MODE_PRIVATE).getAbsolutePath(), null,
                    this.getClassLoader());
            // 反射拿到R.drawable类的字节码文件对象
            Class<?> c = dexClassLoader.loadClass(skinPackageName
                    + ".R$drawable");
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("test_skin_1")) {
                    int imgId = field.getInt(R.drawable.class);
                    Drawable background = resources.getDrawable(imgId);
                    llBottom.setBackgroundDrawable(background);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        graffitiView.setLayerList(layerList);
        initRecyclerView();
    }

    private void initConfig() {
        MaterialManager.getInstance().setMaterialId("XXOO");
        LayerManager.getInstance().createLayer(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        layerList = LayerManager.getInstance().getLayerList();
    }

    private void initRecyclerView() {
        LayerAdapter layerAdapter = new LayerAdapter(this);
        layerAdapter.setFooterView(getAddTextView());
        rvLayer.setAdapter(layerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvLayer.setLayoutManager(linearLayoutManager);
        for (Layer layer : layerList) {
            Log.d("BoardActivity", "initRecyclerView: layer = " + layer);
        }
        layerAdapter.setLayerList(layerList);
    }

    private TextView getAddTextView() {
        TextView textView = new TextView(this);
        textView.setText("+");
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);
        return textView;
    }

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
                graffitiView.undo();
                break;
            case R.id.recover:
                graffitiView.recover();
                break;
            case R.id.reset:
                graffitiView.redo();
                break;
            case R.id.paintSize:
                seekbar.setVisibility(isVisiable ? View.GONE : View.VISIBLE);
                isVisiable = !isVisiable;
                break;
            case R.id.save:
                graffitiView.saveToSDCard();
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
                //graffitiView.setToTransform();
                break;
        }
    }

    private int[] shapes = new int[]{
            ShapeManager.SHAPE_DEFAULT, ShapeManager.SHAPE_RECT, ShapeManager.SHAPE_SQUARE,
            ShapeManager.SHAPE_FIVE_POINTED_STAR, ShapeManager.SHAPE_CIRCULAR, ShapeManager.SHAPE_ELLIPSE,
            ShapeManager.SHAPE_RADIAN, ShapeManager.SHAPE_ARROW, ShapeManager.SHAPE_CUSTOM
    };
}
