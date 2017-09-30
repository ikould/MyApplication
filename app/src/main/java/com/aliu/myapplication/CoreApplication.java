package com.aliu.myapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 唯一的Application
 * <p>
 * Created by ikould on 2017/6/1.
 */

public class CoreApplication extends RePluginApplication {

    // 网络请求版本号(当前调用）
    public static final String NET_VERSION_CODE = "1.0.0";

    // App启动Application时间
    public long startAppTime;

    // 整个应用统一调用
    public Handler  handler        = new Handler();
    // 单一线程池（可用于数据库读写操作）
    public Executor singleExecutor = Executors.newSingleThreadExecutor();
    // 最多含有5个线程的线程池
    public Executor mostExecutor   = Executors.newFixedThreadPool(5);
    // 调用Application实体类
    private static CoreApplication instance;

    // 配置是否初始化
    private boolean isConfigInit;

    public static CoreApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePlugin.App.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startAppTime = System.currentTimeMillis();
        instance = this;
        RePlugin.App.onCreate();
        initConfig();
    }

    /**
     * 初始化配置，由WelcomeActivity获取权限后统一调用
     */
    public void initConfig() {
       // RePlugin.install("/sdcard/exam.apk");
        Log.d("CoreApplication", "initConfig: isConfigInit = " + isConfigInit);
       /* if (!isConfigInit && (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())) {
            Log.d("CoreApplication", "initConfig: ");
            isConfigInit = true;
            // 手机信息初始化
            PhoneTools.getPhoneInfo(this);
            // SharedPreference 初始化
            BaseAppConfig.getInstance().init(this);
            // 数据库初始化
            DbOpenHelper.init(this);
            // 崩溃拦截
            setCrashHandlerEnable(false);
            testInit();
        }*/
    }
}
