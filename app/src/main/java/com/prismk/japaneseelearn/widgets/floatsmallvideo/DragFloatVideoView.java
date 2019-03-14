package com.prismk.japaneseelearn.widgets.floatsmallvideo;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.widgets.floatsmallvideo.util.Util;


/**
 * create by HiYang in 22
 *
 * @description 包含播放Ui组件的可拖动的悬浮视图组件
 */
public class DragFloatVideoView extends FrameLayout {
    private Context mContext;
    private WindowManager.LayoutParams lp;
    private final SurfaceView mSurfaceView;
    private final WindowManager mWindowManager;
    private final View mRoot;
    private final ImageButton mBtnClose;
    private AnimatorSet mShowAnimatorSet;
    private AnimatorSet mHideAnimatorSet;
    private boolean mIsAddView;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    show();
                    break;

                default:
                    break;
            }
        };
    };

    public void addToWindowManager(){
        if (getParent()!=null){
            mWindowManager.removeView(this);
        }
        Message ms = Message.obtain();
        ms.what= 1;
        mHandler.sendMessage(ms);
        mWindowManager.addView(this,lp);
    }

    public void removeSelf() {
        if (isAttachedToWindow()){
            mWindowManager.removeView(this);
        }
    }

    public DragFloatVideoView(Context context) {
        super(context);
        mContext = context;

        mRoot = inflate(mContext, R.layout.layout_touch_view, this);
        mSurfaceView = mRoot.findViewById(R.id.float_surface);
        mBtnClose = mRoot.findViewById(R.id.float_delete);

        mRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCLickListener !=null){
                    mOnCLickListener.onClicked();
                }
            }
        });

        mBtnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelf();
                if (mOnClosedListener!=null){
                    mOnClosedListener.onClosed();
                }
            }
        });

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams();
        lp.width = (int) context.getResources().getDimension(R.dimen.float_video_width);
        lp.height = (int) context.getResources().getDimension(R.dimen.float_video_height);

        int screenWidth = Util.getScreenWidth(context);
        int screenHeight = Util.getScreenHeight(context);
        lp.x = screenWidth;
        lp.y = screenHeight - lp.height - 200;
//        lp.windowAnimations = android.R.style.Animation_Translucent;
        lp.format = PixelFormat.TRANSPARENT;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

    }

    int lastX, lastY;
    int paramX, paramY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                paramX = lp.x;
                paramY = lp.y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getRawX() - lastX);
                int dy = (int) (event.getRawY() - lastY);
                lp.x = paramX + dx;
                lp.y = paramY + dy;
                mWindowManager.updateViewLayout(this, lp);
                break;
            case MotionEvent.ACTION_UP:
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                if (Math.abs(rawX - lastX) < 10 && Math.abs(rawY - lastY) < 10) {
                    performClick();
                }

                break;
        }
        return true;
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private OnRootClickListener mOnCLickListener;
    private OnClosedListener mOnClosedListener;

    public interface OnRootClickListener {
        void onClicked();
    }
    public interface OnClosedListener {
        void onClosed();
    }

    public void setOnCLickListener(OnRootClickListener onCLickListener) {
        mOnCLickListener = onCLickListener;
    }

    public void setOnClosedListener(OnClosedListener onClosedListener) {
        mOnClosedListener = onClosedListener;
    }

    public void setOnSurfaceViewCreatedListener(final OnSurfaceViewCreatedListener listener){
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (listener!=null){
                    listener.onCreated(holder.getSurface());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    public interface OnSurfaceViewCreatedListener{
        void onCreated(Surface surface);
    }

    private void show(){
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        setAnimation(in);
        mRoot.setVisibility(VISIBLE);
    }
}