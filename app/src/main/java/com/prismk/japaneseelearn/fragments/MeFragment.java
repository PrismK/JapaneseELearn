package com.prismk.japaneseelearn.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.activities.AllCollectionClassesActivity;
import com.prismk.japaneseelearn.widgets.Title;


/**
 * 首页Fragment
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    private View mMeFragment;
    private RelativeLayout rl_classescollection;
    private LinearLayout ll_classes_all;
    private LinearLayout ll_classes_notvip;
    private LinearLayout ll_classes_vip;
    private TextView tv_classes_all;
    private TextView tv_classes_notvip;
    private TextView tv_classes_vip;
    private ImageView imv_classes_all;
    private ImageView imv_classes_notvip;
    private ImageView iv_classes_vip;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mMeFragment = View.inflate(getActivity(), getLayoutId(), null);
        initView();
        setOnClickListener();
        return mRootView;
    }

    private void setOnClickListener() {
        rl_classescollection.setOnClickListener(this);
        ll_classes_all.setOnClickListener(this);
        ll_classes_notvip.setOnClickListener(this);
        ll_classes_vip.setOnClickListener(this);
    }

    private void initView() {
        rl_classescollection = mRootView.findViewById(R.id.rl_classescollection);

        ll_classes_all = mRootView.findViewById(R.id.ll_classes_all);
        ll_classes_notvip = mRootView.findViewById(R.id.ll_classes_notvip);
        ll_classes_vip = mRootView.findViewById(R.id.ll_classes_vip);

        tv_classes_all = mRootView.findViewById(R.id.tv_classes_all);
        tv_classes_notvip = mRootView.findViewById(R.id.tv_classes_notvip);
        tv_classes_vip = mRootView.findViewById(R.id.tv_classes_vip);

        imv_classes_all = mRootView.findViewById(R.id.imv_classes_all);
        imv_classes_notvip = mRootView.findViewById(R.id.imv_classes_notvip);
        iv_classes_vip = mRootView.findViewById(R.id.iv_classes_vip);
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarLight();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AllCollectionClassesActivity.class);
        switch (v.getId()) {
            case R.id.rl_classescollection:
            case R.id.ll_classes_all:
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.ll_classes_notvip:
                intent.putExtra("position", 1);
                startActivity(intent);
                break;
            case R.id.ll_classes_vip:
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
        }
    }
}