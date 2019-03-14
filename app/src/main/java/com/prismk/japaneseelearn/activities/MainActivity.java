package com.prismk.japaneseelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.gyf.barlibrary.ImmersionBar;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.fragments.BaseFragment;
import com.prismk.japaneseelearn.fragments.ClassesFragment;
import com.prismk.japaneseelearn.fragments.HearFragment;
import com.prismk.japaneseelearn.fragments.HomeFragment;
import com.prismk.japaneseelearn.fragments.MeFragment;
import com.prismk.japaneseelearn.fragments.WordFragment;

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

public class MainActivity extends AppCompatActivity {

    FrameLayout fl_fragment;

    LinearLayout ll_home;
    ImageView imv_home;
    TextView tv_home;

    LinearLayout ll_classes;
    ImageView imv_classes;
    TextView tv_classes;

    LinearLayout ll_hear;
    ImageView imv_hear;
    TextView tv_hear;

    LinearLayout ll_word;
    ImageView imv_word;
    TextView tv_word;

    LinearLayout ll_me;
    ImageView imv_me;
    TextView tv_me;

    private ImmersionBar mImmersionBar;

    //装fragment的实例集合
    private ArrayList<BaseFragment> fragments;

    //缓存Fragment或上次显示的Fragment
    private Fragment tempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initStatusBar();
        initView();
        initFragment();
        initOnClickListener();
        initLayout(ll_home);
    }

    private void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    private void initView() {
        fl_fragment = findViewById(R.id.fl_fragment);

        ll_home = findViewById(R.id.ll_home);
        imv_home = findViewById(R.id.imv_home);
        tv_home = findViewById(R.id.tv_home);

        ll_classes = findViewById(R.id.ll_classes);
        imv_classes = findViewById(R.id.imv_classes);
        tv_classes = findViewById(R.id.tv_classes);

        ll_hear = findViewById(R.id.ll_hear);
        imv_hear = findViewById(R.id.imv_hear);
        tv_hear = findViewById(R.id.tv_hear);

        ll_word = findViewById(R.id.ll_word);
        imv_word = findViewById(R.id.imv_word);
        tv_word = findViewById(R.id.tv_word);

        ll_me = findViewById(R.id.ll_me);
        imv_me = findViewById(R.id.imv_me);
        tv_me = findViewById(R.id.tv_me);
    }

    private void initOnClickListener() {
        ll_home.setOnClickListener(onBottomViewClickListener);
        imv_home.setOnClickListener(onBottomViewClickListener);
        tv_home.setOnClickListener(onBottomViewClickListener);

        ll_classes.setOnClickListener(onBottomViewClickListener);
        imv_classes.setOnClickListener(onBottomViewClickListener);
        tv_classes.setOnClickListener(onBottomViewClickListener);

        ll_hear.setOnClickListener(onBottomViewClickListener);
        ll_word.setOnClickListener(onBottomViewClickListener);
        ll_me.setOnClickListener(onBottomViewClickListener);
    }

    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private View.OnClickListener onBottomViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initLayout(v);
            onClickViewChange(v);
        }
    };

    private void onClickViewChange(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
            case R.id.imv_home:
            case R.id.tv_home:
                selectHome();
                resetClasses();
                resetHear();
                resetWord();
                resetMe();
                break;
            case R.id.ll_classes:
            case R.id.imv_classes:
            case R.id.tv_classes:
                resetHome();
                selectClasses();
                resetHear();
                resetWord();
                resetMe();
                break;
            case R.id.ll_hear:
            case R.id.imv_hear:
            case R.id.tv_hear:
                resetHome();
                resetClasses();
                selectHear();
                resetWord();
                resetMe();
                break;
            case R.id.ll_word:
            case R.id.imv_word:
            case R.id.tv_word:
                resetHome();
                resetClasses();
                resetHear();
                selectWord();
                resetMe();
                break;
            case R.id.ll_me:
            case R.id.imv_me:
            case R.id.tv_me:
                resetHome();
                resetClasses();
                resetHear();
                resetWord();
                selectMe();
                break;
        }
    }

    private void selectHome() {
        imv_home.setImageResource(getResources().getIdentifier("home_select", "mipmap", getPackageName()));
        tv_home.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void selectClasses() {
        imv_classes.setImageResource(getResources().getIdentifier("classes_select", "mipmap", getPackageName()));
        tv_classes.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void selectHear() {
        imv_hear.setImageResource(getResources().getIdentifier("hear_select", "mipmap", getPackageName()));
        tv_hear.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void selectWord() {
        imv_word.setImageResource(getResources().getIdentifier("word_select", "mipmap", getPackageName()));
        tv_word.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void selectMe() {
        imv_me.setImageResource(getResources().getIdentifier("me_select", "mipmap", getPackageName()));
        tv_me.setTextColor(getResources().getColor(R.color.blueMain));
    }

    private void resetHome() {
        imv_home.setImageResource(getResources().getIdentifier("home", "mipmap", getPackageName()));
        tv_home.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void resetClasses() {
        imv_classes.setImageResource(getResources().getIdentifier("classes", "mipmap", getPackageName()));
        tv_classes.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void resetHear() {
        imv_hear.setImageResource(getResources().getIdentifier("hear", "mipmap", getPackageName()));
        tv_hear.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void resetWord() {
        imv_word.setImageResource(getResources().getIdentifier("word", "mipmap", getPackageName()));
        tv_word.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void resetMe() {
        imv_me.setImageResource(getResources().getIdentifier("me", "mipmap", getPackageName()));
        tv_me.setTextColor(getResources().getColor(R.color.grayMain));
    }

    private void initLayout(View v) {
        int layoutId;
        if (v == ll_home || v == imv_home || v == tv_home)
            layoutId = 0;
        else if (v == ll_classes || v == imv_classes || v == tv_classes)
            layoutId = 1;
        else if (v == ll_hear || v == imv_hear || v == tv_hear)
            layoutId = 2;
        else if (v == ll_word || v == imv_word || v == tv_word)
            layoutId = 3;
        else if (v == ll_me || v == imv_me || v == tv_me)
            layoutId = 4;
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
        fragments.add(new ClassesFragment());
        fragments.add(new HearFragment());
        fragments.add(new WordFragment());
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
                    transaction.add(R.id.fl_fragment, nextFragment).commit();
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