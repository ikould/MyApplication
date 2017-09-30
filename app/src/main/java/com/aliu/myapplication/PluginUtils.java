package com.aliu.myapplication;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;

/**
 * describe
 * Created by liudong on 2017/9/27.
 */

public class PluginUtils {

    /**
     * 获取插件Apk的AssetManager对象
     *
     * @param apk
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static AssetManager getPluginAssetManager(File apk) throws Exception {
        // 字节码文件对象
        Class c = AssetManager.class;
        AssetManager assetManager = (AssetManager) c.newInstance();
        // 获取addAssetPath方法对象
        Method method = c.getDeclaredMethod("addAssetPath", String.class);
        method.invoke(assetManager, apk.getAbsolutePath());
        return assetManager;
    }

    /**
     * 获取插件Apk的Resources对象
     *
     * @param assets
     * @param metrics 系统尺寸和分辨率描述对象
     * @param config  配置
     * @return
     */
    public static Resources getPluginResources(AssetManager assets,
                                               DisplayMetrics metrics, Configuration config) {
        Resources resources = new Resources(assets, metrics, config);
        return resources;
    }

//  public Resources(AssetManager assets, DisplayMetrics metrics,Configuration config) {
//      this(assets, metrics, config,CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO, null);
//  }

}
