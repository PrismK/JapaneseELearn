package com.prismk.japaneseelearn.widgets.elearncamera.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.utils.SharedPreferencesUtil;
import com.prismk.japaneseelearn.widgets.elearncamera.state.CameraMachine;
import com.prismk.japaneseelearn.widgets.elearncamera.state.ErrorListener;
import com.prismk.japaneseelearn.widgets.elearncamera.util.FileUtil;
import com.prismk.japaneseelearn.widgets.elearncamera.util.ScreenUtils;

import java.io.IOException;


public class ElearnCamera extends FrameLayout implements CameraInterface.CameraOpenOverCallback, SurfaceHolder
        .Callback, CameraView, View.OnClickListener {

    //Camera状态机
    private CameraMachine machine;

    //闪关灯状态

    private String currentFlashModel = Camera.Parameters.FLASH_MODE_OFF;

    //拍照浏览时候的类型
    public static final int TYPE_PICTURE = 0x001;
    public static final int TYPE_VIDEO = 0x002;
    public static final int TYPE_SHORT = 0x003;
    public static final int TYPE_DEFAULT = 0x004;

    //录制视频比特率
    public static final int MEDIA_QUALITY_HIGH = 20 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 16 * 100000;
    public static final int MEDIA_QUALITY_LOW = 12 * 100000;
    public static final int MEDIA_QUALITY_POOR = 8 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 4 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 2 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 1 * 80000;


    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;      //只能拍照
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;     //只能录像
    public static final int BUTTON_STATE_BOTH = 0x103;              //两者都可以


    //回调监听
    private JCameraListener jCameraLisenter;


    private Context mContext;
    private VideoView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private ImageView mFlashLamp;
    private FoucsView mFoucsView;
    private MediaPlayer mMediaPlayer;

    private int layout_width;
    private float screenProp = 0f;

    private Bitmap captureBitmap;   //捕获的图片
    private Bitmap firstFrame;      //第一帧图片
    private String videoUrl;        //视频URL

    //缩放梯度
    private int zoomGradient = 0;

    private boolean firstTouch = true;
    private float firstTouchLength = 0;
    private ImageView imv_back;
    private Button btn_flash_off;
    private Button btn_flash_auto;
    private Button btn_flash_open;
    private LinearLayout ll_flash_cover;
    private Button btn_camera_take;
    private Button btn_camera_back;
    private Button btn_camera_submit;

    private boolean isTokenPhoto = false;

    public ElearnCamera(Context context) {
        this(context, null);
    }

    public ElearnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElearnCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initView();
    }

    private void initData() {
        layout_width = ScreenUtils.getScreenWidth(mContext);
        //缩放梯度
        zoomGradient = (int) (layout_width / 16f);
        machine = new CameraMachine(getContext(), this,
                this);
    }

    private void initView() {
        setWillNotDraw(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_widget_elearn_camera, this);
        mVideoView = (VideoView) view.findViewById(R.id.video_preview);
        mPhoto = (ImageView) view.findViewById(R.id.image_photo);
        mSwitchCamera = (ImageView) view.findViewById(R.id.image_switch);
        ll_flash_cover = (LinearLayout) view.findViewById(R.id.ll_flash_cover);
        mFlashLamp = (ImageView) view.findViewById(R.id.image_flash);
        btn_camera_back = (Button) view.findViewById(R.id.btn_camera_back);
        btn_camera_submit = (Button) view.findViewById(R.id.btn_camera_submit);
        imv_back = (ImageView) view.findViewById(R.id.imv_back);
        imv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(R.anim.top_in, R.anim.bottom_out);

            }
        });
        mFlashLamp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_flash_cover.getVisibility() == VISIBLE) {
                    ll_flash_cover.setVisibility(INVISIBLE);
                } else {
                    ll_flash_cover.setVisibility(VISIBLE);
                }

            }
        });
        btn_flash_off = (Button) view.findViewById(R.id.btn_flash_off);
        btn_flash_auto = (Button) view.findViewById(R.id.btn_flash_auto);
        btn_flash_open = (Button) view.findViewById(R.id.btn_flash_open);
        btn_camera_take = (Button) view.findViewById(R.id.btn_camera_take);


        mFoucsView = (FoucsView) view.findViewById(R.id.fouce_view);
        mVideoView.getHolder().addCallback(this);
        btn_flash_auto.setOnClickListener(this);
        btn_flash_open.setOnClickListener(this);
        btn_flash_off.setOnClickListener(this);
        btn_camera_take.setOnClickListener(this);
        btn_camera_back.setOnClickListener(this);
        btn_camera_submit.setOnClickListener(this);

        //切换摄像头
        mSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                machine.swtich(mVideoView.getHolder(), screenProp);
            }
        });

    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = mVideoView.getMeasuredWidth();
        float heightSize = mVideoView.getMeasuredHeight();
        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }
    }*/

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float widthSize = mVideoView.getMeasuredWidth();
        float heightSize = mVideoView.getMeasuredHeight();
        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(mVideoView.getHolder(), screenProp);
    }


    //生命周期onResume
    public void onResume() {
        //if (btn_camera_take.getVisibility() == VISIBLE) {
        if (!isTokenPhoto && btn_camera_take.getVisibility() == VISIBLE) {
            resetState(TYPE_DEFAULT); //重置状态
            CameraInterface.getInstance().registerSensorManager(mContext);
            CameraInterface.getInstance().setSwitchView(mSwitchCamera, mFlashLamp, imv_back, btn_flash_auto, btn_flash_off, btn_flash_open);
            machine.start(mVideoView.getHolder(), screenProp);
        }

    }

    //生命周期onPause
    public void onPause() {
        if (btn_camera_take.getVisibility() == VISIBLE) {
            stopVideo();
            resetState(TYPE_PICTURE);
            CameraInterface.getInstance().isPreview(false);
            CameraInterface.getInstance().unregisterSensorManager(mContext);
        }
    }

    //SurfaceView生命周期
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        CameraInterface.getInstance().doOpenCamera(ElearnCamera.this);
        String flashModeOff = Camera.Parameters.FLASH_MODE_OFF;
        String flashMode = SharedPreferencesUtil.getString(mContext, "flashmode", flashModeOff);
        refreshFlashMode(flashMode);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        SharedPreferencesUtil.setString(mContext,"flashmode",currentFlashModel);
        CameraInterface.getInstance().doDestroyCamera();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTokenPhoto) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getPointerCount() == 1) {
                        //显示对焦指示器
                        setFocusViewWidthAnimation(event.getX(), event.getY());
                    }
                    if (event.getPointerCount() == 2) {
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        firstTouch = true;
                    }
                    if (event.getPointerCount() == 2) {
                        //第一个点
                        float point_1_X = event.getX(0);
                        float point_1_Y = event.getY(0);
                        //第二个点
                        float point_2_X = event.getX(1);
                        float point_2_Y = event.getY(1);

                        float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                                point_2_Y, 2));

                        if (firstTouch) {
                            firstTouchLength = result;
                            firstTouch = false;
                        }
                        if ((int) (result - firstTouchLength) / zoomGradient != 0) {
                            firstTouch = true;
                            machine.zoom(result - firstTouchLength, CameraInterface.TYPE_CAPTURE);
                        }
