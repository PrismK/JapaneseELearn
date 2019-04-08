package com.prismk.japaneseelearn.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.bean.MusicData;
import com.prismk.japaneseelearn.services.MusicPlayerService;
import com.prismk.japaneseelearn.utils.radio.DisplayUtil;
import com.prismk.japaneseelearn.utils.radio.FastBlurUtil;
import com.prismk.japaneseelearn.widgets.music.BackgourndAnimationRelativeLayout;
import com.prismk.japaneseelearn.widgets.music.DiscView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.prismk.japaneseelearn.widgets.music.DiscView.DURATION_NEEDLE_ANIAMTOR;


public class MusicPlayerActivity extends BaseActivity implements DiscView.IPlayInfo, View.OnClickListener {

    private DiscView mDisc;
    private Toolbar mToolbar;
    private SeekBar mSeekBar;
    private ImageView mIvPlayOrPause, mIvNext, mIvLast;
    private TextView mTvMusicDuration, mTvTotalMusicDuration;
    private BackgourndAnimationRelativeLayout mRootLayout;
    public static final int MUSIC_MESSAGE = 0;

    public static final String PARAM_MUSIC_LIST = "PARAM_MUSIC_LIST";

    private Handler mMusicHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSeekBar.setProgress(mSeekBar.getProgress() + 1000);
            mTvMusicDuration.setText(duration2Time(mSeekBar.getProgress()));
            startUpdateSeekBarProgress();
        }
    };

    private MusicReceiver mMusicReceiver = new MusicReceiver();
    private List<MusicData> mMusicDatas = new ArrayList<>();
    private Intent serviceIntent;
    private ImageView mBack;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        position = getIntent().getIntExtra("num", 1);
        initMusicData();
        initView();
        initMusicReceiver();
        makeStatusBarTransparent();
    }

    private void initMusicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.ACTION_STATUS_MUSIC_PLAY);
        intentFilter.addAction(MusicPlayerService.ACTION_STATUS_MUSIC_PAUSE);
        intentFilter.addAction(MusicPlayerService.ACTION_STATUS_MUSIC_DURATION);
        intentFilter.addAction(MusicPlayerService.ACTION_STATUS_MUSIC_COMPLETE);
        /*注册本地广播*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter);
    }

    private void initView() {
        mDisc = (DiscView) findViewById(R.id.discview);
        mIvNext = (ImageView) findViewById(R.id.ivNext);
        mIvLast = (ImageView) findViewById(R.id.ivLast);
        mIvPlayOrPause = (ImageView) findViewById(R.id.ivPlayOrPause);
        mTvMusicDuration = (TextView) findViewById(R.id.tvCurrentTime);
        mTvTotalMusicDuration = (TextView) findViewById(R.id.tvTotalTime);
        mSeekBar = (SeekBar) findViewById(R.id.musicSeekBar);
        mRootLayout = (BackgourndAnimationRelativeLayout) findViewById(R.id.rootLayout);

        mToolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);

        mBack = findViewById(R.id.imv_back_to_hear);

        mDisc.setPlayInfoListener(this);
        mIvLast.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvPlayOrPause.setOnClickListener(this);
        mBack.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvMusicDuration.setText(duration2Time(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopUpdateSeekBarProgree();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
                startUpdateSeekBarProgress();
            }
        });

        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
        mDisc.setMusicDataList(mMusicDatas);
    }

    private void stopUpdateSeekBarProgree() {
        mMusicHandler.removeMessages(MUSIC_MESSAGE);
    }

    /*设置透明状态栏*/
    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initMusicData() {


        switch (position) {
            case 1:
                MusicData musicData1_1 = new MusicData(R.raw.music1, R.raw.ic_music1, "第一册-第一课-第一部分", "Administrator-1");
                MusicData musicData1_2 = new MusicData(R.raw.music2, R.raw.ic_music2, "第一册-第一课-第二部分", "Administrator-1");
                MusicData musicData1_3 = new MusicData(R.raw.music3, R.raw.ic_music3, "第一册-第一课-第三部分", "Administrator-1");
                MusicData musicData1_4 = new MusicData(R.raw.music2, R.raw.ic_music2, "第一册-第一课-第四部分", "Administrator-1");
                MusicData musicData1_5 = new MusicData(R.raw.music1, R.raw.ic_music1, "第一册-第一课-第五部分", "Administrator-1");
                mMusicDatas.add(musicData1_1);
                mMusicDatas.add(musicData1_2);
                mMusicDatas.add(musicData1_3);
                mMusicDatas.add(musicData1_4);
                mMusicDatas.add(musicData1_5);
                break;
            case 2:
                MusicData musicData2_1 = new MusicData(R.raw.music1, R.raw.ic_music1, "第二册-第一课-第一部分", "Administrator-2");
                MusicData musicData2_2 = new MusicData(R.raw.music2, R.raw.ic_music2, "第二册-第一课-第二部分", "Administrator-2");
                MusicData musicData2_3 = new MusicData(R.raw.music3, R.raw.ic_music3, "第二册-第一课-第三部分", "Administrator-2");
                MusicData musicData2_4 = new MusicData(R.raw.music2, R.raw.ic_music2, "第二册-第一课-第四部分", "Administrator-2");
                MusicData musicData2_5 = new MusicData(R.raw.music1, R.raw.ic_music1, "第二册-第一课-第五部分", "Administrator-2");
                mMusicDatas.add(musicData2_1);
                mMusicDatas.add(musicData2_2);
                mMusicDatas.add(musicData2_3);
                mMusicDatas.add(musicData2_4);
                mMusicDatas.add(musicData2_5);
                break;
            case 3:
                MusicData musicData3_1 = new MusicData(R.raw.music1, R.raw.ic_music1, "第三册-第一课-第一部分", "Administrator-3");
                MusicData musicData3_2 = new MusicData(R.raw.music2, R.raw.ic_music2, "第三册-第一课-第二部分", "Administrator-3");
                MusicData musicData3_3 = new MusicData(R.raw.music3, R.raw.ic_music3, "第三册-第一课-第三部分", "Administrator-3");
                MusicData musicData3_4 = new MusicData(R.raw.music2, R.raw.ic_music2, "第三册-第一课-第四部分", "Administrator-3");
                MusicData musicData3_5 = new MusicData(R.raw.music1, R.raw.ic_music1, "第三册-第一课-第五部分", "Administrator-3");
                mMusicDatas.add(musicData3_1);
                mMusicDatas.add(musicData3_2);
                mMusicDatas.add(musicData3_3);
                mMusicDatas.add(musicData3_4);
                mMusicDatas.add(musicData3_5);
                break;
            case 4:
                MusicData musicData4_1 = new MusicData(R.raw.music1, R.raw.ic_music1, "第四册-第一课-第一部分", "Administrator-4");
                MusicData musicData4_2 = new MusicData(R.raw.music2, R.raw.ic_music2, "第四册-第一课-第二部分", "Administrator-4");
                MusicData musicData4_3 = new MusicData(R.raw.music3, R.raw.ic_music3, "第四册-第一课-第三部分", "Administrator-4");
                MusicData musicData4_4 = new MusicData(R.raw.music2, R.raw.ic_music2, "第四册-第一课-第四部分", "Administrator-4");
                MusicData musicData4_5 = new MusicData(R.raw.music1, R.raw.ic_music1, "第四册-第一课-第五部分", "Administrator-4");
                mMusicDatas.add(musicData4_1);
                mMusicDatas.add(musicData4_2);
                mMusicDatas.add(musicData4_3);
                mMusicDatas.add(musicData4_4);
                mMusicDatas.add(musicData4_5);
                break;
            case 5:
                MusicData musicData5_1 = new MusicData(R.raw.music1, R.raw.ic_music1, "第五册-第一课-第一部分", "Administrator-5");
                MusicData musicData5_2 = new MusicData(R.raw.music2, R.raw.ic_music2, "第五册-第一课-第二部分", "Administrator-5");
                MusicData musicData5_3 = new MusicData(R.raw.music3, R.raw.ic_music3, "第五册-第一课-第三部分", "Administrator-5");
                MusicData musicData5_4 = new MusicData(R.raw.music2, R.raw.ic_music2, "第五册-第一课-第四部分", "Administrator-5");
                MusicData musicData5_5 = new MusicData(R.raw.music1, R.raw.ic_music1, "第五册-第一课-第五部分", "Administrator-5");
                mMusicDatas.add(musicData5_1);
                mMusicDatas.add(musicData5_2);
                mMusicDatas.add(musicData5_3);
                mMusicDatas.add(musicData5_4);
                mMusicDatas.add(musicData5_5);
                break;
        }
        serviceIntent = new Intent(this, MusicPlayerService.class);
        serviceIntent.putExtra(PARAM_MUSIC_LIST, (Serializable) mMusicDatas);
        startService(serviceIntent);
    }

    private void try2UpdateMusicPicBackground(final int musicPicRes) {
        if (mRootLayout.isNeed2UpdateBackground(musicPicRes)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Drawable foregroundDrawable = getForegroundDrawable(musicPicRes);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRootLayout.setForeground(foregroundDrawable);
                            mRootLayout.beginAnimation();
                        }
                    });
                }
            }).start();
        }
    }

    private Drawable getForegroundDrawable(int musicPicRes) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtil.getScreenWidth(MusicPlayerActivity.this)
                * 1.0 / DisplayUtil.getScreenHeight(this) * 1.0);

        Bitmap bitmap = getForegroundBitmap(musicPicRes);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 50, bitmap
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

    private Bitmap getForegroundBitmap(int musicPicRes) {
        int screenWidth = DisplayUtil.getScreenWidth(this);
        int screenHeight = DisplayUtil.getScreenHeight(this);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(getResources(), musicPicRes, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        if (imageWidth < screenWidth && imageHeight < screenHeight) {
            return BitmapFactory.decodeResource(getResources(), musicPicRes);
        }

        int sample = 2;
        int sampleX = imageWidth / DisplayUtil.getScreenWidth(this);
        int sampleY = imageHeight / DisplayUtil.getScreenHeight(this);

        if (sampleX > sampleY && sampleY > 1) {
            sample = sampleX;
        } else if (sampleY > sampleX && sampleX > 1) {
            sample = sampleY;
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeResource(getResources(), musicPicRes, options);
    }

    @Override
    public void onMusicInfoChanged(String musicName, String musicAuthor) {
        getSupportActionBar().setTitle(musicName);
        getSupportActionBar().setSubtitle(musicAuthor);
    }

    @Override
    public void onMusicPicChanged(int musicPicRes) {
        try2UpdateMusicPicBackground(musicPicRes);
    }

    @Override
    public void onMusicChanged(DiscView.MusicChangedStatus musicChangedStatus) {
        switch (musicChangedStatus) {
            case PLAY: {
                play();
                break;
            }
            case PAUSE: {
                pause();
                break;
            }
            case NEXT: {
                next();
                break;
            }
            case LAST: {
                last();
                break;
            }
            case STOP: {
                stop();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mIvPlayOrPause) {
            mDisc.playOrPause();
        } else if (v == mIvNext) {
            mDisc.next();
        } else if (v == mIvLast) {
            mDisc.last();
        } else if (v == mBack) {
            finish();
        }
    }

    private void play() {
        optMusic(MusicPlayerService.ACTION_OPT_MUSIC_PLAY);
        startUpdateSeekBarProgress();
    }

    private void pause() {
        optMusic(MusicPlayerService.ACTION_OPT_MUSIC_PAUSE);
        stopUpdateSeekBarProgree();
    }

    private void stop() {
        stopUpdateSeekBarProgree();
        mIvPlayOrPause.setImageResource(R.drawable.ic_play);
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
        mSeekBar.setProgress(0);
    }

    private void next() {
        mRootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                optMusic(MusicPlayerService.ACTION_OPT_MUSIC_NEXT);
            }
        }, DURATION_NEEDLE_ANIAMTOR);
        stopUpdateSeekBarProgree();
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
    }

    private void last() {
        mRootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                optMusic(MusicPlayerService.ACTION_OPT_MUSIC_LAST);
            }
        }, DURATION_NEEDLE_ANIAMTOR);
        stopUpdateSeekBarProgree();
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
    }

    private void complete(boolean isOver) {
        if (isOver) {
            mDisc.stop();
        } else {
            mDisc.next();
        }
    }

    private void optMusic(final String action) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }

    private void seekTo(int position) {
        Intent intent = new Intent(MusicPlayerService.ACTION_OPT_MUSIC_SEEK_TO);
        intent.putExtra(MusicPlayerService.PARAM_MUSIC_SEEK_TO, position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void startUpdateSeekBarProgress() {
        /*避免重复发送Message*/
        stopUpdateSeekBarProgree();
        mMusicHandler.sendEmptyMessageDelayed(0, 1000);
    }

    /*根据时长格式化称时间文本*/
    private String duration2Time(int duration) {
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        return (min < 10 ? "0" + min : min + "") + ":" + (sec < 10 ? "0" + sec : sec + "");
    }

    private void updateMusicDurationInfo(int totalDuration) {
        mSeekBar.setProgress(0);
        mSeekBar.setMax(totalDuration);
        mTvTotalMusicDuration.setText(duration2Time(totalDuration));
        mTvMusicDuration.setText(duration2Time(0));
        startUpdateSeekBarProgress();
    }

    class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicPlayerService.ACTION_STATUS_MUSIC_PLAY)) {
                mIvPlayOrPause.setImageResource(R.drawable.ic_pause);
                int currentPosition = intent.getIntExtra(MusicPlayerService.PARAM_MUSIC_CURRENT_POSITION, 0);
                mSeekBar.setProgress(currentPosition);
                if (!mDisc.isPlaying()) {
                    mDisc.playOrPause();
                }
            } else if (action.equals(MusicPlayerService.ACTION_STATUS_MUSIC_PAUSE)) {
                mIvPlayOrPause.setImageResource(R.drawable.ic_play);
                if (mDisc.isPlaying()) {
                    mDisc.playOrPause();
                }
            } else if (action.equals(MusicPlayerService.ACTION_STATUS_MUSIC_DURATION)) {
                int duration = intent.getIntExtra(MusicPlayerService.PARAM_MUSIC_DURATION, 0);
                updateMusicDurationInfo(duration);
            } else if (action.equals(MusicPlayerService.ACTION_STATUS_MUSIC_COMPLETE)) {
                boolean isOver = intent.getBooleanExtra(MusicPlayerService.PARAM_MUSIC_IS_OVER, true);
                complete(isOver);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver);
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }
}
