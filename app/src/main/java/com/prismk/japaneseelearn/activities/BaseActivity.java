package com.prismk.japaneseelearn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.managers.EventBusMananger;
import com.prismk.japaneseelearn.utils.PermissionsUtil;
import com.prismk.japaneseelearn.views.WaitingDialog;
import com.prismk.japaneseelearn.widgets.ELearnToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/9
 * 描    述：项目基础Activity，具体实现功能：状态栏、Activity跳转动画、
 * 页面跳转动画、权限管理工具使用方法、点击事件、返回键点击两次退出应用、
 * 修订历史：NULL
 * ================================================
 */

@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.SLIDE)
public abstract class BaseActivity extends AppCompatActivity {

    private ImmersionBar mImmersionBar;
    private InputMethodManager imm;
    private WaitingDialog waitingDialog;

    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(getLayoutId());
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setOnSlideListener();

        EventBusMananger.register(this);
    }


    public void checkPermissionRequest(PermissionsUtil.Permission permission, int requestCode) {
        if (!PermissionsUtil.checkAndRequestIfNoPermissionForActivity(this, permission, requestCode)) {
            return;
        }
    }

    public boolean isExemptActivity() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("This is " + getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * 必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，
         * 在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
         * */
        removeStatusBar();
        EventBusMananger.unregister(this);
        imm = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Object o) {

    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    /**
     * 子类可以实现此方法用于处理返回
     */
    public void goBack() {
        finish();
        goPreAnim();
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onViewClick(View view) {

    }

    private void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    private void removeStatusBar() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    protected void setStatusBarLight() {
        if (mImmersionBar != null) {
            mImmersionBar.reset()
                    .statusBarView(findViewById(R.id.status_bar_view))
                    .statusBarDarkFont(false, 0.2f)
                    .init();
        }
    }

    protected void setStatusBarView() {
        if (mImmersionBar != null) {
            mImmersionBar.reset()
                    .statusBarView(findViewById(R.id.status_bar_view))
                    .statusBarDarkFont(false, 0.2f)
                    .init();
        }
    }

    protected void setStatusBarViewWithKeyboard() {
        if (mImmersionBar != null) {
            mImmersionBar.reset()
                    .statusBarView(findViewById(R.id.status_bar_view))
                    .statusBarDarkFont(true, 0.2f)
                    .keyboardEnable(true)
                    .init();
        }
    }

    protected void setStatusBarDark() {
        if (mImmersionBar != null) {
            mImmersionBar.reset()
                    .statusBarView(findViewById(R.id.status_bar_view))
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    protected void disableStatusBar() {
        if (mImmersionBar != null) {
            mImmersionBar.reset()
                    .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                    .init();
        }
    }


    protected abstract int getLayoutId();

    private void setOnSlideListener() {
        ParallaxHelper.getParallaxBackLayout(this).setSlideCallback(new ParallaxBackLayout.ParallaxSlideCallback() {
            @Override
            public void onStateChanged(int state) {
                if (state == 1) {
                    hideSoftKeyBoard();
                }
            }

            @Override
            public void onPositionChanged(float percent) {

            }
        });
    }

    public void showSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 启动下一个Activity的动画
     */
    public void goNextAnim() {
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 返回上一个Activity的动画
     */
    public void goPreAnim() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /*淡入淡出动画*/
    public void goFadeAnim() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim
                .fade_out);
    }

    /*从下至上动画*/
    public void goUpAnim() {
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
    }

    /*从上至下动画*/
    public void goDownAnim() {
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
    }

    public void showLoginWaitingDialog() {
        waitingDialog = WaitingDialog.createLoginDialog(this);
        waitingDialog.show();
    }


    public void showWaitingDialog() {
        waitingDialog = WaitingDialog.createDialog(this);
        waitingDialog.show();
    }

    public void dismissWaitingDialog() {
        if (waitingDialog != null) {
            waitingDialog.dismiss();
            waitingDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ELearnToast.showShort("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }
    }

    public void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