//                    Log.i("CJT", "result = " + (result - firstTouchLength));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    firstTouch = true;
                    break;
            }
        }
        return true;
    }

    //对焦框指示器动画
    private void setFocusViewWidthAnimation(float x, float y) {
        machine.foucs(x, y, new CameraInterface.FocusCallback() {
            @Override
            public void focusSuccess() {
                mFoucsView.setVisibility(INVISIBLE);
            }
        });
    }

    private void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            FrameLayout.LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWidth());
            videoViewParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            videoViewParam.gravity = Gravity.CENTER;
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    /**************************************************
     * 对外提供的API                     *
     **************************************************/

    public void setSaveVideoPath(String path) {
        CameraInterface.getInstance().setSaveVideoPath(path);
    }


    public void setJCameraLisenter(JCameraListener jCameraLisenter) {
        this.jCameraLisenter = jCameraLisenter;
    }


    private ErrorListener errorLisenter;

    //启动Camera错误回调
    public void setErrorLisenter(ErrorListener errorLisenter) {
        this.errorLisenter = errorLisenter;
        CameraInterface.getInstance().setErrorLinsenter(errorLisenter);
    }


    //设置录制质量
    public void setMediaQuality(int quality) {
        CameraInterface.getInstance().setMediaQuality(quality);
    }

    @Override
    public void resetState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();    //停止播放
                //初始化VideoView
                FileUtil.deleteFile(videoUrl);
                mVideoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                machine.start(mVideoView.getHolder(), screenProp);
                break;
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);


                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                mVideoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                break;
        }
        mSwitchCamera.setVisibility(VISIBLE);
        mFlashLamp.setVisibility(VISIBLE);
        imv_back.setVisibility(VISIBLE);
    }

    @Override
    public void confirmState(int type) {
        switch (type) {
            case TYPE_VIDEO:
                stopVideo();    //停止播放
                mVideoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                machine.start(mVideoView.getHolder(), screenProp);
                if (jCameraLisenter != null) {
                    jCameraLisenter.recordSuccess(videoUrl, firstFrame);
                }
                break;
            case TYPE_PICTURE:

                if (jCameraLisenter != null) {
                    jCameraLisenter.captureSuccess(captureBitmap);
                }
                break;
            case TYPE_SHORT:
                break;
            case TYPE_DEFAULT:
                break;
        }
    }

    @Override
    public void showPicture(Bitmap bitmap, boolean isVertical) {
        if (isVertical) {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            mPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        captureBitmap = bitmap;
        mPhoto.setImageBitmap(bitmap);

    }

    @Override
    public void playVideo(Bitmap firstFrame, final String url) {
        videoUrl = url;
        this.firstFrame = firstFrame;
        new Thread(new Runnable() {
            //            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    if (mMediaPlayer == null) {
                        mMediaPlayer = new MediaPlayer();
                    } else {
                        mMediaPlayer.reset();
                    }
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
                            .OnVideoSizeChangedListener() {
                        @Override
                        public void
                        onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
                                    .getVideoHeight());
                        }
                    });
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mMediaPlayer.start();
                        }
                    });
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopVideo() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void setTip(String tip) {
    }

    @Override
    public void startPreviewCallback() {
        handlerFoucs(mFoucsView.getWidth() / 2, mFoucsView.getHeight() / 2);
    }

    @Override
    public boolean handlerFoucs(float x, float y) {

        mFoucsView.setVisibility(VISIBLE);
        if (x < mFoucsView.getWidth() / 2) {
            x = mFoucsView.getWidth() / 2;
        }
        if (x > layout_width - mFoucsView.getWidth() / 2) {
            x = layout_width - mFoucsView.getWidth() / 2;
        }
        if (y < mFoucsView.getWidth() / 2) {
            y = mFoucsView.getWidth() / 2;
        }
        mFoucsView.setX(x - mFoucsView.getWidth() / 2);
        mFoucsView.setY(y - mFoucsView.getHeight() / 2);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFoucsView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFoucsView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFoucsView, "alpha", 1f, 0.4f, 1f, 0.4f, 1f, 0.4f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera_back:
                machine.cancle(mVideoView.getHolder(), screenProp);
                btn_camera_back.setVisibility(INVISIBLE);
                btn_camera_submit.setVisibility(INVISIBLE);
                btn_camera_take.setVisibility(VISIBLE);
                isTokenPhoto = false;
                break;
            case R.id.btn_camera_submit:
                machine.confirm();
                break;
            case R.id.btn_camera_take:
                mFoucsView.setVisibility(INVISIBLE);
                takePicture();
                isTokenPhoto = true;
                break;
            case R.id.btn_flash_auto:
                refreshFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case R.id.btn_flash_off:
                refreshFlashMode(Camera.Parameters.FLASH_MODE_OFF);

                break;
            case R.id.btn_flash_open:
                refreshFlashMode(Camera.Parameters.FLASH_MODE_ON);

                break;
        }
        ll_flash_cover.setVisibility(INVISIBLE);
    }

    private void refreshFlashMode(String flashModeAuto) {
        machine.flash(flashModeAuto);
        switch (currentFlashModel) {
            case Camera.Parameters.FLASH_MODE_ON:
                btn_flash_open.setBackgroundResource(R.drawable.background_selector);
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                btn_flash_off.setBackgroundResource(R.drawable.background_selector);
                break;
            case Camera.Parameters.FLASH_MODE_AUTO:
                btn_flash_auto.setBackgroundResource(R.drawable.background_selector);
                break;
        }
        switch (flashModeAuto) {
            case Camera.Parameters.FLASH_MODE_ON:
                mFlashLamp.setImageResource(R.mipmap.camera_flash_on);
                btn_flash_open.setBackgroundResource(R.drawable.button);
                break;
            case Camera.Parameters.FLASH_MODE_OFF:
                mFlashLamp.setImageResource(R.mipmap.camera_flash_off);
                btn_flash_off.setBackgroundResource(R.drawable.button);
                break;
            case Camera.Parameters.FLASH_MODE_AUTO:
                mFlashLamp.setImageResource(R.mipmap.camera_flash_auto);
                btn_flash_auto.setBackgroundResource(R.drawable.button);

                break;
        }


        currentFlashModel = flashModeAuto;
    }

    private void takePicture() {
        mSwitchCamera.setVisibility(INVISIBLE);
        mFlashLamp.setVisibility(INVISIBLE);
        imv_back.setVisibility(INVISIBLE);
        ll_flash_cover.setVisibility(INVISIBLE);
        btn_camera_take.setVisibility(INVISIBLE);

        machine.capture();

        btn_camera_submit.setVisibility(VISIBLE);
        btn_camera_back.setVisibility(VISIBLE);

        mPhoto.setImageResource(0);
        mPhoto.setVisibility(VISIBLE);
    }

    public void onDestroy() {
        isTokenPhoto = false;
        CameraInterface.getInstance().destroyCameraInterface();
    }
}
