package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.fragments.BaseFragment;
import com.prismk.japaneseelearn.fragments.HomeFragment;
import com.prismk.japaneseelearn.fragments.MeFragment;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2018/12/9
 * 描    述：登陆后的APP主页面
 * 修订历史：NULL
 * ================================================
 */

public class MainOfTeacherActivity extends AppCompatActivity {

    FrameLayout fl_fragmentOfTeacher;

    LinearLayout ll_home;
    ImageView imv_home;
    TextView tv_home;

    LinearLayout ll_me;
    ImageView imv_me;
    TextView tv_me;

    private ImmersionBar mImmersionBar;

    //装fragment的实例集合
    private ArrayList<BaseFragment> fragments;

    //缓存Fragment或上次显示的Fragment
    private Fragment tempFragment;
    private ImageButton imb_addResource;
    private FrameLayout fl_popview;
    private TranslateAnimation showAnim;
    private TranslateAnimation dismissAnim;
    private Button btn_release_vip;
    private Button btn_release_notvip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initStatusBar();
        initView();
        initFragment();
        initOnClickListener();
        initLayout(ll_home);
        initAmimation();
    }

    private void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    private void initView() {
        fl_fragmentOfTeacher = findViewById(R.id.fl_fragmentOfTeacher);
        ll_home = findViewById(R.id.ll_home);
        imv_home = findViewById(R.id.imv_home);
        tv_home = findViewById(R.id.tv_home);
        ll_me = findViewById(R.id.ll_me);
        imv_me = findViewById(R.id.imv_me);
        tv_me = findViewById(R.id.tv_me);
        imb_addResource = findViewById(R.id.imb_addResource);
        fl_popview = findViewById(R.id.fl_popview);
        btn_release_vip = findViewById(R.id.btn_release_vip);
        btn_release_notvip = findViewById(R.id.btn_release_notvip);
    }

    private void initOnClickListener() {
        ll_home.setOnClickListener(onBottomViewClickListener);
        ll_me.setOnClickListener(onBottomViewClickListener);
        imb_addResource.setOnClickListener(onBottomViewClickListener);
        fl_popview.setOnClickListener(onBottomViewClickListener);
        btn_release_notvip.setOnClickListener(onBottomViewClickListener);
        btn_release_vip.setOnClickListener(onBottomViewClickListener);
    }

    protected int getLayoutId() {
        return R.layout.activity_main_of_teacher;
    }

    private View.OnClickListener onBottomViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_home:
                case R.id.ll_me:
                    initLayout(v);
                    onClickViewChange(v);
                    break;
                case R.id.imb_addResource:
                    onClickViewChange(v);
                    showReleaseView();
                    break;
                case R.id.fl_popview:
                    dismissReleaseView();
                    break;
                case R.id.btn_release_notvip:
                    Intent intent = new Intent(MainOfTeacherActivity.this, ReleaseClassesActivity.class);
                    intent.putExtra("isVip",false);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_release_vip:
                    Intent intent1 = new Intent(MainOfTeacherActivity.this, ReleaseClassesActivity.class);
                    intent1.putExtra("isVip",true);
                    startActivity(intent1);
                    finish();
                    break;
            }

        }
    };

    private void initAmimation() {
        showAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        showAnim.setDuration(300);
        dismissAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f
        );
        dismissAnim.setDuration(300);
    }

    private void showReleaseView() {
        fl_popview.startAnimation(showAnim);
        fl_popview.setVisibility(View.VISIBLE);
    }

    private void dismissReleaseView() {
        fl_popview.startAnimation(dismissAnim);
        fl_popview.setVisibility(View.GONE);
    }

    private void onClickViewChange(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                selectHome();
                resetMe();
                break;
            case R.id.ll_me:
                resetHome();
                selectMe();
                break;
            case R.id.imb_addResource:

                break;
        }
    }

    private void selectHome() {
        imv_home.setImageResource(getResources().getIdentifier("home_select", "mipmap", getPackageName()));
        tv_home.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void selectMe() {
        imv_me.setImageResource(getResources().getIdentifier("me_select", "mipmap", getPackageName()));
        tv_me.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void resetHome() {
        imv_home.setImageResource(getResources().getIdentifier("home", "mipmap", getPackageName()));
        tv_home.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void resetMe() {
        imv_me.setImageResource(getResources().getIdentifier("me", "mipmap", getPackageName()));
        tv_me.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void initLayout(View v) {
        int layoutId;
        if (v == ll_home || v == imv_home || v == tv_home)
            layoutId = 0;
        else if (v == ll_me || v == imv_me || v == tv_me)
            layoutId = 1;
        else
            layoutId = 0;

        //根据位置得到相应的Fragment
        BaseFragment baseFragment = getFragment(layoutId);
        /**
         * 第一个参数: 上次显示的Fragment
         * 第二个参数: 当前正要显示的Fragment
         */
        switchFragment(tempFragment, baseFragment);

        switch (layoutId) {

        }
    }

    /**
     * 添加的时候按照顺序
     */
    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MeFragment());
    }

    /**
     * 根据位置得到对应的 Fragment
     *
     * @param position
     * @return
     */
    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            BaseFragment baseFragment = fragments.get(position);
            return baseFragment;
        }
        return null;
    }

    /**
     * 切换Fragment
     *
     * @param fragment
     * @param nextFragment
     */
    private void switchFragment(Fragment fragment, BaseFragment nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加成功
                if (!nextFragment.isAdded()) {
                    //隐藏当前的Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                    //添加Fragment
                    transaction.add(R.id.fl_fragmentOfTeacher, nextFragment).commit();
                } else {
                    //隐藏当前Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onBottomViewClickListener = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}