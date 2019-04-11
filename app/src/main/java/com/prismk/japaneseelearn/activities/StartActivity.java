package com.prismk.japaneseelearn.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.guidepage.GuideActivity;
import com.prismk.japaneseelearn.managers.TeacherFollowedDBManager;
import com.prismk.japaneseelearn.managers.UserDBManager;
import com.prismk.japaneseelearn.managers.VideoCollectionDBManager;
import com.prismk.japaneseelearn.managers.VideoDBManager;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/9
 * 描    述：启动页（闪屏页）
 * 修订历史：NULL
 * ================================================
 */

public class StartActivity extends BaseActivity {

    private UserDBManager mUserDBManager;
    private VideoDBManager mVideoDBManager;
    private VideoCollectionDBManager mVideoCollectionDBManager;
    private TeacherFollowedDBManager mTeacherFollowedDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setFullScreen();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBarLight();
        splashNormal();
    }

    private void initDBManager() {
        if (mUserDBManager == null) {
            mUserDBManager = new UserDBManager(this);
            mUserDBManager.openDataBase();
//            mUserDBManager.closeDataBase();
        }
        if (mVideoDBManager == null) {
            mVideoDBManager = new VideoDBManager(this);
            mVideoDBManager.openDataBase();
            mVideoDBManager.closeDataBase();
        }
        if (mVideoCollectionDBManager == null) {
            mVideoCollectionDBManager = new VideoCollectionDBManager(this);
            mVideoCollectionDBManager.openDataBase();
            mVideoCollectionDBManager.closeDataBase();
        }
        if (mTeacherFollowedDBManager == null) {
            mTeacherFollowedDBManager = new TeacherFollowedDBManager(this);
            mTeacherFollowedDBManager.openDataBase();
            mTeacherFollowedDBManager.closeDataBase();
        }
    }

    private void setFullScreen() {
        // 通过下面两行代码可实现全屏无标题栏显示activity,需要写在setContentView()方法上边
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    protected void disableBack() {
        ParallaxHelper.getParallaxBackLayout(this)
                .setEnableGesture(false);
    }

    private void splashNormal() {
        /********************************************************************************
         *
         * 普通闪屏实现方式
         *
         * ******************************************************************************/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initDBManager();
                SharedPreferences login_sp = getSharedPreferences("userInfo", 0);
                String name = login_sp.getString("USER_NAME", "");
                String pwd = login_sp.getString("PASSWORD", "");
                boolean isFirstIn = login_sp.getBoolean("isFirstIn", true);
                Intent intent = null;
                if (!name.isEmpty() && !pwd.isEmpty()) {
                    if (!mUserDBManager.getUserDataListFromUserDB().get(mUserDBManager.getLoginUesrID() - 1).isTeacherUser())
                        intent = new Intent(StartActivity.this, MainActivity.class);
                    else
                        intent = new Intent(StartActivity.this, MainOfTeacherActivity.class);
                } else if (name.isEmpty() && pwd.isEmpty() && isFirstIn == true){
                    intent = new Intent(StartActivity.this, GuideActivity.class);
                    SharedPreferences.Editor edit = login_sp.edit();
                    edit.putBoolean("isFirstIn",false);
                    edit.commit();
                } else {
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                goFadeAnim();
                finish();
            }
        }, 1000 * 1);
    }

    private void splashCountdown() {
        /********************************************************************************
         *
         * 倒计时闪屏实现方式
         *
         * ******************************************************************************/
        MyCountDownTimer mc = new MyCountDownTimer(1000, 1000);
        mc.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = StartActivity.this.getSharedPreferences("IsLogin", Context.MODE_PRIVATE);
                Boolean isLogin = sharedPreferences.getBoolean("islogin", false);
                if (isLogin) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    private class MyCountDownTimer extends CountDownTimer {
        /*
         * 此类用于需要在页面上显示倒计时的情况，配合splashCountdown方法使用
         * */
        //millisInFuture:倒计时的总数,单位毫秒
        //例如 millisInFuture=1000;表示1秒
        //countDownInterval:表示间隔多少毫秒,调用一次onTick方法()
        //例如: countDownInterval =1000;表示每1000毫秒调用一次onTick()
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            //tv_countDown.setText(getResources().getString(R.string.splash_countdown_finished));
        }

        public void onTick(long millisUntilFinished) {
            //tv_countDown.setText(getResources().getString(R.string.splash_countdown) + millisUntilFinished / 1000 );
        }
    }

    @Override
    protected void onDestroy() {
        mUserDBManager.closeDataBase();
        super.onDestroy();
    }
}
