package com.prismk.japaneseelearn.utils;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/13
 * 描    述：SharePreferences工具类
 * 修订历史：NULL
 * ================================================
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
    public static SharedPreferences mPreference;

    public static SharedPreferences getPreference(Context context) {
        if (mPreference == null)
            mPreference = PreferenceManager
                    .getDefaultSharedPreferences(context);
        return mPreference;
    }

    public static void setInteger(Context context, String name, int value) {
        getPreference(context).edit().putInt(name, value).commit();
    }

    public static int getInteger(Context context, String name, int default_i) {
        return getPreference(context).getInt(name, default_i);
    }

    /**
     * 设置字符串类型的配置
     */
    public static void setString(Context context, String name, String value) {
        getPreference(context).edit().putString(name, value).commit();
    }

    public static String getString(Context context, String name) {
        return getPreference(context).getString(name, null);
    }

    /**
     * 获取字符串类型的配置
     */
    public static String getString(Context context, String name, String defalt) {
        return getPreference(context).getString(name, defalt);
    }

    /**
     * 获取boolean类型的配置
     *
     * @param context
     * @param name
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String name,
                                     boolean defaultValue) {
        return getPreference(context).getBoolean(name, defaultValue);
    }

    /**
     * 设置boolean类型的配置
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setBoolean(Context context, String name, boolean value) {
        getPreference(context).edit().putBoolean(name, value).commit();
    }

    public static void setFloat(Context context, String name, Float value) {
        getPreference(context).edit().putFloat(name, value).commit();
    }

    public static Float getFloat(Context context, String name, Float value) {
        return getPreference(context).getFloat(name, 0);
    }

    public static void setLong(Context context, String name, Long value) {
        getPreference(context).edit().putLong(name, value).commit();
    }

    public static Long getLong(Context context, String name, Long defaultValue) {
        return getPreference(context).getLong(name, defaultValue);
    }

    /**
     * 移除指定的存储字符串
     */
    public static void clearSharePreferences(Context context, String name) {
        getPreference(context).edit().remove(name).commit();
    }

}
