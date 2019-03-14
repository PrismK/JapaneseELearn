package com.prismk.japaneseelearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.widgets.Title;


/**
 * 首页Fragment
 */
public class MeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        View mMeFragment = View.inflate(getActivity(), getLayoutId(), null);
        return mRootView;
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("我的");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }
}