package com.aliu.myapplication.board;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.aliu.myapplication.PluginUtils;
import com.aliu.myapplication.R;
import com.aliu.myapplication.board.paint.PaintManager;
import com.aliu.myapplication.board.shape.ShapeManager;
import com.ikould.frame.activity.BaseActivity;
import com.ikould.frame.activity.OnPermissionResultListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dalvik.system.DexClassLoader;

import com.aliu.myapplication.board.view.GraffitiView;

import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_ARROW;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_CIRCULAR;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_CUSTOM;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_DEFAULT;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_ELLIPSE;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_FIVE_POINTED_STAR;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_RADIAN;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_RECT;
import static com.aliu.myapplication.board.shape.ShapeManager.SHAPE_SQUARE;

/**
 * describe
 * Created by liudong on 2017/9/26.
 */

public class BoardActivity extends BaseActivity {

    @BindView(R.id.graffitiView)
    GraffitiView         graffitiView;
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

    private boolean isVisiable;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
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
                graffitiView.setToTransform();
                break;
        }
    }

    private int[] shapes = new int[]{
            ShapeManager.SHAPE_DEFAULT, ShapeManager.SHAPE_RECT, ShapeManager.SHAPE_SQUARE,
            ShapeManager.SHAPE_FIVE_POINTED_STAR, ShapeManager.SHAPE_CIRCULAR, ShapeManager.SHAPE_ELLIPSE,
            ShapeManager.SHAPE_RADIAN, ShapeManager.SHAPE_ARROW, ShapeManager.SHAPE_CUSTOM
    };
}
