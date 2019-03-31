package com.prismk.japaneseelearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.R;

/**
 * ================================================
 * 作    者：prism（棱镜）Github地址：https://github.com/prismk/
 * 本地用户：18340
 * 版    本：1.0
 * 创建日期：2019/3/31
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class VipCollectionClassesFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vip_classes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mMeFragment = View.inflate(getActivity(), getLayoutId(), null);
        return mRootView;
    }

}
