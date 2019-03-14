package com.prismk.japaneseelearn.widgets.floatsmallvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.managers.floatsmallvideo.PlayState;


/**
 * create by HiYang in 2018/09/11
 *
 * @description 视频UI组件 只处理UI状态变化
 */
public class LiveVideoView extends RelativeLayout {
    private SurfaceView mSurfaceView;
    private ImageButton mToggleBtn;
    private View mShade;

    private @PlayState
    int mPlayState;

    public LiveVideoView(Context context) {
        this(context, null);

    }

    public LiveVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.live_video, this);
        mSurfaceView = findViewById(R.id.surface);
        mToggleBtn = findViewById(R.id.toggle);
        mShade = findViewById(R.id.shade);

        mSurfaceView.setOnClickListener(mListener);
        mToggleBtn.setOnClickListener(mListener);
    }

    public void onStatePause() {
        mPlayState = PlayState.PAUSE;
        adjustToggleIcon();
        setKeepScreenOn(false);
    }

    public void onStatePlay() {
        mPlayState = PlayState.PLAY;
        adjustToggleIcon();
        setKeepScreenOn(true);
    }

    private void adjustToggleIcon() {
        if (mPlayState == PlayState.PLAY) {
            mToggleBtn.setImageResource(R.drawable.ic_pause_64dp);
        } else {
            mToggleBtn.setImageResource(R.drawable.ic_play_arrow_64dp);
        }
    }

    private void toggleControls() {
        if (mShade.getVisibility() == View.VISIBLE) {
            mShade.setVisibility(View.GONE);
            mToggleBtn.setVisibility(View.GONE);
        } else {
            mShade.setVisibility(View.VISIBLE);
            mToggleBtn.setVisibility(View.VISIBLE);
        }
    }

    private OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.toggle:
                    if (onToggleClickedListener != null) {
                        onToggleClickedListener.onToggleClicked();
                    }
                    break;
                case R.id.surface:
                    toggleControls();
                    break;
            }
        }
    };

    public void addCallBack(SurfaceHolder.Callback callback) {
        mSurfaceView.getHolder().addCallback(callback);
    }

    private OnToggleClickedListener onToggleClickedListener;

    public interface OnToggleClickedListener {
        void onToggleClicked();
    }

    public void setOnToggleClickedListener(OnToggleClickedListener onToggleClickedListener) {
        this.onToggleClickedListener = onToggleClickedListener;
    }

}
