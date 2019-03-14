package com.prismk.japaneseelearn.managers.floatsmallvideo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.prismk.japaneseelearn.services.ELearnApplication;


public class FloatVideoController {

    private static FloatVideoController mInstance = new FloatVideoController();
    private FloatVideoService mFloatVideoService;
    private ServiceConnection mConn;

    private FloatVideoController() {
    }

    public static FloatVideoController getInstance() {
        return mInstance;
    }

    public void requestPermission(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("检测到您没有开启悬浮窗权限，点击去开启")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sp = ELearnApplication.self().getSharedPreferences("overlay", Activity.MODE_PRIVATE);
                        sp.edit().putBoolean("isShow", false).commit();
                    }
                })
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.setData(Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(intent);
                    }
                })
                .show();
    }

    public void doOverlayPlay() {
        final ELearnApplication app = ELearnApplication.self();
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                FloatVideoService.FloatVideoBinder binder = (FloatVideoService.FloatVideoBinder) service;
                mFloatVideoService = binder.getService();
                app.startService(new Intent(app, FloatVideoService.class));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        app.bindService(new Intent(app, FloatVideoService.class), mConn,Context.BIND_AUTO_CREATE);
    }

    public void start() {
        mFloatVideoService.start();
    }

    public void remove(){
        mFloatVideoService.remove();
        if (mConn!=null){
            ELearnApplication.self().unbindService(mConn);
        }
    }
}
