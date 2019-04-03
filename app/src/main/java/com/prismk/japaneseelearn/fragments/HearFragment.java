package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.Resource;
import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.MusicPlayerActivity;
import com.prismk.japaneseelearn.views.loadview.ZProgressHUD;
import com.prismk.japaneseelearn.widgets.Title;

/**
 * 首页Fragment
 */
public class HearFragment extends BaseFragment implements View.OnClickListener {

    private CardView cv_radio_first;
    private CardView cv_radio_second;
    private CardView cv_radio_third;
    private CardView cv_radio_fourth;
    private CardView cv_radio_fifth;
    private ZProgressHUD progressHUD;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hear;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        initView();
        setOnClickListener();
        View mHearFragment = View.inflate(getActivity(), getLayoutId(), null);
        return mRootView;
    }

    private void setOnClickListener() {
        cv_radio_first.setOnClickListener(this);
        cv_radio_second.setOnClickListener(this);
        cv_radio_third.setOnClickListener(this);
        cv_radio_fourth.setOnClickListener(this);
        cv_radio_fifth.setOnClickListener(this);
    }

    private void initView() {
        cv_radio_first = mRootView.findViewById(R.id.cv_radio_first);
        cv_radio_second = mRootView.findViewById(R.id.cv_radio_second);
        cv_radio_third = mRootView.findViewById(R.id.cv_radio_third);
        cv_radio_fourth = mRootView.findViewById(R.id.cv_radio_fourth);
        cv_radio_fifth = mRootView.findViewById(R.id.cv_radio_fifth);
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("听力");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_radio_first:
                progressHUD = ZProgressHUD.getInstance(getActivity());
                progressHUD.setMessage("正在加载");
                progressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
                progressHUD.show();
                Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }
}