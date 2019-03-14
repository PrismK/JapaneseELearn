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
public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        View mHomeFragment = View.inflate(getActivity(), getLayoutId(), null);
        return mRootView;
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("首页");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
    }
}