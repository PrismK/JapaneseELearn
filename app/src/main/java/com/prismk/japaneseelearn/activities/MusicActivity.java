package com.prismk.japaneseelearn.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.listener.IMusic;
import com.prismk.japaneseelearn.listener.OnDataArrivedListener;
import com.prismk.japaneseelearn.services.MusicService;

public class MusicActivity extends BaseActivity {

    private Intent intent;
    private MyConn myConn;
    private IMusic music;
    private Button btn_play_or_pause;
    private Button btn_replay;
    private Button btn_stop;

    private int playState;
    private SeekBar sb_seek_bar;

    private static final int MSG_FROM_SERVICE = 1;
    private static final int MAX_MSG_FROM_SERVICE = 2;

    private MusicService musicService;
    private IncomingHandler incomingHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        startMusicService();
        bindMusicService();
        initView();
        incomingHandler = new IncomingHandler();
    }

    /**
     * 接收信息
     */
    private final class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FROM_SERVICE:
                    int obj = (int)msg.obj;
                    sb_seek_bar.setProgress(obj);
                    break;
            }
        }
    }

    private void bindMusicService() {
        myConn = new MyConn();
        bindService(intent, myConn, Context.BIND_AUTO_CREATE);
    }

    private void startMusicService() {
        intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    private void initView() {
        btn_play_or_pause = (Button) findViewById(R.id.btn_play_or_pause);
        btn_replay = (Button) findViewById(R.id.btn_replay);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        sb_seek_bar = (SeekBar) findViewById(R.id.sb_seek_bar);
        sb_seek_bar.setMax(204643);
    }

    private class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //music = (IMusic) service;
            musicService = ((MusicService.MyBinder) service).getService();
            MusicActivity.this.playState = musicService.getPlayState();
            if (playState == MusicService.PLAY) {
                btn_play_or_pause.setText("暂停");
            } else {
                btn_play_or_pause.setText("播放");
            }

            musicService.setOnDataArrivedListener(new OnDataArrivedListener() {
                @Override
                public void onDataArrived(int data) {
                    incomingHandler.obtainMessage(MSG_FROM_SERVICE, data).sendToTarget();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            Toast.makeText(MusicActivity.this, "服务断开", Toast.LENGTH_SHORT).show();

        }
    }

    public void play(View view) {
        this.playState = musicService.getPlayState();
        switch (this.playState) {
            case MusicService.PLAY:
                musicService.pause();
                btn_play_or_pause.setText("播放");
                break;
            case MusicService.STOP:
            case MusicService.PAUSE:
                musicService.play();
                btn_play_or_pause.setText("暂停");
                break;
        }
    }

    public void replay(View view) {
        musicService.replay();
        btn_play_or_pause.setText("暂停");
    }

    public void stop(View view) {
        musicService.stop();
        btn_play_or_pause.setText("播放");
    }

    @Override
    protected void onDestroy() {
        unbindService(myConn);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music;
    }
}