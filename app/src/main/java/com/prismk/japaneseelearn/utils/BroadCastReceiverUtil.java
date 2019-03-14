package com.prismk.japaneseelearn.utils;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/16
 * 描    述：
 * 修订历史：
 * ================================================
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.io.Serializable;

/**
 * 注册广播，发送广播，注销广播
 */
public class BroadCastReceiverUtil {

    public interface OnReceiveBroadcast {
        public void onReceive(Context context, Intent intent);
    }

    private static class CustomBroadcastReceiver extends BroadcastReceiver {
        private OnReceiveBroadcast onReceiveBroadcast;

        public CustomBroadcastReceiver(OnReceiveBroadcast onReceiveBroadcast) {
            this.onReceiveBroadcast = onReceiveBroadcast;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (onReceiveBroadcast != null) {
                onReceiveBroadcast.onReceive(context, intent);
            }
        }

    }

    public static BroadcastReceiver registerBroadcastReceiver(Context context, String[] filters, OnReceiveBroadcast onReceiveBroadcast) {
        BroadcastReceiver broadcastReceiver = new CustomBroadcastReceiver(onReceiveBroadcast);
        IntentFilter filter = new IntentFilter();
        for (String filterStr : filters) {
            filter.addAction(filterStr);
        }

        context.registerReceiver(broadcastReceiver, filter);
        return broadcastReceiver;
    }

    public static void sendBroadcast(Context context, String filter) {
        if(context == null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter, Bundle bundle) {
        if(context == null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(filter);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter, String bundleName, Bundle bundle) {
        if(context == null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(filter);
        intent.putExtra(bundleName, bundle);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter,String name,  Serializable serializable) {
        Intent intent = new Intent();
        intent.putExtra(name, serializable);
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter,String name,  String value) {
        Intent intent = new Intent();
        intent.putExtra(name, value);
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter,String name,  long value) {
        Intent intent = new Intent();
        intent.putExtra(name, value);
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter,String name,  int value) {
        Intent intent = new Intent();
        intent.putExtra(name, value);
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String filter,String name,  String value, String name1, String value1) {
        Intent intent = new Intent();
        intent.putExtra(name, value);
        intent.putExtra(name1, value1);
        intent.setAction(filter);
        context.sendBroadcast(intent);
    }

    public static void unRegisterBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        if (context != null && broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }

}

