package com.prismk.japaneseelearn.managers.floatsmallvideo;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.Surface;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.VideoPlayerActivity;
import com.prismk.japaneseelearn.services.ELearnApplication;
import com.prismk.japaneseelearn.widgets.floatsmallvideo.DragFloatVideoView;

import static android.app.Notification.FLAG_FOREGROUND_SERVICE;
import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;


public class FloatVideoService extends Service {

    private DragFloatVideoView mDragFloatVideoView;
    private PlayerManager mPlayerManager;
    private Application mApplication;
    public boolean mIsFloatVideoActive;
    private NotificationManager manager;

    public static final String PRIMARY_CHANNEL = "default";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mPlayerManager = PlayerManager.getInstance();
        mApplication = ELearnApplication.self();

        return new FloatVideoBinder();
    }

    private static final String CHANNEL_ID = "float_video_service";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        openFloatView();
        start();

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, PRIMARY_CHANNEL, NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            getManager().createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, VideoPlayerActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, FLAG_CANCEL_CURRENT))
                .setContentTitle("小窗口播放中")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentText("点击进入全屏播放")
                .setAutoCancel(false)
                .setShowWhen(false)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = builder.build();
        notification.flags = FLAG_FOREGROUND_SERVICE;
        startForeground(110, notification);
        return START_NOT_STICKY;

    }

    public void openFloatView() {
        //初始化视图组件
        mDragFloatVideoView = new DragFloatVideoView(ELearnApplication.self());
        mDragFloatVideoView.setOnCLickListener(new DragFloatVideoView.OnRootClickListener() {
            @Override
            public void onClicked() {
                mApplication.startActivity(new Intent(mApplication, VideoPlayerActivity.class));
            }
        });
        mDragFloatVideoView.setOnClosedListener(new DragFloatVideoView.OnClosedListener() {
            @Override
            public void onClosed() {
                stopPlay();
            }
        });

        mDragFloatVideoView.addToWindowManager();
    }

    public void start() {
        mDragFloatVideoView.setOnSurfaceViewCreatedListener(new DragFloatVideoView.OnSurfaceViewCreatedListener() {
            @Override
            public void onCreated(Surface surface) {
                mPlayerManager.setPlayerSurface(surface);
            }
        });
    }

    public void remove() {
        mDragFloatVideoView.removeSelf();
        stopForeground(true);
        mIsFloatVideoActive = false;
    }

    public void stopPlay() {
        mPlayerManager.stop();
        mDragFloatVideoView.removeSelf();
        stopForeground(true);
        mIsFloatVideoActive = false;
    }

    public class FloatVideoBinder extends Binder {
        FloatVideoService getService() {
            return FloatVideoService.this;
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
