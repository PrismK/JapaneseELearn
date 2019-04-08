package com.prismk.japaneseelearn.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.prismk.japaneseelearn.listener.IMusic;
import com.prismk.japaneseelearn.listener.OnDataArrivedListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private final IBinder myBinder = new MyBinder();

    public static final int PLAY = 1;
    public static final int STOP = 2;
    public static final int PAUSE = 3;

    private int playState = STOP;
    private MediaPlayer mediaPlayer;

    private OnDataArrivedListener mOnDataArrivedListener;

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    public class MyBinder extends Binder implements IMusic {

        public MusicService getService() {
            return MusicService.this;
        }

        @Override
        public void play() {
            MusicService.this.play();
        }

        @Override
        public void pause() {
            MusicService.this.pause();
        }

        @Override
        public void replay() {
            MusicService.this.replay();
        }

        @Override
        public void stop() {
            MusicService.this.stop();
        }

        @Override
        public int getPlayState() {
            return MusicService.this.getPlayState();
        }

    }

    public void play() {
        if (this.playState != PAUSE)
            initMediaPlayer();
        setPlayState(PLAY);
        MusicService.this.mediaPlayer.start();
        updateMusicProgress();
    }

    public void pause() {
        setPlayState(PAUSE);
        this.mediaPlayer.pause();
    }

    public void replay() {
        if (this.playState == STOP)
            return;
        setPlayState(PLAY);
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();
        initMediaPlayer();
        this.mediaPlayer.start();
    }

    public void stop() {
        setPlayState(STOP);
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();
    }

    public int getPlayState() {
        return this.playState;
    }

    private void setPlayState(int playStates) {
        this.playState = playStates;
    }

    @SuppressLint("SdCardPath")
    private void initMediaPlayer() {
        this.mediaPlayer = new MediaPlayer();
        //初始化mediaplayer，播放之前初始化防止错乱
        this.mediaPlayer.reset();
        try {
            this.mediaPlayer.setDataSource("/mnt/sdcard/Music/crodia.mp3");
            this.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMusicProgress() {
        //总音乐时长（毫秒）
        int duration = this.mediaPlayer.getDuration();
        System.out.println("-------"+duration);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = MusicService.this.mediaPlayer.getCurrentPosition();
                if (mOnDataArrivedListener != null) {
                    mOnDataArrivedListener.onDataArrived(currentPosition);
                    System.out.println("-----------current"+currentPosition);
                }
            }
        };
        timer.schedule(timerTask, 100, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.playState != STOP)
            this.mediaPlayer.stop();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (this.playState != STOP)
            this.mediaPlayer.stop();
        return super.onUnbind(intent);
    }

    /**
     * 设置监听
     */
    public void setOnDataArrivedListener(OnDataArrivedListener onDataArrivedListener) {
        this.mOnDataArrivedListener = onDataArrivedListener;
    }
}