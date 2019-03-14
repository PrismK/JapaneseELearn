package com.prismk.japaneseelearn.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.github.anzewei.parallaxbacklayout.ParallaxHelper;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/10
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class ELearnApplication extends Application {

    private static Context mELearnAppContext = null;

    public static Context getELearnAppContext() {
        return mELearnAppContext;
    }

    public static ELearnApplication self() {
        return (ELearnApplication) mELearnAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mELearnAppContext = this;
        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
        startService(new Intent(ELearnApplication.this,DBservier.class));
    }
}
