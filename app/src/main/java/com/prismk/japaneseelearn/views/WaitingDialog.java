package com.prismk.japaneseelearn.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prismk.japaneseelearn.R;

public class WaitingDialog extends Dialog {

    private WaitingDialog(Context context) {
        super(context);
    }

    private WaitingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_view_waitingdialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCanceledOnTouchOutside(false);
    }

    private WaitingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private WaitingDialog initView(Context context, boolean isLogin) {

        LinearLayout ll_animate = findViewById(R.id.ll_animate);
        if (isLogin) {
            ll_animate.setBackground(context.getResources().getDrawable(R.drawable.shape_login_waitinganimte_bg));
        }else {
            ll_animate.setBackground(context.getResources().getDrawable(R.drawable.shape_waitinganimate_bg));
        }

        return this;
    }

    public static WaitingDialog createDialog(Context context) {
        return new WaitingDialog(context, R.style.WaitingDialog).initView(context,false);
    }

    public static WaitingDialog createLoginDialog(Context context) {
        return new WaitingDialog(context, R.style.LoginWaitingDialog).initView(context,true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ImageView progressDialogView = (ImageView) findViewById(R.id.img_animate);
        AnimationDrawable animationDrawable = (AnimationDrawable) progressDialogView.getDrawable();
        animationDrawable.start();
        animationDrawable.setOneShot(false);
    }
}