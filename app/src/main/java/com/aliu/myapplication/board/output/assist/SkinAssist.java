package com.aliu.myapplication.board.output.assist;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.aliu.myapplication.PluginUtils;
import com.aliu.myapplication.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * 皮肤协助类
 *
 * @author ikould on 2017/11/28.
 */

public class SkinAssist {

    // ====== 单例 ======

    private static SkinAssist instance;

    public static SkinAssist getInstance() {
        if (instance == null) {
            synchronized (SkinAssist.class) {
                if (instance == null) {
                    instance = new SkinAssist();
                }
            }
        }
        return instance;
    }

    private SkinAssist() {
    }

    // ====== 操作 ======

    /**
     * 初始化皮肤
     */
    public void initSkin(Activity activity, HorizontalScrollView llBottom) {
        String skinName = "skin1.apk";
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + skinName;
        System.out.println("skinPath:" + skinPath);
        String skinPackageName = "com.ikould.skin1";
        File file = new File(skinPath);
        if (file.exists()) {
            Toast.makeText(activity, "skin exists", Toast.LENGTH_SHORT).show();
        } else {
            // 网络下载皮肤逻辑
            try {
                InputStream inputStream = activity.getAssets().open(
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
                Toast.makeText(activity, "skin download finish",
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
                    assetManager, activity.getResources().getDisplayMetrics(),
                    activity.getResources().getConfiguration());
            // 类加载器
            DexClassLoader dexClassLoader = new DexClassLoader(
                    file.getAbsolutePath(), activity.getDir(skinName, Context.MODE_PRIVATE).getAbsolutePath(), null,
                    activity.getClassLoader());
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
}
