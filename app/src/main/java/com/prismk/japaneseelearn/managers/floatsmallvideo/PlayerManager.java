package com.prismk.japaneseelearn.managers.floatsmallvideo;

import android.app.Application;
import android.net.Uri;
import android.view.Surface;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.provider.RawDataSourceProvider;
import com.prismk.japaneseelearn.services.ELearnApplication;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * create by HiYang in 2018/09/12
 *
 * @description 小窗口视频媒体播放器管理类
 */
public class PlayerManager {
    //    private MediaPlayer mMediaPlayer;
    private IjkMediaPlayer mMediaPlayer;
    private static PlayerManager mPlayerManager;

    private PlayerManager() {
        mMediaPlayer = new IjkMediaPlayer();
    }

    public static PlayerManager getInstance() {
        if (mPlayerManager == null) {
            synchronized (PlayerManager.class) {
                mPlayerManager = new PlayerManager();
            }
        }
        return mPlayerManager;
    }

    public void setPlayerSurface(Surface surface) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    public void initPlayer(Surface surface) {
        Uri uri = Uri.parse("android.resource://" + ELearnApplication.self().getPackageName() + "/" + R.raw.bigbuckbunny);

        mMediaPlayer.reset();
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 1);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        mMediaPlayer.setSurface(surface);
        RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(ELearnApplication.self(), uri);
        mMediaPlayer.setDataSource(rawDataSourceProvider);
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

            }
        });
        mMediaPlayer.prepareAsync();
    }

    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }
}
