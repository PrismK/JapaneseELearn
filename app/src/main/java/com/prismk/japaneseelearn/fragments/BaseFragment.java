package com.prismk.japaneseelearn.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.prismk.japaneseelearn.R;

public abstract class BaseFragment extends Fragment {
    public final String TAG = "RouteMan_" + this.getClass().getSimpleName();

    protected View mRootView = null;

    private ImmersionBar mImmersionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initStatusBar();
    }

    protected abstract int getLayoutId();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    protected void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    private void removeStatusBar() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    protected void setStatusBarView() {
        if (mImmersionBar != null) {
            mImmersionBar.statusBarView(mRootView.findViewById(R.id.status_bar_view))
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    protected void setStatusBarViewNormal() {
        if (mImmersionBar != null) {
            mImmersionBar.statusBarView(mRootView.findViewById(R.id.status_bar_view))
                    .init();
        }
    }

    protected void setStatusBarDark() {
        if (mImmersionBar != null) {
            mImmersionBar.statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeStatusBar();
    }
}